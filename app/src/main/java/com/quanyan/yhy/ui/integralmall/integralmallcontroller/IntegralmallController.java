package com.quanyan.yhy.ui.integralmall.integralmallcontroller;

import android.content.Context;
import android.os.Handler;

import com.quanyan.base.BaseController;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.yhy.common.beans.net.model.common.Booth;
import com.yhy.common.beans.net.model.item.CodeQueryDTO;
import com.yhy.common.beans.net.model.master.QueryTermsDTO;
import com.yhy.common.beans.net.model.pedometer.InviteShareInfo;
import com.yhy.common.beans.net.model.point.PointDetailQueryResult;
import com.yhy.common.beans.net.model.tm.DailyTaskQueryResult;
import com.yhy.common.beans.net.model.trip.ShortItemsResult;
import com.yhy.common.constants.ValueConstants;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:wjm
 * Date:16/621
 * Version 1.3.0
 */
public class IntegralmallController extends BaseController {

    public IntegralmallController(Context context, Handler handler) {
        super(context, handler);
    }

    public void doGetInviteShareInfo(Context context){
        NetManager.getInstance(context).doGetInviteShareInfo(new OnResponseListener<InviteShareInfo>() {
            @Override
            public void onComplete(boolean isOK, InviteShareInfo result, int errorCode, String errorMsg) {
                if(isOK && result != null){
                    sendMessage(ValueConstants.MSG_HOTEL_HOME_BANNER_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_HOTEL_HOME_BANNER_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_HOTEL_HOME_BANNER_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取积分商城首页的广告位
     */
    public void doGetHotSearchList(final Context context, String code) {
        NetManager.getInstance(context).doGetBooth(code, new OnResponseListener<Booth>() {
            @Override
            public void onComplete(boolean isOK, Booth result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new Booth();
                    }
                    sendMessage(ValueConstants.MSG_HOTEL_HOME_BANNER_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_HOTEL_HOME_BANNER_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_HOTEL_HOME_BANNER_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 总积分查询
     */
    public void doTotalPointQuery(final Context context) {
        NetManager.getInstance(context).doTotalPointQuery(new OnResponseListener<Long>() {
            @Override
            public void onComplete(boolean isOK, Long result, int errorCode, String errorMsg) {
                if (isOK) {

                    sendMessage(ValueConstants.MSG_INTEGRALMALL_TOTALPOINT_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_INTEGRALMALL_TOTALPOINT_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_INTEGRALMALL_TOTALPOINT_KO, errorCode, 0, errorMessage);
            }
        });
    }


    /**
     * 每日任务清单查询
     */
    public void doDailyTaskQuery(final Context context, int pageNo, int pageSize) {
        NetManager.getInstance(context).doDailyTaskQuery(pageNo, pageSize, new OnResponseListener<DailyTaskQueryResult>() {
            @Override
            public void onComplete(boolean isOK, DailyTaskQueryResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new DailyTaskQueryResult();
                    }
                    sendMessage(ValueConstants.MSG_INTEGRALMALL_DAILYTASK_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_INTEGRALMALL_DAILYTASK_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_INTEGRALMALL_DAILYTASK_KO, errorCode, 0, errorMessage);
            }
        });
    }


    /**
     * 每日任务清单查询
     */
    public void doPointDetailQuery(final Context context, int pageNo, int pageSize) {
        NetManager.getInstance(context).doPointDetailQuery(pageNo, pageSize, new OnResponseListener<PointDetailQueryResult>() {
            @Override
            public void onComplete(boolean isOK, PointDetailQueryResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new PointDetailQueryResult();
                    }
                    sendMessage(ValueConstants.MSG_INTEGRALMALL_POINTDETAIL_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_INTEGRALMALL_POINTDETAIL_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_INTEGRALMALL_POINTDETAIL_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 分享每日步数
     */
    public void doShareDailySteps(final Context context) {
        NetManager.getInstance(context).doShareDailySteps(new OnResponseListener<Long>() {
            @Override
            public void onComplete(boolean isOK, Long result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_INTEGRALMALL_COMPLETETASK_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_INTEGRALMALL_COMPLETETASK_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_INTEGRALMALL_COMPLETETASK_KO, errorCode, 0, errorMessage);
            }
        });
    }


    /**
     * 根据展位获取商品列表
     * @param context
     * @param code
     */
    public void doGetIntegralmallListByCode(final Context context, CodeQueryDTO code){
        NetManager.getInstance(context).doGetItemListByCode(code, new OnResponseListener<ShortItemsResult>() {

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_INTEGRALMALL_LIST_KO, errorCode, 0, errorMessage);
            }

            @Override
            public void onComplete(boolean isOK, ShortItemsResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_INTEGRALMALL_LIST_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_INTEGRALMALL_LIST_KO, errorCode, 0, errorMsg);
            }
        });
    }

    /**
     * 运动页面分页获取商品列表
     * @param context
     * @param code
     */
    public void doGetIntegralmallListByPage(final Context context, CodeQueryDTO code){
        NetManager.getInstance(context).doGetItemListForPointMall(code, new OnResponseListener<ShortItemsResult>() {
//        NetManager.getInstance(context).doGetItemListByCode(code, new OnResponseListener<ShortItemsResult>() {

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_INTEGRALMALL_LIST_KO, errorCode, 0, errorMessage);
            }

            @Override
            public void onComplete(boolean isOK, ShortItemsResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_INTEGRALMALL_LIST_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_INTEGRALMALL_LIST_KO, errorCode, 0, errorMsg);
            }
        });
    }
}
