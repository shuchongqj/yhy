package com.newyhy.adapter.circle;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.newyhy.beans.CircleLiveInfoResult;
import com.newyhy.views.itemview.CircleLiveTabLayout;
import com.quanyan.yhy.R;

import java.util.HashMap;
import java.util.List;

/**
 * 改版圈子中 直播模块 Adapter
 * Created by Jiervs on 2018/6/19.
 */

public class LiveAdapter extends BaseQuickAdapter<CircleLiveInfoResult,BaseViewHolder> {

    private Activity mActivity;
    private Fragment mFragment;
    private List<CircleLiveInfoResult> mList;
    public HashMap<String, String> extraMap;

    public LiveAdapter(Activity mActivity, @Nullable Fragment mFragment, @Nullable List<CircleLiveInfoResult> mList) {
        super(R.layout.circle_live_tab_recycler_item,mList);
        this.mActivity = mActivity;
        this.mFragment = mFragment;
        this.mList = mList;
    }

    @Override
    protected void convert(BaseViewHolder holder, CircleLiveInfoResult item) {
        CircleLiveTabLayout live = holder.getView(R.id.circle_live_tab_item);
        live.setData(mActivity,item);
        live.position = holder.getAdapterPosition();
        live.extraMap = extraMap;
    }
}
