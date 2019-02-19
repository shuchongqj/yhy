package com.mogujie.tt.imservice.manager;

import com.mogujie.tt.imservice.event.ConsultEvent;
import com.mogujie.tt.ui.activity.MessageActivity;
import com.mogujie.tt.utils.Logger;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.quanyan.yhy.ui.consult.ConsultMasterReciveDialogActivity;
import com.quanyan.yhy.ui.consult.ConsultUserReplyDialogActivity;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_ProcessStateQuery;
import com.yhy.common.beans.im.entity.ConsultControlEntity;
import com.yhy.common.beans.im.entity.SessionEntity;
import com.yhy.common.beans.net.model.tm.ConsultState;
import com.yhy.common.beans.net.model.tm.ProcessState;
import com.yhy.common.beans.net.model.trip.ShortItem;
import com.yhy.common.beans.net.model.trip.ShortItemsResult;
import com.yhy.common.constants.ConsultContants;
import com.yhy.common.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.greenrobot.event.EventBus;

/**
 * 负责咨询相关服务
 * 为回话页面以及联系人页面提供服务
 * <p/>
 */
public class IMConsultManager extends IMManager {
    private Logger logger = Logger.getLogger(IMConsultManager.class);

    // 单例
    private static IMConsultManager inst = new IMConsultManager();
//    private boolean consultDataReady = true;

    public static IMConsultManager instance() {
        return inst;
    }

    private ConsultState consultState;
    private Map<Long, ShortItem> itemMaps = new ConcurrentHashMap<>();

    @Override
    public void doOnStart() {
    }

    @Override
    public void reset() {
//        consultDataReady = false;
        itemMaps.clear();
    }

    /**
     * 登陆成功触发
     * auto自动登陆
     */
    public void onNormalLoginOk() {
        onLocalNetOk();
    }

    public void onLocalNetOk() {
        requestConsultInfo(true);
    }

    public void requestConsultInfo(boolean needShowDialog) {
        final boolean show = needShowDialog;
        NetManager.getInstance(ctx).doGetConsultInfoForSeller(null, new OnResponseListener<ConsultState>() {
            @Override
            public void onComplete(boolean isOK, ConsultState result, int errorCode, String errorMsg) {
                if (isOK && result != null) {
                    consultState = result;
                    if (show) {
                        if (consultState.consultInfo.sellerQueueLength > 0 && (consultState.processOrder == null || !consultState.processOrder.processOrderStatus.equals(ConsultContants.STATUS_CONSULT_IN_CHAT))) {
                            ConsultMasterReciveDialogActivity.gotoConsultMasterReciveDialogActivity(ctx);
                        }
                    }
                    triggerEvent(new ConsultEvent(ConsultEvent.Event.CONSULT_QUEUE_UPDATE, consultState));
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {

            }
        });
    }

    public long getSellerQueueLength() {
        return consultState == null ? 0 : consultState.consultInfo.sellerQueueLength;
    }

    /**
     * @param event
     */
    public void triggerEvent(ConsultEvent event) {
        EventBus.getDefault().postSticky(event);
    }

    public void checkAllConsultInfoUpdate() {
        List<SessionEntity> sessionEntityList = IMSessionManager.instance().getRecentSessionList();
        if (sessionEntityList.size() == 0) {
            return;
        }
        List<Long> ids = new ArrayList<>();
        for (SessionEntity entity : sessionEntityList) {
            long serviceId = entity.getServiceId();
            if (serviceId == 0) continue;
            if (itemMaps.get(serviceId) == null)
                ids.add(serviceId);
        }

        if (ids.size() > 0) {
            getItemListByItemIds(ids);
        }
    }

    public void getItemListByItemIds(List<Long> ids) {
        final long[] itemIds = new long[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            itemIds[i] = ids.get(i);
        }
        NetManager.getInstance(ctx).doGetItemListByItemIds(itemIds, new OnResponseListener<ShortItemsResult>() {
            @Override
            public void onComplete(boolean isOK, ShortItemsResult result, int errorCode, String errorMsg) {
                if (isOK && result != null && result.shortItemList != null) {
                    for (ShortItem item : result.shortItemList) {
                        itemMaps.put(item.id, item);
                    }
                    triggerEvent(new ConsultEvent(ConsultEvent.Event.SESSION_CONSULT_OK));
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {

            }
        });
    }


    public Map<Long, ShortItem> getItemMap() {
        return itemMaps;
    }

    public void getProcessState(long fromId, long toId, long itemId) {
        Api_TRADEMANAGER_ProcessStateQuery params = new Api_TRADEMANAGER_ProcessStateQuery();
        params.fromUserId = fromId;
        params.toUserId = toId;
        params.itemId = itemId;
        params.processType = "CONSULT";
        NetManager.getInstance(ctx).doGetProcessState(params, new OnResponseListener<ProcessState>() {
            @Override
            public void onComplete(boolean isOK, ProcessState result, int errorCode, String errorMsg) {
                if (result == null || result.equals("")) {
                    return;
                }
                if (isOK && result != null) {
                    triggerEvent(new ConsultEvent(ConsultEvent.Event.PROCESS_STATE_UPDATE, result));
                    return;
                }
                triggerEvent(new ConsultEvent(ConsultEvent.Event.PROCESS_STATE_UPDATE_KO, errorCode));
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                triggerEvent(new ConsultEvent(ConsultEvent.Event.PROCESS_STATE_UPDATE_KO, errorCode));
            }
        });
    }

    public void dispatchControllMessage(String content) {
        ConsultControlEntity entity = ConsultControlEntity.parseFromJson(content);
        if (entity == null) return;
        triggerEvent(new ConsultEvent(ConsultEvent.Event.CONSULT_CONTROL, entity));
        String tags = entity.getTags();
        if (tags.equals(ConsultContants.DEQUEUE)) {
            //离开队伍
            if (IMLoginManager.instance().getLoginId() == entity.getSellerId()) {
                requestConsultInfo(false);
            }
        } else if (tags.equals(ConsultContants.ENQUEUE)) {
            if (IMLoginManager.instance().getLoginId() == entity.getSellerId()) {
                requestConsultInfo(false);
            }
        } else if (tags.equals(ConsultContants.WAITING)) {
            ConsultMasterReciveDialogActivity.gotoConsultMasterReciveDialogActivity(ctx);
        } else if (tags.equals(ConsultContants.CANCEL)) {

        } else if (tags.equals(ConsultContants.OVER)) {
            if (IMLoginManager.instance().getLoginId() == entity.getSellerId()) {
                requestConsultInfo(false);
            }
        } else if (tags.equals(ConsultContants.START)) {
            if (IMLoginManager.instance().getLoginId() == entity.getBuyerId()) {
                if (!CommonUtil.isTopActivy(ctx, MessageActivity.class.getName()) && !CommonUtil.isTopActivy(ctx, ConsultUserReplyDialogActivity.class.getName())) {
                    ConsultUserReplyDialogActivity.gotoConsultUserReplyDialogActivity(ctx, entity.getItemId(), entity.getSellerId());
                }
            }
        }
    }


}
