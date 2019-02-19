package com.yhy.network.api.snscenter;

import com.yhy.network.YhyCallback;
import com.yhy.network.YhyCaller;
import com.yhy.network.api.Api;
import com.yhy.network.api.ApiImp;
import com.yhy.network.req.snscenter.DislikeReq;
import com.yhy.network.req.snscenter.DislikeVideoReq;
import com.yhy.network.req.snscenter.GetRecommendPageListReq;
import com.yhy.network.req.snscenter.GetShortVideoDetail;
import com.yhy.network.req.snscenter.GetTagInfoListByTypeReq;
import com.yhy.network.req.snscenter.GetTopicPageListReq;
import com.yhy.network.req.snscenter.GetUserReplayReq;
import com.yhy.network.req.snscenter.GetUserSuperbListReq;
import com.yhy.network.req.snscenter.QueryGuidanceRecordReq;
import com.yhy.network.req.snscenter.SaveUserCorrelationReq;
import com.yhy.network.req.snscenter.SearchPageListReq;
import com.yhy.network.resp.Response;
import com.yhy.network.resp.snscenter.DislikeResp;
import com.yhy.network.resp.snscenter.DislikeVideoResp;
import com.yhy.network.resp.snscenter.GetRecommendPageListResp;
import com.yhy.network.resp.snscenter.GetReplayByUserIdResp;
import com.yhy.network.resp.snscenter.GetShortVideoDetailResp;
import com.yhy.network.resp.snscenter.GetTagInfoListByTypeResp;
import com.yhy.network.resp.snscenter.GetTopicPageListResp;
import com.yhy.network.resp.snscenter.GetUserSuperbListResp;
import com.yhy.network.resp.snscenter.QueryGuidanceRecordResp;
import com.yhy.network.resp.snscenter.SaveUserCorrelationResp;
import com.yhy.network.resp.snscenter.SearchPageListResp;

public class SnsCenterApi {

    private Api api = new ApiImp();

    public YhyCaller<Response<GetTagInfoListByTypeResp>, GetTagInfoListByTypeResp> getTagInfoListByType(GetTagInfoListByTypeReq req, YhyCallback<Response<GetTagInfoListByTypeResp>> callback){
        return api.makeRequest(req, callback, GetTagInfoListByTypeResp.class);
    }

    public YhyCaller<Response<QueryGuidanceRecordResp>, QueryGuidanceRecordResp> queryGuidanceRecord(QueryGuidanceRecordReq req, YhyCallback<Response<QueryGuidanceRecordResp>> callback){
        return api.makeRequest(req, callback, QueryGuidanceRecordResp.class);
    }

    public YhyCaller<Response<SaveUserCorrelationResp>, SaveUserCorrelationResp> saveUserCorrlation(SaveUserCorrelationReq req, YhyCallback<Response<SaveUserCorrelationResp>> callback){
        return api.makeRequest(req, callback, SaveUserCorrelationResp.class);
    }


    public YhyCaller<Response<DislikeVideoResp>, DislikeVideoResp> dislikeVideo(DislikeVideoReq req, YhyCallback<Response<DislikeVideoResp>> callback){
        return api.makeRequest(req, callback, DislikeVideoResp.class);
    }

    public YhyCaller<Response<DislikeResp>, DislikeResp> dislike(DislikeReq req, YhyCallback<Response<DislikeResp>> callback){
        return api.makeRequest(req, callback, DislikeResp.class);
    }

    public YhyCaller<Response<GetRecommendPageListResp>, GetRecommendPageListResp> getRecommendPageList(GetRecommendPageListReq req, YhyCallback<Response<GetRecommendPageListResp>> callback){
        return api.makeRequest(req, callback, GetRecommendPageListResp.class);
    }

    public YhyCaller<Response<SearchPageListResp>, SearchPageListResp> searchPageList(SearchPageListReq req, YhyCallback<Response<SearchPageListResp>> callback){
        return api.makeRequest(req, callback, SearchPageListResp.class);
    }

    public YhyCaller<Response<GetUserSuperbListResp>, GetUserSuperbListResp> getUserSuperbList(GetUserSuperbListReq req, YhyCallback<Response<GetUserSuperbListResp>> callback){
        return api.makeRequest(req, callback, GetUserSuperbListResp.class);
    }

    public YhyCaller<Response<GetReplayByUserIdResp>, GetReplayByUserIdResp> getUserReplayList(GetUserReplayReq req, YhyCallback<Response<GetReplayByUserIdResp>> callback){
        return api.makeRequest(req, callback, GetReplayByUserIdResp.class);
    }

    public YhyCaller<Response<GetTopicPageListResp>, GetTopicPageListResp> getTopicPageList(GetTopicPageListReq req, YhyCallback<Response<GetTopicPageListResp>> callback){
        return api.makeRequest(req, callback, GetTopicPageListResp.class);
    }

    public YhyCaller<Response<GetShortVideoDetailResp>, GetShortVideoDetailResp> getShortVideoDetail(GetShortVideoDetail req, YhyCallback<Response<GetShortVideoDetailResp>> callback){
        return api.makeRequest(req, callback, GetShortVideoDetailResp.class);
    }
}
