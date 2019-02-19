package com.newyhy.contract.presenter;


import android.content.Context;
import android.support.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.google.gson.Gson;
import com.newyhy.beans.CircleLiveList;
import com.newyhy.contract.CircleLiveContract;
import com.quanyan.yhy.R;
import com.quanyan.yhy.database.DBManager;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.yhy.common.beans.net.model.user.UserInfo;
import com.yhy.common.beans.user.User;
import com.yhy.common.utils.SPUtils;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;

/**
 * CircleLivePresenter
 * Created by Jiervs on 2018/6/21.
 */

public class CircleLivePresenter implements CircleLiveContract.Presenter {

    private Context context;

    private CircleLiveContract.View view;

    private boolean isFromCourt;//是否是主场
    @Autowired
    IUserService userService;

    public CircleLivePresenter(Context context, @NonNull CircleLiveContract.View liveView, boolean isFromCourt) {
        this.context = context;
        this.view = liveView;
        liveView.setPresenter(this);
        this.isFromCourt = isFromCourt;

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
    public void getNetData(int pageIndex, int pageSize) {
        if (context != null) {
            int hobby = 0;
            if (isFromCourt){
                User userInfo = userService.getUserInfo(userService.getLoginUserId());
                if (userInfo != null) {
                    hobby = userInfo.getSportHobby();
                }
            }
            NetManager.getInstance(context).queryLiveOrVideoList(userService.getLoginUserId(), hobby, pageIndex, pageSize, "LIVE", "", new OnResponseListener<String>() {
                @Override
                public void onComplete(boolean isOK, String result, int errorCode, String errorMsg) {
                    CircleLiveList list = new Gson().fromJson(result, CircleLiveList.class);
                    if (view != null) view.showData(list);
                }

                @Override
                public void onInternError(int errorCode, String errorMessage) {
                    if (view != null)
                        view.showErrorView(R.mipmap.default_page_video, context.getString(R.string.circle_no_data), "");
                }
            });
        }
    }

    @Override
    public void getCacheData() {

    }
}
