package com.newyhy.contract;

import com.newyhy.contract.presenter.CircleRecommendPresenter;
import com.yhy.common.base.BasePresenter;
import com.yhy.common.base.BaseView;
import com.yhy.network.req.snscenter.GetRecommendPageListReq;
import com.yhy.network.resp.snscenter.GetRecommendPageListResp;


/**
 * CircleRecommendContract
 * Created by Jiervs on 2018/6/21.
 */

public interface CircleRecommendContract {

    interface View extends BaseView<CircleRecommendPresenter> {

        void showData(GetRecommendPageListResp resp);

        void showErrorView(int resId , String tips, String advice);

        void showTopTip(String tip);
    }

    interface Presenter extends BasePresenter {

        void getNetData(GetRecommendPageListReq.Companion.P p);

        void getCacheData();
    }
}
