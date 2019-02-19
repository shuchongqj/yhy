package com.yhy.network.api.outplace;

import com.yhy.network.YhyCallback;
import com.yhy.network.YhyCaller;
import com.yhy.network.api.Api;
import com.yhy.network.api.ApiImp;
import com.yhy.network.req.outplace.GetOutPlaceCityListReq;
import com.yhy.network.resp.Response;
import com.yhy.network.resp.outplace.GetOutPlaceCityListResp;

/**
 * Created by yangboxue on 2018/7/19.
 */

public class OutPlaceApi {
    private Api api = new ApiImp();

    public YhyCaller<Response<GetOutPlaceCityListResp>, GetOutPlaceCityListResp> getOutPlaceCityList(GetOutPlaceCityListReq req, YhyCallback<Response<GetOutPlaceCityListResp>> callback){
        return api.makeRequest(req, callback, GetOutPlaceCityListResp.class);
    }
}
