package com.newyhy.contract.presenter;


import android.content.Context;
import android.support.annotation.NonNull;

import com.newyhy.contract.CircleFollowContract;
import com.quanyan.yhy.R;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.yhy.common.beans.net.model.discover.UgcInfoResultList;

/**
 * CircleFollowPresenter
 * Created by Jiervs on 2018/6/21.
 */

public class CircleFollowPresenter implements CircleFollowContract.Presenter {

    private Context context;

    private CircleFollowContract.View view;

    public CircleFollowPresenter(Context context,@NonNull CircleFollowContract.View followView) {
        this.context = context;
        this.view = followView;
        followView.setPresenter(this);
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
        NetManager.getInstance(context).doGetUGCPageList(pageIndex, pageSize, 2, new OnResponseListener<UgcInfoResultList>() {
            @Override
            public void onComplete(boolean isOK, UgcInfoResultList result, int errorCode, String errorMsg) {
                if (isOK && result != null && result.ugcInfoList != null && result.ugcInfoList.size() > 0) {
                    if (view != null) view.showData(result);
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {
                if (view != null) view.showErrorView(R.mipmap.default_page_lists,context.getString(R.string.circle_no_data),"", v -> {});
            }
        });
    }

    @Override
    public void getCacheData() {

    }
}
