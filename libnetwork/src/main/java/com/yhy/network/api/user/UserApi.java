package com.yhy.network.api.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.yhy.common.base.YHYBaseApplication;
import com.yhy.network.YhyCallback;
import com.yhy.network.YhyCaller;
import com.yhy.network.YhyCode;
import com.yhy.network.api.Api;
import com.yhy.network.api.ApiImp;
import com.yhy.network.manager.AccountManager;
import com.yhy.network.req.msgcenter.SaveMsgRelevanceReq;
import com.yhy.network.req.user.GetUserInfoByUserIdReq;
import com.yhy.network.req.user.LoginReq;
import com.yhy.network.req.user.LogoutReq;
import com.yhy.network.resp.Response;
import com.yhy.network.resp.msgcenter.SaveMsgRelevanceResp;
import com.yhy.network.resp.user.GetUserInfoByUserIdResp;
import com.yhy.network.resp.user.LoginResp;
import com.yhy.network.resp.user.LogoutResp;
import com.yhy.network.utils.RequestHandlerKt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UserApi {
    private Api api = new ApiImp();


    public YhyCaller<Response<LoginResp>, LoginResp> login(LoginReq req, YhyCallback<Response<LoginResp>> callback){
        return api.makeRequest(req, new LoginCallbackWrapper(callback), LoginResp.class);
    }

    public YhyCaller<Response<SaveMsgRelevanceResp>, SaveMsgRelevanceResp> bindUserAndPush(SaveMsgRelevanceReq req, YhyCallback<Response<SaveMsgRelevanceResp>> callback){
        return api.makeRequest(req, callback, SaveMsgRelevanceResp.class);
    }

    public YhyCaller<Response<GetUserInfoByUserIdResp>, GetUserInfoByUserIdResp> getUserInfo(GetUserInfoByUserIdReq req, YhyCallback<Response<GetUserInfoByUserIdResp>> callback){
        return api.makeRequest(req, callback, GetUserInfoByUserIdResp.class);
    }



    public YhyCaller<Response<LogoutResp>, LogoutResp> logout(){
        //清除登陆状态
        return api.makeRequest(new LogoutReq(), null, LogoutResp.class);
    }

    public boolean isLogin(){
        return getUserId() > 0;
    }

    public String getToken(){
        return AccountManager.Companion.getAccountManager().getUserToken();
    }

    public String getPhone(){
        return AccountManager.Companion.getAccountManager().getPhoneNumber();
    }

    public Long getUserId(){
        return AccountManager.Companion.getAccountManager().getUserId();
    }

    @SuppressLint("ApplySharedPref")
    public void saveLoginToken(String token) {
        AccountManager.Companion.getAccountManager().saveLoginToken(token);
    }

    @SuppressLint("ApplySharedPref")
    public void savePhone(String phone) {
        AccountManager.Companion.getAccountManager().savePhone(phone);
    }

    @SuppressLint("ApplySharedPref")
    public void savePhoneCode(String code) {
        AccountManager.Companion.getAccountManager().savePhoneCode(code);
    }

    @SuppressLint("ApplySharedPref")
    public void saveUserId(Long userId) {
        AccountManager.Companion.getAccountManager().saveUserId(userId);
    }

    @SuppressLint("ApplySharedPref")
    public void saveWebToken(String webToken) {
        AccountManager.Companion.getAccountManager().saveWebToken(webToken);
    }

    @SuppressLint("ApplySharedPref")
    public void saveDtk(String dtk, String certStr) {
        if (dtk == null || dtk.isEmpty()){
            return;
        }
        if (certStr == null || certStr.isEmpty()){
            return;
        }
        AccountManager.Companion.getAccountManager().saveDtk(dtk);
        AccountManager.Companion.getAccountManager().saveCertString(certStr);
        AccountManager.Companion.getAccountManager().refreshCert();
        AccountManager.Companion.getAccountManager().initCert(certStr);
    }

    private class LoginCallbackWrapper implements YhyCallback<Response<LoginResp>>{

        private YhyCallback<Response<LoginResp>> callback;

        LoginCallbackWrapper(YhyCallback<Response<LoginResp>> callback) {
            this.callback = callback;
        }

        @Override
        public void onSuccess(Response<LoginResp> data) {
            AccountManager.Companion.getAccountManager().saveLoginToken(data.getContent().getToken());
            AccountManager.Companion.getAccountManager().savePhone(data.getContent().getMobile());
            AccountManager.Companion.getAccountManager().saveUserId(data.getContent().getUid());

            if (this.callback != null){
                this.callback.onSuccess(data);
            }
        }

        @Override
        public void onFail(@NotNull YhyCode code, @Nullable Exception exception) {
            if (this.callback != null){
                this.callback.onFail(code, exception);
            }
        }
    }
}
