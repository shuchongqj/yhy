package com.quanyan.yhy.net;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;

import com.quanyan.base.util.LocalUtils;
import com.yhy.common.utils.NetworkUtil;
import com.quanyan.base.util.StringUtil;

import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.net.lsn.OnAbstractListener;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.smart.sdk.api.request.ApiCode;
import com.smart.sdk.api.resp.Api_CreditNotification;
import com.smart.sdk.client.ApiContext;
import com.smart.sdk.client.BaseRequest;
import com.smart.sdk.client.LocalException;
import com.smart.sdk.client.ServerResponse;
import com.yhy.common.base.YHYBaseApplication;
import com.yhy.common.beans.net.model.CreditNotification;
import com.yhy.common.config.ContextHelper;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.AndroidUtils;
import com.yhy.common.utils.LogUtils;
import com.yhy.common.utils.SPUtils;
import com.yhy.network.api.user.UserApi;
import com.yhy.network.manager.AccountManager;
import com.yhy.network.manager.DeviceManager;
import com.yhy.network.utils.RequestHandlerKt;
import com.yhy.network.utils.TempLog;

import org.apache.http.HttpStatus;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created with Android Studio.
 * Title:BaseNetManager
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:和平必胜、正义必胜、人民必胜
 * Author:炎黄子孙
 * Date:15/11/11
 * Time:上午10:34
 * Version 1.0
 */
public class BaseNetManager {
    protected ApiContext mApiContext;
    protected Context mContext;
    protected Handler mHandler;

    protected String getDtk() {
        return AccountManager.Companion.getAccountManager().getDeviceToken();
    }

    protected boolean checkSubmitStatus(boolean isCheckDtk,
                                      final OnAbstractListener lsn) {
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (lsn != null) {
                        lsn.onInternError(
                                ErrorCode.NETWORK_UNAVAILABLE,
                                mContext.getString(R.string.network_unavailable));
                    }
                }
            });

            return false;
        }

        if (isCheckDtk && TextUtils.isEmpty(getDtk())) {
            mHandler.post(() -> {
                if (lsn != null) {
                    lsn.onInternError(
                            ErrorCode.DEVICE_TOKEN_MISSING,
                            mContext.getString(R.string.device_token_missing));
                }
            });

            return false;
        }

        return true;
    }

    protected boolean checkSubmitStatus(final OnAbstractListener lsn) {
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            mHandler.post(() -> {
                if (lsn != null) {
                    lsn.onInternError(
                            ErrorCode.NETWORK_UNAVAILABLE,
                            mContext.getString(R.string.network_unavailable));
                }
            });
            return false;
        }

        if (TextUtils.isEmpty(getDtk())) {
            mHandler.post(() -> {
                if (lsn != null) {
                    lsn.onInternError(
                            ErrorCode.DEVICE_TOKEN_MISSING,
                            mContext.getString(R.string.device_token_missing));
                }
            });
            return true;
        }

        return true;
    }

    int errorCode = -1;
    String errorMsg = null;

    /**
     * 统一处理异常
     * @param e
     * @param lsn
     */
    protected void handlerRequestException(Exception e,final OnAbstractListener lsn){
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            errorCode = ErrorCode.SDCARD_UNAVAILABLE;
            errorMsg = mContext.getString(R.string.sdcard_unavailable);
        } else {
            errorCode = ErrorCode.STATUS_NETWORK_EXCEPTION;
            errorMsg = mContext.getString(R.string.local_error_network_access_failed);
        }
        if(e instanceof LocalException){
            if(((LocalException)e).getCode() == HttpStatus.SC_GATEWAY_TIMEOUT ||
                    ((LocalException)e).getCode() == LocalException.SOCKET_TIMEOUT){
                errorCode = ErrorCode.CONNECTTION_TIME_OUT;
                errorMsg = mContext.getString(R.string.connection_time_out);
            }
        }

        mHandler.post(() -> {
            if (lsn != null) {
                lsn.onInternError(errorCode, errorMsg);
            }
        });
    }

    /**
     * 处理服务器返回异常
     * @param lsn
     */
    protected void handlerException(final int requestCode,final String errorMsg,final OnAbstractListener lsn){
        mHandler.post(() -> {
            if (lsn != null) {
                lsn.onInternError(requestCode, StringUtil.handlerErrorCode(mContext,requestCode));
            }
        });
    }

    /**
     *  zhangzx@yingheying.com
     *  Decouple the monolithic Network API Call to segments: prepare,execute,postExecute.
     */
    protected abstract class RequestExecutor {
        /**
         * Prepare for the request execution: such as check the network available etc.
         * @return  true: prepare successfully false: prepare failed.
         */
        protected abstract boolean prepare();

        /**
         * The post operation of the request execution, such as deserialize the response etc.
         * @param request  the request which wraps the response entity.
         */
        protected abstract void postExecute(final BaseRequest<?> request);

        /**
         * The exception handler in the API level(the response is exception).
         * @param request  The request which wraps the exception.
         */
        protected abstract void handleException(final BaseRequest<?> request);

        /**
         * The exception handler in the network level, such as the API server is not available.
         * @param exception The exception value.
         */
        protected abstract void handleRequestException(final Exception exception);

        /**
         * The request execution body.
         * @param request the request to execute.
         */
        public void execute(final BaseRequest<?> request) {
            if (prepare()) {
                Runnable requestThread = () -> {
                    try {
                        sendRequest(mContext, mApiContext, request);
                        if (request.getReturnCode() == ApiCode.SUCCESS) {
                            mHandler.post(() -> postExecute(request));
                        } else {
                            handleException(request);
                        }
                    } catch (final Exception e) {
                        handleRequestException(e);
                    }
                };
                NetThreadManager.getInstance().execute(requestThread);

            }
        }
    }

    /**
     * 登出后改变登录状态
     */
    public void updateApicontextAndHandler(ApiContext apiContext){
        mApiContext  = apiContext;
    }

    /**
     * 刷新状态
     */
    public void refreshEnvInfo(){
        getDtk();
    }


    public static ApiContext getApiContext(Context context) {
        ApiContext apiContext = new ApiContext(ContextHelper.getAppId(), AndroidUtils.getVersionCode(context), NetManager.mPublicKey);
        apiContext.setChannel(LocalUtils.getChannel(context));
        apiContext.setDomain(String.valueOf(ContextHelper.getDomainId()));
        return apiContext;
    }

    public static ApiContext getApiContext(Context context, String cerStr, String dtk) {
        ApiContext apiContext = new ApiContext(ContextHelper.getAppId(), AndroidUtils.getVersionCode(context), NetManager.mPublicKey);
        apiContext.setChannel(LocalUtils.getChannel(context));
        apiContext.setDomain(String.valueOf(ContextHelper.getDomainId()));
        if (dtk == null) {
            return apiContext;
        }
        apiContext.setCertificateWithDeviceToken(new ByteArrayInputStream(Base64.decode(cerStr.getBytes(), Base64.DEFAULT)), dtk);
        return apiContext;
    }

    private static void handlerCreditNotification(ServerResponse serverResponse) {
        if (serverResponse == null) {
            return;
        }

        //设置session
        if(!StringUtil.isEmpty(serverResponse.getSession())){
            DeviceManager.Companion.getDeviceManager().saveSession(serverResponse.getSession());
        }

        if (serverResponse.getCreditNotifications() != null) {
            for (Api_CreditNotification creditNotification : serverResponse.getCreditNotifications()) {
                final Intent intent = new Intent(ValueConstants.BROADCASTRECEIVER_ALL_TAST_COMPLETE);
                try {
                    intent.putExtra(SPUtils.EXTRA_DATA, CreditNotification.deserialize(creditNotification.serialize()));

                    long score = SPUtils.getScore(YHYBaseApplication.getInstance());
                    score = score + creditNotification.credit;
                    SPUtils.setScore(YHYBaseApplication.getInstance(), score);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case 0:
                                YHYBaseApplication.getInstance().sendBroadcast(intent);
                                break;
                        }
                    }
                }.sendEmptyMessageDelayed(0, 1 * 1000);
            }
        }
    }

    public static void sendHttpRequest(final Context ctx, final ApiContext context, final BaseRequest<?>[] requests) throws Exception {
        if (TextUtils.isEmpty(ContextHelper.getEnvUrl())) {
            ContextHelper.setEnvUrl(ContextHelper.DEFAULT_API_URL);
        }
        TempLog.Companion.log("request did =" + context.getDeviceId());
        final String cid = "" + (long) (Math.random() * 10000000000L);
        WebRequestUtil.fillResponse(ContextHelper.getEnvUrl(), context.getParameterString(requests), cid, true, new WebRequestUtil.ResponseFiller() {

            @Override
            public void fill(InputStream is) {
                ServerResponse serverResponse = context.fillResponse(requests, is);

                if (requests[0].getReturnCode() == ApiCode.SUCCESS) {
                    handlerCreditNotification(serverResponse);
                } else {
                    handlerLocalException(requests[0].getReturnCode(), cid, ctx, context, requests, serverResponse.getData());
                }
            }
        });
    }

    /**
     * Https的请求
     *
     * @param ctx
     * @param context
     * @param requests
     * @throws Exception
     */
    public static void sendHttpsRequest(final Context ctx,final ApiContext context, final BaseRequest<?>[] requests) throws Exception {
        if(TextUtils.isEmpty(ContextHelper.getHttpsApiUrl())){
            ContextHelper.setHttpsApiUrl(ContextHelper.DEFAULT_HTTPS_API_URL);
        }
        LogUtils.e("minrui","sendHttpsRequest="+ContextHelper.getHttpsApiUrl());
        LogUtils.e("minrui","parms="+context.getParameterString(requests));
        final String cid = "" + (long)(Math.random() * 10000000000L);
        TempLog.Companion.log("request did =" + context.getDeviceId());
        WebRequestUtil.fillHttpsResponse(ContextHelper.getHttpsApiUrl(), context.getParameterString(requests), cid, true, new WebRequestUtil.ResponseFiller() {


            @Override
            public void fill(InputStream is) {
                ServerResponse serverResponse = context.fillResponse(requests, is);

                if(requests[0].getReturnCode() == ApiCode.SUCCESS){
                    handlerCreditNotification(serverResponse);
                }else{
                    handlerLocalException(requests[0].getReturnCode(), cid, ctx, context, requests,serverResponse.getData());
                }
            }
        });
    }
    private static void handlerLocalException(int code, final String cid, final Context ctx, final ApiContext context, final BaseRequest<?>[] requests,
                                              String value) {
        for (BaseRequest request : requests){
            TempLog.Companion.log(request + "handlerLocalException=" + code);
        }
        if (code == ApiCode.TOKEN_EXPIRE) {
            //TODO user token expired
            NetManager.getInstance(ctx).doRefreshUserToken(new OnResponseListener<Boolean>() {
                @Override
                public void onComplete(boolean isOK, Boolean result, int errorCode, String errorMsg) {

                }

                @Override
                public void onInternError(int errorCode, String errorMessage) {

                }
            });
            //refresh user token
        } else if (code == ApiCode.NO_ACTIVE_DEVICE ||
                code == ApiCode.NO_TRUSTED_DEVICE ||
                code == ApiCode.RISK_USER_LOCKED
                ) {
            //TODO Others login in anthor device
//			ctx.sendBroadcast(new Intent(SPUtils.BC_ACTION_NO_ACTIVE_DEVICE));
            NavUtils.gotoKickOut(YHYBaseApplication.getInstance());
//			EventBus.getDefault().post(LoginEvent.LOGIN_OUT);
        } else if (code == ApiCode.USER_LOCKED) {
            if (value == null || (value.length() < 1)) {
                return;
            }
            long validTime = Long.parseLong(value);
            gotoBlackHouse(YHYBaseApplication.getInstance(), validTime);
        } else if (code == ApiCode.SIGNATURE_ERROR
                || code == ApiCode.TOKEN_ERROR) {
            //TODO 签名错误
            System.err.println("========签名错误");
//            SPUtils.clearLogStatus(YHYBaseApplication.getInstance());
//            NavUtils.gotoKickOut(YHYBaseApplication.getInstance());
            LocalUtils.JumpToLogin(YHYBaseApplication.getInstance());
        }
    }

    public static void gotoBlackHouse(Context context, long time) {

    }

    public static void sendRequest(Context ctx, final ApiContext context, final BaseRequest<?> request) throws Exception {
        if(!TextUtils.isEmpty(SPUtils.getExtraCurrentLat(ctx))) {
            context.setLatitude(SPUtils.getExtraCurrentLat(ctx));
        }
        if(!StringUtil.isEmpty(SPUtils.getExtraCurrentLon(ctx))) {
            context.setLongitude(SPUtils.getExtraCurrentLon(ctx));
        }
        String token = new UserApi().getToken();
        context.setToken((token == null || token.isEmpty() ? null : token));
        context.setUserId(new UserApi().getUserId());
        context.setSession(AccountManager.Companion.getAccountManager().getSession());
        if(ContextHelper.getHttpsApiUrl().contains("https")){
            sendHttpsRequest(ctx,context, new BaseRequest<?>[] { request });
        }else{
            sendHttpRequest(ctx,context, new BaseRequest<?>[] { request });
        }
    }

    public static void sendRequest(Context ctx, final ApiContext context, final BaseRequest<?>[] request) throws Exception {
        if(!TextUtils.isEmpty(SPUtils.getExtraCurrentLat(ctx))) {
            context.setLatitude(SPUtils.getExtraCurrentLat(ctx));
        }
        if(!TextUtils.isEmpty(SPUtils.getExtraCurrentLon(ctx))) {
            context.setLongitude(SPUtils.getExtraCurrentLon(ctx));
        }
        context.setToken(new UserApi().getToken());
        context.setUserId(new UserApi().getUserId());
        context.setSession(AccountManager.Companion.getAccountManager().getSession());
        if(ContextHelper.getHttpsApiUrl().contains("https")){
            sendHttpsRequest(ctx,context, request);
        }else{
            sendHttpRequest(ctx,context, request);
        }
    }

 }
