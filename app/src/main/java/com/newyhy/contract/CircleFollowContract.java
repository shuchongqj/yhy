package com.newyhy.contract;

import android.support.annotation.Nullable;
import android.view.View;

import com.newyhy.contract.presenter.CircleFollowPresenter;
import com.yhy.common.base.BasePresenter;
import com.yhy.common.base.BaseView;
import com.yhy.common.beans.net.model.discover.UgcInfoResultList;

/**
 * CircleFollowContract
 * Created by Jiervs on 2018/6/21.
 */

public interface CircleFollowContract {

    interface View extends BaseView<CircleFollowPresenter> {

        void showData(UgcInfoResultList result);

        void showErrorView(int resId,String tips, String advice, @Nullable android.view.View.OnClickListener onClickListener);
    }

    interface Presenter extends BasePresenter {

        void getNetData(int pageIndex,int pageSize);

        void getCacheData();
    }
}
