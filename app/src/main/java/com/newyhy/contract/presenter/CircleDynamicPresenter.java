package com.newyhy.contract.presenter;


import android.content.Context;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.newyhy.contract.CircleDynamicContract;
import com.quanyan.yhy.R;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.yhy.common.beans.net.model.discover.TopicInfoResultList;
import com.yhy.common.beans.net.model.discover.UgcInfoResultList;
import com.yhy.common.beans.topic.Topic;
import com.yhy.network.YhyCallback;
import com.yhy.network.YhyCode;
import com.yhy.network.api.snscenter.SnsCenterApi;
import com.yhy.router.YhyRouter;
import com.yhy.service.ITopicService;

import java.util.List;

/**
 * CircleDynamicPresenter
 * Created by Jiervs on 2018/6/21.
 */

public class CircleDynamicPresenter implements CircleDynamicContract.Presenter {

    private Context context;

    private CircleDynamicContract.View view;

    @Autowired
    ITopicService topicService;

    public CircleDynamicPresenter(Context context, @NonNull CircleDynamicContract.View dynamicView) {
        this.context = context;
        this.view = dynamicView;
        dynamicView.setPresenter(this);
        YhyRouter.getInstance().inject(this);
    }

    @Override
    public void process() {

    }

    @Override
    public void release() {
        context = null;
        view = null;
    }

    @Override
    public void getNetData(int pageIndex,int pageSize) {

        if (context != null)
        NetManager.getInstance(context).doGetAreUgcPageList(pageIndex, pageSize, new OnResponseListener<UgcInfoResultList>() {
            @Override
            public void onComplete(boolean isOK, UgcInfoResultList result, int errorCode, String errorMsg) {
                if (isOK && result != null && result.ugcInfoList != null && result.ugcInfoList.size() > 0) {
                    if (view != null) view.showData(result);
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                if (view != null) view.showErrorView(R.mipmap.default_dynamic,context.getString(R.string.circle_no_data),"");
            }
        });

        //获取话题
        if (pageIndex == 1 && context != null) {
            topicService.getTopics(1, 4, new YhyCallback<List<Topic>>() {
                @Override
                public void onSuccess(List<Topic> data) {
                    if (view != null) view.showTopic(data);
                }

                @Override
                public void onFail(YhyCode code, Exception exception) {

                }
            });
        }
    }

    @Override
    public void getCacheData() {

    }
}
