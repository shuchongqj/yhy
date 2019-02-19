package com.yhy.network.api.log;

import com.yhy.network.YhyCallback;
import com.yhy.network.YhyCaller;
import com.yhy.network.api.Api;
import com.yhy.network.api.ApiImp;
import com.yhy.network.req.log.LogUploadReq;
import com.yhy.network.resp.Response;
import com.yhy.network.resp.log.LogUploadResp;

public class LogApi {

    private Api api = new ApiImp();

    public YhyCaller<Response<LogUploadResp>, LogUploadResp> uploadLog(LogUploadReq req, YhyCallback<Response<LogUploadResp>> callback){
        return api.makeLogRequest(req, callback, LogUploadResp.class);
    }

}
