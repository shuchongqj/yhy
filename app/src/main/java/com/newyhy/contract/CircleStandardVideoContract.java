package com.newyhy.contract;

import com.newyhy.beans.CircleLiveList;
import com.newyhy.contract.presenter.CircleStandardVideoPresenter;
import com.yhy.common.base.BasePresenter;
import com.yhy.common.base.BaseView;

/**
 * CircleStandardVideoContract
 * Created by Jiervs on 2018/6/21.
 */

public interface CircleStandardVideoContract {

    interface View extends BaseView<CircleStandardVideoPresenter> {

        void showData(CircleLiveList result);

        void showErrorView(int resId , String tips, String advice);
    }

    interface Presenter extends BasePresenter {

        void getNetData(int pageIndex, int pageSize);

        void getCacheData();
    }
}
