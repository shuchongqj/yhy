package com.yhy.network.api.resourcecenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.yhy.common.base.YHYBaseApplication;
import com.yhy.network.YhyCallback;
import com.yhy.network.YhyCaller;
import com.yhy.network.YhyCode;
import com.yhy.network.api.Api;
import com.yhy.network.api.ApiImp;
import com.yhy.network.req.msgcenter.SaveMsgRelevanceReq;
import com.yhy.network.req.resourcecenter.GetMainSquareUserInfoReq;
import com.yhy.network.req.user.GetUserInfoByUserIdReq;
import com.yhy.network.req.user.LoginReq;
import com.yhy.network.req.user.LogoutReq;
import com.yhy.network.resp.Response;
import com.yhy.network.resp.msgcenter.SaveMsgRelevanceResp;
import com.yhy.network.resp.resourcecenter.GetMainSquareUserInfoResp;
import com.yhy.network.resp.user.GetUserInfoByUserIdResp;
import com.yhy.network.resp.user.LoginResp;
import com.yhy.network.resp.user.LogoutResp;
import com.yhy.network.utils.RequestHandlerKt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResourceCenterApi {
    private Api api = new ApiImp();

    public YhyCaller<Response<GetMainSquareUserInfoResp>, GetMainSquareUserInfoResp> getMainSquareUserInfo(GetMainSquareUserInfoReq req, YhyCallback<Response<GetMainSquareUserInfoResp>> callback){
        return api.makeRequest(req, callback, GetMainSquareUserInfoResp.class);
    }
}
