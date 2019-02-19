package com.yhy.service;


import com.alibaba.android.arouter.facade.template.IProvider;
import com.yhy.common.beans.topic.Topic;
import com.yhy.common.beans.user.User;
import com.yhy.network.YhyCallback;
import com.yhy.network.resp.snscenter.GetTagInfoListByTypeResp;

import java.util.List;

/**
 * 用户相关的service
 *
 */
public interface ITopicService extends IProvider{


    void getTopics(int page, int pageCount, YhyCallback<List<Topic>> callback);

}
