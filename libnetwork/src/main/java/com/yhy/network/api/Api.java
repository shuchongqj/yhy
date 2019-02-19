package com.yhy.network.api;

import android.support.annotation.Nullable;

import com.yhy.network.YhyCallback;
import com.yhy.network.YhyCaller;
import com.yhy.network.req.BaseLogRequest;
import com.yhy.network.req.BaseRequest;
import com.yhy.network.req.device.RegisterReq;
import com.yhy.network.req.log.LogUploadReq;
import com.yhy.network.resp.Response;
import com.yhy.network.resp.device.RegisterResp;


public interface Api {

    <T> YhyCaller<Response<T>, T> makeRequest(BaseRequest request, @Nullable YhyCallback<Response<T>> callback, Class<T> respClass);
    <T> YhyCaller<Response<T>, T> makeLogRequest(LogUploadReq request, @Nullable YhyCallback<Response<T>> callback, Class<T> respClass);

    YhyCaller<Response<RegisterResp>, RegisterResp> register(RegisterReq req, @Nullable YhyCallback<Response<RegisterResp>> callback);
}
