package com.quanyan.yhy.ui.wallet.Controller;

import android.content.Context;
import android.os.Handler;

import com.quanyan.base.BaseController;
import com.quanyan.base.util.MD5Utils;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.yhy.common.beans.net.model.paycore.BankCard;
import com.yhy.common.beans.net.model.paycore.BankCardList;
import com.yhy.common.beans.net.model.paycore.BankNameList;
import com.yhy.common.beans.net.model.paycore.BaseResult;
import com.yhy.common.beans.net.model.paycore.BillList;
import com.yhy.common.beans.net.model.paycore.CebCloudPayInfo;
import com.yhy.common.beans.net.model.paycore.CebCloudPayParam;
import com.yhy.common.beans.net.model.paycore.EleAccountInfo;
import com.yhy.common.beans.net.model.paycore.PayCoreBaseResult;
import com.yhy.common.beans.net.model.paycore.PcPayResult;
import com.yhy.common.beans.net.model.paycore.RechargeParam;
import com.yhy.common.beans.net.model.paycore.RechargeResult;
import com.yhy.common.beans.net.model.paycore.SetupPayPwdParam;
import com.yhy.common.beans.net.model.paycore.SubmitIdCardPhotoParam;
import com.yhy.common.beans.net.model.paycore.SubmitIdCardPhotoResult;
import com.yhy.common.beans.net.model.paycore.UpdatePayPwdParam;
import com.yhy.common.beans.net.model.paycore.VerifyIdCardPhotoParam;
import com.yhy.common.beans.net.model.paycore.VerifyIdentityParam;
import com.yhy.common.beans.net.model.paycore.VerifyIdentityResult;
import com.yhy.common.beans.net.model.paycore.WithdrawParam;
import com.yhy.common.beans.net.model.tm.TmPayStatusInfo;
import com.yhy.common.constants.ValueConstants;

/**
 * Created with Android Studio.
 * Title:PayController
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-8-15
 * Time:11:05
 * Version 1.0
 * Description:
 */
public class WalletController extends BaseController {

    public WalletController(Context context, Handler handler) {
        super(context, handler);
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


    public void doSubmitIdCardPhoto(final     String frontPhotoName, final String reversePhotoName, final Context context) {


        SubmitIdCardPhotoParam mVerifyIdCardPhotoParam = new SubmitIdCardPhotoParam();
        mVerifyIdCardPhotoParam.frontPhotoName=frontPhotoName;
        mVerifyIdCardPhotoParam.reversePhotoName=reversePhotoName;

        NetManager.getInstance(context).doSubmitIdCardPhoto(mVerifyIdCardPhotoParam, new OnResponseListener<SubmitIdCardPhotoResult>() {
            @Override
            public void onComplete(boolean isOK, SubmitIdCardPhotoResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new SubmitIdCardPhotoResult();
                    }
                    sendMessage(ValueConstants.PAY_SUBMIT_IDCARDPHOTO_SUCCESS, result);
                    return;
                }
                sendMessage(ValueConstants.PAY_SUBMIT_IDCARDPHOTO_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.PAY_SUBMIT_IDCARDPHOTO_ERROR, errorCode, 0, errorMessage);
            }
        });

    }
    /**
     * 查询用户收支明细
     *
     * @param context
     */
    public void doVerifyIdCardPhoto(final     String photoName, final String photoType, final Context context) {


        VerifyIdCardPhotoParam mVerifyIdCardPhotoParam = new VerifyIdCardPhotoParam();
        mVerifyIdCardPhotoParam.photoName=photoName;
        mVerifyIdCardPhotoParam.photoType=photoType;
        NetManager.getInstance(context).doVerifyIdCardPhoto(mVerifyIdCardPhotoParam, new OnResponseListener<BaseResult>() {
            @Override
            public void onComplete(boolean isOK, BaseResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new BaseResult();
                    }
                    sendMessage(ValueConstants.PAY_VERIFY_IDCARDPHOTO_SUCCESS, result);
                    return;
                }
                sendMessage(ValueConstants.PAY_VERIFY_IDCARDPHOTO_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.PAY_VERIFY_IDCARDPHOTO_ERROR, errorCode, 0, errorMessage);

            }
        });

    }

    /**
     * 查询用户收支明细
     *
     * @param context
     */
    public void doGetPageQueryUserBill(final int pageNo, final int pageSize, final Context context) {


        NetManager.getInstance(context).doPageQueryUserBill(pageNo, pageSize, new OnResponseListener<BillList>() {
            @Override
            public void onComplete(boolean isOK, BillList result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new BillList();
                    }
                    sendMessage(ValueConstants.PAY_PAGEQUERYUBILL_SUCCESS, result);
                    return;
                }
                sendMessage(ValueConstants.PAY_PAGEQUERYUBILL_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.PAY_PAGEQUERYUBILL_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 根据卡号得到银行卡信息
     *
     * @param bankCardNo
     * @param context
     */
    public void doGetBankCardByCardNo(String bankCardNo, final Context context) {
        NetManager.getInstance(context).doGetBankCardByCardNo(bankCardNo, new OnResponseListener<BankCard>() {
            @Override
            public void onComplete(boolean isOK, BankCard result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.PAY_BankCardByCardNo_SUCCESS, result);
                    return;
                }
                sendMessage(ValueConstants.PAY_BankCardByCardNo_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.PAY_BankCardByCardNo_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 发送验证码
     *
     * @param verifyCodeType
     * @param mobilePhone
     * @param context
     */
    public void doSendVerifyCode(String verifyCodeType, String mobilePhone, final Context context, final int type) {
        NetManager.getInstance(context).doSendVerifyCode(verifyCodeType, mobilePhone, new OnResponseListener<PayCoreBaseResult>() {
            @Override
            public void onComplete(boolean isOK, PayCoreBaseResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new PayCoreBaseResult();
                    }
                    if (type == 1) {
                        sendMessage(ValueConstants.PAY_SendVerifyCode_SUCCESS, result);
                    } else {
                        sendMessage(ValueConstants.PAY_SendVerifyCode_TYPE_SUCCESS, result);
                    }
                    return;
                }
                sendMessage(ValueConstants.PAY_SendVerifyCode_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.PAY_SendVerifyCode_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 检验验证码
     *
     * @param verifyCodeType
     * @param verifyCode
     * @param verifyIdentityCode
     * @param context
     */
    public void doCheckVerifyCode(String verifyCodeType, String verifyCode, String verifyIdentityCode, final Context context) {
        NetManager.getInstance(context).doCheckVerifyCode(verifyCodeType, verifyCode, verifyIdentityCode, new OnResponseListener<PayCoreBaseResult>() {
            @Override
            public void onComplete(boolean isOK, PayCoreBaseResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new PayCoreBaseResult();
                    }
                    sendMessage(ValueConstants.PAY_CheckVerifyCode_SUCCESS, result);
                    return;
                }
                sendMessage(ValueConstants.PAY_CheckVerifyCode_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.PAY_CheckVerifyCode_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 设置支付密码
     *
     * @param verifyCode
     * @param verifyCodeType
     * @param verifyIdentityCode
     * @param payPwd
     * @param context
     */
    public void doSetPayPassWord(String verifyCode, String verifyCodeType, String verifyIdentityCode, String payPwd, Context context) {
        SetupPayPwdParam params = new SetupPayPwdParam();
        params.verifyCode = verifyCode;
        params.verifyCodeType = verifyCodeType;
        params.verifyIdentityCode = verifyIdentityCode;
        params.payPwd = MD5Utils.toMD5(payPwd);

        NetManager.getInstance(context).doSetupPayPwd(params, new OnResponseListener<PayCoreBaseResult>() {
            @Override
            public void onComplete(boolean isOK, PayCoreBaseResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new PayCoreBaseResult();
                    }
                    sendMessage(ValueConstants.PAY_SetupPayPwd_SUCCESS, result);
                    return;
                }
                sendMessage(ValueConstants.PAY_SetupPayPwd_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.PAY_SetupPayPwd_ERROR, errorCode, 0, errorMessage);
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
        NetManager.getInstance(context).doGetPayStatusInfo(bizOrderId, new OnResponseListener<TmPayStatusInfo>() {
            @Override
            public void onComplete(boolean isOK, TmPayStatusInfo result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new TmPayStatusInfo();
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

    /**
     * 查询用户绑定的银行卡列表
     *
     * @param pageNo
     * @param pageSize
     * @param context
     */
    public void doPageQueryUserBindBankCard(int pageNo, int pageSize, final Context context) {
        NetManager.getInstance(context).doPageQueryUserBindBankCard(pageNo, pageSize, new OnResponseListener<BankCardList>() {
            @Override
            public void onComplete(boolean isOK, BankCardList result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new BankCardList();
                    }
                    sendMessage(ValueConstants.PAY_PageQueryUserBindBankCard_SUCCESS, result);
                    return;
                }
                sendMessage(ValueConstants.PAY_PageQueryUserBindBankCard_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.PAY_PageQueryUserBindBankCard_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 充值
     *
     * @param rechargeAmount
     * @param returnUrl
     * @param sourceType
     * @param context
     */
    public void doRecharge(long rechargeAmount, String returnUrl, String sourceType, final Context context) {
        RechargeParam params = new RechargeParam();
        params.rechargeAmount = rechargeAmount;
        params.returnUrl = returnUrl;
        params.sourceType = sourceType;
        NetManager.getInstance(context).doRecharge(params, new OnResponseListener<RechargeResult>() {
            @Override
            public void onComplete(boolean isOK, RechargeResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new RechargeResult();
                    }
                    sendMessage(ValueConstants.PAY_Recharge_SUCCESS, result);
                    return;
                }
                sendMessage(ValueConstants.PAY_Recharge_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.PAY_Recharge_ERROR, errorCode, 0, errorMessage);
            }
        });
    }


    /**
     * 更新支付密码
     *
     * @param oldPayPwd
     * @param payPwd
     * @param context
     */
    public void doUpdatePayPwd(String oldPayPwd, String payPwd, final Context context) {
        UpdatePayPwdParam params = new UpdatePayPwdParam();
        params.oldPayPwd = MD5Utils.toMD5(oldPayPwd);
        params.payPwd = MD5Utils.toMD5(payPwd);
        NetManager.getInstance(context).doUpdatePayPwd(params, new OnResponseListener<PayCoreBaseResult>() {
            @Override
            public void onComplete(boolean isOK, PayCoreBaseResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new PayCoreBaseResult();
                    }
                    sendMessage(ValueConstants.PAY_UpdatePayPwd_SUCCESS, result);
                    return;
                }
                sendMessage(ValueConstants.PAY_UpdatePayPwd_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.PAY_UpdatePayPwd_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 验证身份
     *
     * @param params
     * @param context
     */
    public void doVerifyIdentity(VerifyIdentityParam params, final Context context) {
        NetManager.getInstance(context).doVerifyIdentity(params, new OnResponseListener<VerifyIdentityResult>() {
            @Override
            public void onComplete(boolean isOK, VerifyIdentityResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new VerifyIdentityResult();
                    }
                    sendMessage(ValueConstants.PAY_VerifyIdentity_SUCCESS, result);
                    return;
                }
                sendMessage(ValueConstants.PAY_VerifyIdentity_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.PAY_VerifyIdentity_ERROR, errorCode, 0, errorMessage);
            }
        });
    }


    /**
     * 验证支付密码
     *
     * @param payPwd
     * @param context
     */
    public void doVerifyPayPwd(String payPwd, final Context context) {
        NetManager.getInstance(context).doVerifyPayPwd(MD5Utils.toMD5(payPwd), new OnResponseListener<PayCoreBaseResult>() {
            @Override
            public void onComplete(boolean isOK, PayCoreBaseResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new PayCoreBaseResult();
                    }
                    sendMessage(ValueConstants.PAY_VerifyPayPwd_SUCCESS, result);
                    return;
                }
                sendMessage(ValueConstants.PAY_VerifyPayPwd_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.PAY_VerifyPayPwd_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 提现
     *
     * @param params
     * @param context
     */
    public void doWithdraw(WithdrawParam params, final Context context) {
        NetManager.getInstance(context).doWithdraw(params, new OnResponseListener<PayCoreBaseResult>() {
            @Override
            public void onComplete(boolean isOK, PayCoreBaseResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new PayCoreBaseResult();
                    }
                    sendMessage(ValueConstants.PAY_Withdraw_SUCCESS, result);
                    return;
                }
                sendMessage(ValueConstants.PAY_Withdraw_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.PAY_Withdraw_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 得到交易状态
     *
     * @param payOrderId
     * @param context
     */
    public void doQueryTransStatus(long payOrderId, final Context context) {
        NetManager.getInstance(context).doQueryTransStatus(payOrderId, new OnResponseListener<PcPayResult>() {
            @Override
            public void onComplete(boolean isOK, PcPayResult result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new PcPayResult();
                    }
                    sendMessage(ValueConstants.PAY_QueryTransStatus_SUCCESS, result);
                    return;
                }
                sendMessage(ValueConstants.PAY_QueryTransStatus_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.PAY_QueryTransStatus_ERROR, errorCode, 0, errorMessage);
            }
        });
    }


    /**
     * 获取银行名称列表
     *
     * @param context
     */
    public void doGetBankNameList(Context context) {
        NetManager.getInstance(context).doGetBankNameList(new OnResponseListener<BankNameList>() {
            @Override
            public void onComplete(boolean isOK, BankNameList result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result == null) {
                        result = new BankNameList();
                    }
                    sendMessage(ValueConstants.PAY_GetBankNameList_SUCCESS, result);
                    return;
                }
                sendMessage(ValueConstants.PAY_GetBankNameList_ERROR, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.PAY_GetBankNameList_ERROR, errorCode, 0, errorMessage);
            }
        });
    }

}
