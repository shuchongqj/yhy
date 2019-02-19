package com.newyhy.contract;

import com.newyhy.contract.presenter.CircleSearchPresenter;
import com.yhy.common.base.BasePresenter;
import com.yhy.common.base.BaseView;
import com.yhy.network.resp.snscenter.SearchPageListResp;

/**
 * CircleSearchContract
 * Created by Jiervs on 2018/6/21.
 */

public interface CircleSearchContract {

    interface View extends BaseView<CircleSearchPresenter> {

        void showData(SearchPageListResp resp);

        void showErrorView(int resId, String tips, String advice);

        void showTopTip(String tip);
    }

    interface Presenter extends BasePresenter {

        void getNetData(String traceId,String title,int pageNo,int pageSize);

        void getCacheData();
    }
}
