package com.quanyan.yhy.ui.consult.controller;

import android.content.Context;
import android.os.Handler;

import com.quanyan.base.BaseController;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_CreateProcessOrderParam;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_ProcessOrderContent;
import com.yhy.common.beans.net.model.common.Booth;
import com.yhy.common.beans.net.model.tm.AcceptProcessOrderResult;
import com.yhy.common.beans.net.model.tm.CancelProcessResult;
import com.yhy.common.beans.net.model.tm.ConsultContent;
import com.yhy.common.beans.net.model.tm.CreateProcessOrderResultTO;
import com.yhy.common.beans.net.model.tm.FinishProcessResult;
import com.yhy.common.beans.net.model.tm.SellerAndConsultStateResult;
import com.yhy.common.beans.net.model.tm.TmConsultInfo;
import com.yhy.common.beans.net.model.trip.ShortItemsResult;
import com.yhy.common.constants.ValueConstants;

import java.util.List;

/**
 * Created with Android Studio.
 * Title:ConsultController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/6/29
 * Time:17:18
 * Version 1.0
 */
public class ConsultController extends BaseController {

    public ConsultController(Context context, Handler handler) {
        super(context, handler);
    }

    //    创建流程单 trademanager.createProcessOrder
//    查询用户能否下单 trademanager.querySellerAndConsultState

    /**
     * 查询咨询状态
     *
     * @param context
     * @param itemId
     */
    public void querySellerAndConsultState(Context context, long itemId) {
        NetManager.getInstance(context).doQuerySellerAndConsultState(itemId, new OnResponseListener<SellerAndConsultStateResult>() {
            @Override
            public void onComplete(boolean isOK, SellerAndConsultStateResult result, int errorCode, String errorMsg) {
                if (isOK && result != null) {
                    sendMessage(ValueConstants.SELLER_AND_CONSULT_STATE_OK, errorCode, 0, result);
                } else {
                    sendMessage(ValueConstants.SELLER_AND_CONSULT_STATE_KO, errorCode, 0, errorMsg);
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.SELLER_AND_CONSULT_STATE_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 查询咨询状态
     *
     * @param context
     * @param itemId
     */
    public void querySellerAndConsultStateWhenCommit(Context context, long itemId) {
        NetManager.getInstance(context).doQuerySellerAndConsultState(itemId, new OnResponseListener<SellerAndConsultStateResult>() {
            @Override
            public void onComplete(boolean isOK, SellerAndConsultStateResult result, int errorCode, String errorMsg) {
                if (isOK && result != null) {
                    sendMessage(ValueConstants.SELLER_AND_CONSULT_STATE_WHEN_COMMINT_OK, errorCode, 0, result);
                } else {
                    sendMessage(ValueConstants.SELLER_AND_CONSULT_STATE_WHEN_COMMINT_KO, errorCode, 0, errorMsg);
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.SELLER_AND_CONSULT_STATE_WHEN_COMMINT_KO, errorCode, 0, errorMessage);
            }
        });
    }


    /**
     * 创建咨询订单
     *
     * @param itemId            咨询商品id
     * @param descriptionString 标签
     * @param demandDetail      咨询内容 edittext
     * @param travelDays        旅游天数
     * @param persons           人数
     * @param city              目的地城市
     */
    public void createProcessOrder(Context context, long itemId, List<String> descriptionString, String demandDetail, int travelDays, int persons, String city, String source) {
        Api_TRADEMANAGER_CreateProcessOrderParam param = new Api_TRADEMANAGER_CreateProcessOrderParam();
        param.itemId = itemId;
        param.processType = "CONSULT";
        param.processOrderSource = source;
        param.processOrderContent = new Api_TRADEMANAGER_ProcessOrderContent();
        param.processOrderContent.demandDescription = descriptionString;
        param.processOrderContent.demandDetail = demandDetail;
        param.processOrderContent.travelDays = travelDays;
        param.processOrderContent.personNum = persons;
        param.processOrderContent.destination = city;
        NetManager.getInstance(context).doCreateProcessOrder(param, new OnResponseListener<CreateProcessOrderResultTO>() {
            @Override
            public void onComplete(boolean isOK, CreateProcessOrderResultTO result, int errorCode, String errorMsg) {
                if (isOK && result != null) {
                    sendMessage(ValueConstants.CREATE_PROCESS_ORDER_OK, result);
                } else {
                    sendMessage(ValueConstants.CREATE_PROCESS_ORDER_KO, errorCode, 0, errorMsg);
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.CREATE_PROCESS_ORDER_KO, errorCode, 0, errorMessage);
            }
        });
    }

    //达人接单
    public void acceptProcessOrder(Context context) {
        NetManager.getInstance(context).doAcceptProcessOrder(0, "CONSULT", new OnResponseListener<AcceptProcessOrderResult>() {
            @Override
            public void onComplete(boolean isOK, AcceptProcessOrderResult result, int errorCode, String errorMsg) {
                if (isOK && result != null) {
                    sendMessage(ValueConstants.ACCEPT_PROCESS_ORDER_OK, result);
                } else {
                    sendMessage(ValueConstants.ACCEPT_PROCESS_ORDER_KO, errorCode, 0, errorMsg);
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.ACCEPT_PROCESS_ORDER_KO, errorCode, 0, errorMessage);
            }
        });
    }

    public void cancelProcessOrder(Context context, long processOrderId) {
        NetManager.getInstance(context).doCancelProcessOrder(processOrderId, new OnResponseListener<CancelProcessResult>() {
            @Override
            public void onComplete(boolean isOK, CancelProcessResult result, int errorCode, String errorMsg) {
                if (isOK && result != null) {
                    sendMessage(ValueConstants.CANCEL_CONSULT_ORDER_OK, result);
                } else {
                    sendMessage(ValueConstants.CANCEL_CONSULT_ORDER_KO, errorCode, 0, errorMsg);
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.CANCEL_CONSULT_ORDER_KO, errorCode, 0, errorMessage);
            }
        });
    }

    public void finishConsult(Context context, long itemId, long processId) {
        NetManager.getInstance(context).doFinishConsult(itemId, processId, new OnResponseListener<FinishProcessResult>() {
            @Override
            public void onComplete(boolean isOK, FinishProcessResult result, int errorCode, String errorMsg) {
                if (isOK && result != null) {
                    sendMessage(ValueConstants.FINISH_CONSULT_ORDER_OK, result);
                } else {
                    sendMessage(ValueConstants.FINISH_CONSULT_ORDER_KO, errorCode, 0, errorMsg);
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.FINISH_CONSULT_ORDER_KO, errorCode, 0, errorMessage);
            }
        });
    }


    public void getFastConsultItem(Context context, List<String> checkedList, String cityCode, String consultDes) {


        ConsultContent content = new ConsultContent();
        content.consultDes = consultDes;
        content.consultTags = checkedList;
        content.place = cityCode;
        NetManager.getInstance(context).doGetFastConsultItem(content, new OnResponseListener<TmConsultInfo>() {
            @Override
            public void onComplete(boolean isOK, TmConsultInfo result, int errorCode, String errorMsg) {
                if (isOK && result != null) {
                    sendMessage(ValueConstants.GET_FAST_CONSULT_OK, result);
                } else {
                    sendMessage(ValueConstants.GET_FAST_CONSULT_KO, errorCode, 0, errorMsg);
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.GET_FAST_CONSULT_KO, errorCode, 0, errorMessage);
            }
        });
    }

    public void getBooth(Context context, String code) {
        NetManager.getInstance(context).doGetBooth(code, new OnResponseListener<Booth>() {
            @Override
            public void onComplete(boolean isOK, Booth result, int errorCode, String errorMsg) {
                if (isOK && result != null) {
                    sendMessage(ValueConstants.GET_CONSULT_BOOTH_OK, result);
                } else {
                    sendMessage(ValueConstants.GET_CONSULT_BOOTH_KO, errorCode, 0, errorMsg);
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.GET_CONSULT_BOOTH_KO, errorCode, 0, errorMessage);
            }
        });
    }

    public void getItems(Context context, long id) {
        final long[] itemIds = new long[]{id};
        NetManager.getInstance(context).doGetItemListByItemIds(itemIds, new OnResponseListener<ShortItemsResult>() {
            @Override
            public void onComplete(boolean isOK, ShortItemsResult result, int errorCode, String errorMsg) {
                if (isOK && result != null && result.shortItemList != null && result.shortItemList.size() > 0) {
                    sendMessage(ValueConstants.GET_ITEMS_OK, result.shortItemList.get(0));
                    return;
                }
                sendMessage(ValueConstants.GET_ITEMS_KO, errorCode);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.GET_ITEMS_KO, errorCode);
            }
        });
    }
}
