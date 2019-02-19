package com.newyhy.contract.presenter;


import android.content.Context;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.google.gson.Gson;
import com.newyhy.beans.CircleCoffeeVideoList;
import com.newyhy.contract.CircleCoffeeVideoContract;
import com.quanyan.yhy.R;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.yhy.common.utils.SPUtils;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

/**
 * CircleCoffeeVideoPresenter
 * Created by Jiervs on 2018/6/21.
 */

public class CircleCoffeeVideoPresenter implements CircleCoffeeVideoContract.Presenter {

    private Context context;

    private CircleCoffeeVideoContract.View view;

    @Autowired
    IUserService userService;

    public CircleCoffeeVideoPresenter(Context context, @NonNull CircleCoffeeVideoContract.View coffeeView) {
        this.context = context;
        this.view = coffeeView;
        coffeeView.setPresenter(this);
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
        NetManager.getInstance(context).doGetUserVideoList(userService.getLoginUserId(), pageIndex, pageSize, new OnResponseListener<String>() {
            @Override
            public void onComplete(boolean isOK, String result, int errorCode, String errorMsg) {
                CircleCoffeeVideoList resultList = new Gson().fromJson(result,CircleCoffeeVideoList.class);
                if (view != null) view.showData(resultList);
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                if (view != null) view.showErrorView(R.mipmap.default_page_video,context.getString(R.string.circle_no_data),"");
            }
        });
    }

    @Override
    public void getCacheData() {

    }
}
