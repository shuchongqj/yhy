package com.quanyan.yhy.ui.coupon.controller;

import android.content.Context;
import android.os.Handler;

import com.quanyan.base.BaseController;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.yhy.common.beans.net.model.tm.VoucherResultList;
import com.yhy.common.beans.net.model.tm.VoucherTemplateResultList;
import com.yhy.common.constants.ValueConstants;

/**
 * Created with Android Studio.
 * Title:***Acitivity
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:王东旭
 * Date:2016/6/1
 * Time:16:46
 * Version 2.0
 */
public class CouponController extends BaseController {

    public CouponController(Context context, Handler handler) {
        super(context, handler);
    }

    /**
     * 获取优惠券列表
     * @param context
     * @param status
     * @param pageSize
     * @param pageNo
     */

    public void doGetCouponList(Context context, final String status , int pageSize, final int pageNo){
        NetManager.getInstance(context).doQueryMyVoucherList( status,  pageSize,  pageNo, new OnResponseListener<VoucherResultList>() {
            @Override
            public void onComplete(boolean isOK, VoucherResultList result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.COUPON_INFO_LIST_SUCCESS, 0, pageNo, result);
                    return;
                }
                sendMessage(ValueConstants.COUPON_INFO_LIST_FAIL, 0, errorCode, errorMsg);
            }
            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.COUPON_INFO_LIST_FAIL, 0, errorCode, errorMessage);
            }
        });
    }
    /**
     * 获取商品优惠券列表
     * @param context
     * @param itemId
     */
    public void doGetCouponSeller(Context context, final long itemId,int pageSize,int pageNo){
        NetManager.getInstance(context).doQueryItemVoucherList( pageSize ,pageNo,itemId,new OnResponseListener<VoucherTemplateResultList>() {
            @Override
            public void onComplete(boolean isOK, VoucherTemplateResultList result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.COUPON_INFO_SELLER_SUCCESS, 0, 0, result);
                    return;
                }
                sendMessage(ValueConstants.COUPON_INFO_SELLER_FAIL, 0, errorCode, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.COUPON_INFO_SELLER_FAIL, 0, errorCode, errorMessage);
            }
        });
    }
    /**
     * 领取商品优惠券
     * @param context
     * @param itemId
     */
    public void doGetGenerateVoucher(Context context, final long itemId){
        NetManager.getInstance(context).doGenerateVoucher( itemId, new OnResponseListener<Boolean>() {

            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.COUPON_INFO_GET_SUCCESS, 0, 0, result);
                    return;
                }
                sendMessage(ValueConstants.COUPON_INFO_GET_FAIL, 0, errorCode, errorMsg);
            }
            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.COUPON_INFO_GET_FAIL, 0, errorCode, errorMessage);
            }

        });

    }

}
