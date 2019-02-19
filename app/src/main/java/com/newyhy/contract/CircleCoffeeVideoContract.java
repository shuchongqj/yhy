package com.newyhy.contract;

import com.newyhy.beans.CircleCoffeeVideoList;
import com.newyhy.contract.presenter.CircleCoffeeVideoPresenter;
import com.yhy.common.base.BasePresenter;
import com.yhy.common.base.BaseView;
import com.yhy.common.beans.net.model.discover.UgcInfoResultList;

/**
 * CircleCoffeeVideoContract
 * Created by Jiervs on 2018/6/21.
 */

public interface CircleCoffeeVideoContract {

    interface View extends BaseView<CircleCoffeeVideoPresenter> {

        void showData(CircleCoffeeVideoList result);

        void showErrorView(int resId,String tips, String advice);
    }

    interface Presenter extends BasePresenter {

        void getNetData(int pageIndex, int pageSize);

        void getCacheData();
    }
}
