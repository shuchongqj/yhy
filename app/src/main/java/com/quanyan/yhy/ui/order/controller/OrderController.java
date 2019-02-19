package com.quanyan.yhy.ui.order.controller;

import android.content.Context;
import android.os.Handler;

import com.quanyan.base.BaseController;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.smart.sdk.api.resp.Api_TRADEMANAGER_QueryOrderVoucherDTO;
import com.yhy.common.beans.net.model.tm.OrderVoucherResult;
import com.yhy.common.beans.net.model.tm.TmCreateOrderContext;
import com.yhy.common.beans.net.model.tm.TmCreateOrderParam;
import com.yhy.common.beans.net.model.tm.TmCreateOrderResultTO;
import com.yhy.common.constants.ValueConstants;

/**
 * Created with Android Studio.
 * Title:OrderController
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-4-7
 * Time:13:55
 * Version 1.0
 * Description:
 */
public class OrderController extends BaseController {

    public OrderController(Context context, Handler handler) {
        super(context, handler);
    }

    /**
     * 获取订单上下文信息
     *
     * @param id
     */
    public void getCreateOrderContext(final Context context, long id) {
        NetManager.getInstance(context).doGetCreateOrderContext(id, new OnResponseListener<TmCreateOrderContext>() {
            @Override
            public void onComplete(boolean isOK, TmCreateOrderContext result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_CLUB_DETAIL_INFO_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_CLUB_DETAIL_INFO_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_CLUB_DETAIL_INFO_KO, errorCode, 0, errorMessage);
            }
        });
    }


    /**
     * 提交订单
     *
     * @param param
     */
    public void doCreateOrder(final Context context, TmCreateOrderParam param) {
        NetManager.getInstance(context).doCreateOrder(param, new OnResponseListener<TmCreateOrderResultTO>() {
            @Override
            public void onComplete(boolean isOK, TmCreateOrderResultTO result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_CREATE_ORDER_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_CREATE_ORDER_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_CREATE_ORDER_KO, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 查询我的总积分
     *
     * @param context
     */
    public void doTotalPointQuery(final Context context) {
        NetManager.getInstance(context).doTotalPointQuery(new OnResponseListener<Long>() {
            @Override
            public void onComplete(boolean isOK, Long result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.POINT_TOTAL_OK, result);
                    return;
                }
                sendMessage(ValueConstants.POINT_TOTAL_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.POINT_TOTAL_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 查询订单优惠券
     *
     * @param context
     * @param queryOrderVoucherDTO
     */
    public void doQueryOrderVoucherList(final Context context, Api_TRADEMANAGER_QueryOrderVoucherDTO queryOrderVoucherDTO) {
        NetManager.getInstance(context).doQueryOrderVoucherList(queryOrderVoucherDTO, new OnResponseListener<OrderVoucherResult>() {
            @Override
            public void onComplete(boolean isOK, OrderVoucherResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_CREATE_ORDER_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_CREATE_ORDER_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_CREATE_ORDER_KO, errorCode, 0, errorMessage);
            }
        });
    }

}
