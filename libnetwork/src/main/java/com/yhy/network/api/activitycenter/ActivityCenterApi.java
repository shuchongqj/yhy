package com.yhy.network.api.activitycenter;

import com.yhy.network.YhyCallback;
import com.yhy.network.YhyCaller;
import com.yhy.network.api.Api;
import com.yhy.network.api.ApiImp;
import com.yhy.network.req.activitycenter.PlayReq;
import com.yhy.network.req.activitycenter.ShareReq;
import com.yhy.network.resp.Response;
import com.yhy.network.resp.activitycenter.PlayResp;
import com.yhy.network.resp.activitycenter.ShareResp;

/**
 * Created by yangboxue on 2018/7/19.
 */

public class ActivityCenterApi {
    private Api api = new ApiImp();

    public YhyCaller<Response<PlayResp>, PlayResp> play(PlayReq req, YhyCallback<Response<PlayResp>> callback){
        return api.makeRequest(req, callback, PlayResp.class);
    }

    public YhyCaller<Response<ShareResp>, ShareResp> share(ShareReq req, YhyCallback<Response<ShareResp>> callback){
        return api.makeRequest(req, callback, ShareResp.class);
    }
}
