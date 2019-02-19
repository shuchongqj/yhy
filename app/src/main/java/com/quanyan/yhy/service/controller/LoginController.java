package com.quanyan.yhy.service.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.mogujie.tt.imservice.event.LoginEvent;
import com.quanyan.base.BaseController;
import com.quanyan.pedometer.StepFragment2;
import com.quanyan.yhy.common.SmsType;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.smart.sdk.api.resp.Api_BoolResp;
import com.yhy.common.beans.net.model.pedometer.PedometerHistoryResultList;
import com.yhy.common.constants.ValueConstants;

import de.greenrobot.event.EventBus;

/**
 * Created by dengmingjia on 2015/11/3.
 * 登录相关控制器
 */
public class LoginController extends BaseController {
    /**
     * 登录成功
     */
    public final static int LOGIN_SCUESS = 1;
    /**
     * 登录失败
     */
    public final static int LOGIN_FAIL = 2;
    /** 获取验证码成功 */
    public final static int GET_VALIDATE_CODE_SCUESS = 3;
    /** 获取验证码失败 */
    public final static int GET_VALIDATE_CODE_FAIL = 4;
    /** 获取验证码计时 */
    public final static int VALIDATE_TIME = 5;
    /** 重置密码成功 */
    public final static int GET_RESET_PASSWORD_SUCESS = 6;
    /** 重置密码失败 */
    public final static int GET_RESET_PASSWORD_FAIL = 7;
    /** 注册成功 */
    public static int GET_REGISTER_SUCESS = 8;
    /** 注册失败 */
    public static int GET_REGISTER_FAIL = 9;
    /** 修改密码成功 */
    public static int GET_CHANGE_PASSWORD_SUCESS = 10;
    /** 修改密码失败 */
    public static int GET_CHANGE_PASSWORD_FAIL = 11;
    public static final int REQUEST_CODE_REGISTER = 1;

    public LoginController(Context context, Handler handler) {
        super(context, handler);
    }

    /**
     * 获取历史步数
     * @param context
     */
    public void doGetStepHistory(Context context){
            NetManager.getInstance(context).doGetHistoryPedometerInfo(new OnResponseListener<PedometerHistoryResultList>() {
                @Override
                public void onComplete(boolean isOK, PedometerHistoryResultList result, int errorCode, String errorMsg) {
                    if (isOK && result != null) {
                        sendMessage(StepFragment2.MSG_GET_SERVER_DATA_OK, result);
                    }
                }

                @Override
                public void onInternError(int errorCode, String errorMessage) {
                }
            });
    }

    /**
     * 进行登录请求
     *
     * @param phoneNumber 手机号
     * @param password    密码
     */
    public void doLogin(final Context context, String phoneNumber, String password) {

        NetManager.getInstance(context).doLoginByPassword(phoneNumber, password, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(LOGIN_SCUESS, result);
                    EventBus.getDefault().post(LoginEvent.LOGIN_UT_OK);
                    doGetStepHistory(context);
                    return;
                }
                sendMessage(LOGIN_FAIL, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(LOGIN_FAIL, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 动态验证码登录
     * @param context
     * @param phoneNumber
     * @param code
     */
    public void doLoginByDynamicCode(final Context context, String phoneNumber, String code) {
        NetManager.getInstance(context).doLoginByDynamicCode(phoneNumber, code, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(LOGIN_SCUESS, result);
                    EventBus.getDefault().post(LoginEvent.LOGIN_UT_OK);
                    doGetStepHistory(context);
                    return;
                }
                sendMessage(LOGIN_FAIL, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(LOGIN_FAIL, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取验证码
     *
     * @param phoneNumber 手机号
     */
    public void doGetValidateCode(final Context context, String phoneNumber,String smsType) {
        NetManager.getInstance(context).doRequestDynamic(phoneNumber, smsType,new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(GET_VALIDATE_CODE_SCUESS, result);
                    return;
                }
                sendMessage(GET_VALIDATE_CODE_FAIL, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(GET_VALIDATE_CODE_FAIL, errorCode, 0, errorMessage);
            }
        });
    }


    /**
     * 登录状态获取验证码
     *
     */
    public void doGetValidateCodeByLogin(final Context context, String phone, String smsType) {
        NetManager.getInstance(context).doRequestDynamicByLogin(smsType, phone, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(GET_VALIDATE_CODE_SCUESS, result);
                    return;
                }
                sendMessage(GET_VALIDATE_CODE_FAIL, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(GET_VALIDATE_CODE_FAIL, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 获取验证码计时
     *
     * @param time 单位秒
     */
    public void sendValidateCodeTime(int time) {
        Message message = mUiHandler.obtainMessage(VALIDATE_TIME, time, 0);
        mUiHandler.sendMessageDelayed(message, 1000);
    }

    /**
     * 重置密码
     * @param phoneNumber 手机号
     * @param validateCode 验证码
     * @param password 密码
     */
    public void doResetPassword(final Context context, String phoneNumber, String validateCode, String password) {
        NetManager.getInstance(context).doResetPassword(phoneNumber, validateCode, password, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(GET_RESET_PASSWORD_SUCESS, result);
                    return;
                }
                sendMessage(GET_RESET_PASSWORD_FAIL, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(GET_RESET_PASSWORD_FAIL, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 注册
     * @param phoneNumber 手机号
     * @param validateCode 验证码
     * @param password 密码
     */
    public void doRegister(final Context context, String phoneNumber, String validateCode, String password,String nickName) {
        NetManager.getInstance(context).doUserReisterNew(phoneNumber, validateCode, password, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(GET_REGISTER_SUCESS, result);
                    EventBus.getDefault().post(LoginEvent.LOGIN_UT_OK);
                    return;
                }
                sendMessage(GET_REGISTER_FAIL, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(GET_REGISTER_FAIL, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 修改密码接口
     * @param oldPassword 老密码
     * @param newPassword 新密码
     */
    public void doChangePassword(final Context context, String oldPassword, String newPassword) {
        NetManager.getInstance(context).doChangePassword(oldPassword, newPassword, new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(GET_CHANGE_PASSWORD_SUCESS, result);
                    return;
                }
                sendMessage(GET_CHANGE_PASSWORD_FAIL, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(GET_CHANGE_PASSWORD_FAIL, errorCode, 0, errorMessage);
            }
        });
    }

    /**
     * 绑定手机号码
     * @param phone
     * @param code
     */
    public void doBindPhone(final Context context, String phone,String code,String unionId,String openId,String outType,String accessToken) {
        NetManager.getInstance(context).doAppThirdPartyBind(phone, code,unionId,openId,outType,accessToken,new OnResponseListener<Boolean>() {
            @Override
            public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {
                if (isOK) {
                    sendMessage(ValueConstants.MSG_BIND_MOBILE_OK, result);
                    EventBus.getDefault().post(LoginEvent.LOGIN_UT_OK);
                    return;
                }
                sendMessage(ValueConstants.MSG_BIND_MOBILE_KO, errorCode, 0, errorMsg);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                sendMessage(ValueConstants.MSG_BIND_MOBILE_KO, errorCode, 0, errorMessage);
            }
        });
    }

}
