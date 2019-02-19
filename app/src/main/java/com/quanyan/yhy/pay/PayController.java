package com.quanyan.yhy.pay;

import android.content.Context;
import android.os.Handler;

import com.quanyan.base.BaseController;
import com.quanyan.yhy.common.PayStatus;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.yhy.common.beans.net.model.paycore.CebCloudPayInfo;
import com.yhy.common.beans.net.model.paycore.CebCloudPayParam;
import com.yhy.common.beans.net.model.paycore.EleAccountInfo;
import com.yhy.common.beans.net.model.paycore.ElePursePayParam;
import com.yhy.common.beans.net.model.paycore.PayCoreBaseResult;
import com.yhy.common.beans.net.model.paycore.PcPayStatusInfo;
import com.yhy.common.beans.net.model.paycore.WxBatchPayParam;
import com.yhy.common.beans.net.model.paycore.WxPayInfo;
import com.yhy.common.beans.net.model.tm.TmPayInfo;
import com.yhy.common.beans.net.model.tm.TmPayStatusInfo;
import com.yhy.common.constants.ValueConstants;

/**
 * Created with Android Studio.
 * Title:PayController
 * Copyright:Copyright (c) 2015
 * Author:J-King
 * Date:2015-11-28
 * Time:15:50
 * Version 1.0
 * Description:
 */
public class PayController extends BaseController {

    public PayController(Context context, Handler handler) {
        super(context, handler);
    }

    /**
     * 支付宝支付
     *
     * @param bizOrderId
     * @param payChannel
     */
    public void getPayInfo(final Context context, long bizOrderId, String payChannel) {
        NetManager.getInstance(context).doGetPayInfoV2(bizOrderId, payChannel, new OnResponseListener<TmPayInfo>() {
            @Override
            public void onComplete(boolean isOK, TmPayInfo result, int errorCode, String errorMsg) {
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
     * 微信支付
     *

     */
    public void doGetWxBatchPayInfo(final Context context, long[] bizOrderId) {
        WxBatchPayParam mwxBatchPayParam =new WxBatchPayParam();
        mwxBatchPayParam.sourceType= PayStatus.SOURCE_TYPE;
        mwxBatchPayParam.payChannel= PayStatus.PAYTYPE_WX;
        mwxBatchPayParam.paySubChannel=PayStatus.SOURCE_TYPE;
        mwxBatchPayParam.bizOrderIdList=bizOrderId;


        NetManager.getInstance(context).doGetWxBatchPayInfo(mwxBatchPayParam, new OnResponseListener<WxPayInfo>() {
            @Override
            public void onComplete(boolean isOK, WxPayInfo result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_GET_PAY_WEIXIN_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_GET_PAY_WEIXIN_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GET_PAY_WEIXIN_KO, errorCode, 0, errorMessage);
            }
        });

    }

    /**
     * 微信支付成功状态服务器返回查询
     *
     * @param bizOrderId
     */
    public void getPayStatusInfo(final Context context, long bizOrderId) {
        NetManager.getInstance(context).doGetPayStatusInfo(bizOrderId, new OnResponseListener<TmPayStatusInfo>() {
            @Override
            public void onComplete(boolean isOK, TmPayStatusInfo result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_GET_PAY_WEIXINSTATUS_OK, result);
                    return;
                }
                sendMessage(ValueConstants.MSG_GET_PAY_WEIXINSTATUS_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_GET_PAY_WEIXINSTATUS_KO, errorCode, 0, errorMessage);
            }
        });
    }


    /**
     * 得到用户电子钱包的信息
     *
     * @param context
     */
    public void doGetEleAccountInfo(final Context context) {
        NetManager.getInstance(context).doGetEleAccountInfo(new OnResponseListener<EleAccountInfo>() {
            @Override
            public void onComplete(boolean isOK, EleAccountInfo result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.PAY_GETELEACCOUNTINFO_SUCCESS, result);
                    return;
                }
                sendMessage(ValueConstants.PAY_GETELEACCOUNTINFO_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.PAY_GETELEACCOUNTINFO_ERROR, errorCode, 0, errorMessage);
            }
        });
    }


    /**
     * 钱包支付
     *
     * @param elePursePayParam
     * @param context
     */
    public void doElePursePay(ElePursePayParam elePursePayParam, Context context) {
        NetManager.getInstance(context).doElePursePay(elePursePayParam, new OnResponseListener<PayCoreBaseResult>() {
            @Override
            public void onComplete(boolean isOK, PayCoreBaseResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new PayCoreBaseResult();
                    }
                    sendMessage(ValueConstants.PAY_ElePursePay_SUCCESS, result);
                    return;
                }
                sendMessage(ValueConstants.PAY_ElePursePay_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.PAY_ElePursePay_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取光大快捷支付的信息
     *
     * @param bizOrderId
     * @param returnUrl
     * @param sourceType
     * @param context
     */
    public void doGetCebCloudPayInfo(long bizOrderId, String returnUrl, String sourceType, final Context context) {
        CebCloudPayParam payParam = new CebCloudPayParam();
        payParam.bizOrderId = bizOrderId;
        payParam.returnUrl = returnUrl;
        payParam.sourceType = sourceType;
        NetManager.getInstance(context).doGetCebCloudPayInfo(payParam, new OnResponseListener<CebCloudPayInfo>() {
            @Override
            public void onComplete(boolean isOK, CebCloudPayInfo result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new CebCloudPayInfo();
                    }
                    sendMessage(ValueConstants.PAY_GetCebCloudPayInfo_SUCCESS, result);
                    return;
                }
                sendMessage(ValueConstants.PAY_GetCebCloudPayInfo_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.PAY_GetCebCloudPayInfo_ERROR, errorCode, 0, errorMessage);
            }
        });
    }


    /**
     * 获取订单支付状态
     *
     * @param bizOrderId
     * @param context
     */
    public void doGetPayStatusInfo(long bizOrderId, final Context context) {
        NetManager.getInstance(context).doGetPcPayStatusInfo(bizOrderId, new OnResponseListener<PcPayStatusInfo>() {
            @Override
            public void onComplete(boolean isOK, PcPayStatusInfo result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new PcPayStatusInfo();
                    }
                    sendMessage(ValueConstants.PAY_GetPayStatusInfo_SUCCESS, result);
                    return;
                }
                sendMessage(ValueConstants.PAY_GetPayStatusInfo_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.PAY_GetPayStatusInfo_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

}
