package com.newyhy.contract;

import com.newyhy.contract.presenter.CircleDynamicPresenter;
import com.yhy.common.base.BasePresenter;
import com.yhy.common.base.BaseView;
import com.yhy.common.beans.net.model.discover.TopicInfoResultList;
import com.yhy.common.beans.net.model.discover.UgcInfoResultList;
import com.yhy.common.beans.topic.Topic;

import java.util.List;

/**
 * CircleDynamicContract
 * Created by Jiervs on 2018/6/21.
 */

public interface CircleDynamicContract {

    interface View extends BaseView<CircleDynamicPresenter> {

        void showData(UgcInfoResultList result);

        void showTopic(List<Topic> result);

        void showErrorView(int resId , String tips, String advice);
    }

    interface Presenter extends BasePresenter {

        void getNetData(int pageIndex, int pageSize);

        void getCacheData();
    }
}
