package com.yhy.topic;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.yhy.common.beans.topic.Topic;
import com.yhy.network.YhyCallback;
import com.yhy.network.YhyCode;
import com.yhy.network.api.snscenter.SnsCenterApi;
import com.yhy.network.req.PageReq;
import com.yhy.network.req.snscenter.GetTagInfoListByTypeReq;
import com.yhy.network.req.snscenter.GetTopicPageListReq;
import com.yhy.network.resp.Response;
import com.yhy.network.resp.snscenter.GetTagInfoListByTypeResp;
import com.yhy.network.resp.snscenter.GetTopicPageListResp;
import com.yhy.service.ITopicService;

import java.util.ArrayList;
import java.util.List;

import static com.yhy.router.RouterPath.SERVICE_TOPIC;

@Route(path = SERVICE_TOPIC, name = "topicService")
public class TopicService implements ITopicService{

    public void getTopics(int page, int pageCount, YhyCallback<List<Topic>> callback){

        new SnsCenterApi().getTopicPageList(new GetTopicPageListReq(new GetTopicPageListReq.Companion.P(new PageReq(page, pageCount), 1, 0)), new YhyCallback<Response<GetTopicPageListResp>>() {
            @Override
            public void onSuccess(Response<GetTopicPageListResp> data) {
                if (callback != null){
                    List<GetTopicPageListResp.Companion.TopicInfoResult> tagResults = data.getContent().getTopicInfoResultList();
                    List<Topic> topics = new ArrayList<>();
                    for (GetTopicPageListResp.Companion.TopicInfoResult result : tagResults){
                        Topic topic = new Topic();

                        topic.setName(result.getTitle());
                        topic.setId(result.getTopicId());
                        topic.setPic(result.getPics());
                        topic.setDescription(result.getContent());
                        topic.setMemberCount(result.getTalkNum());
                        topic.setReadCount(result.getRedNum());
                        topics.add(topic);
                    }
                    callback.onSuccess(topics);
                }
            }

            @Override
            public void onFail(YhyCode code, Exception exception) {
                if (callback != null){
                    callback.onFail(code, exception);
                }
            }
        }).execAsync();
    }

    @Override
    public void init(Context context) {

    }
}
