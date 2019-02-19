package com.newyhy.adapter.circle;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.newyhy.views.itemview.CircleCoffeeVideoTabLayout;
import com.newyhy.views.itemview.CircleUgcCoffeeVideoLayout;
import com.quanyan.yhy.R;
import com.yhy.common.beans.net.model.discover.UgcInfoResult;

import java.util.HashMap;
import java.util.List;

/**
 * 改版圈子中 小视频模块 Adapter
 * Created by Jiervs on 2018/6/19.
 */

public class CoffeeVideoAdapter extends BaseQuickAdapter<UgcInfoResult,BaseViewHolder> {

    private Activity mActivity;
    private Fragment mFragment;
    private List<UgcInfoResult> mList;
    public HashMap<String, String> extraMap;

    public CoffeeVideoAdapter(Activity mActivity, @Nullable Fragment mFragment, @Nullable List<UgcInfoResult> list) {
        super(R.layout.circle_coffee_tab_recycler_item,list);
        this.mActivity = mActivity;
        this.mFragment = mFragment;
        this.mList = list;
    }

    @Override
    protected void convert(BaseViewHolder holder, UgcInfoResult item) {
        CircleCoffeeVideoTabLayout coffee = holder.getView(R.id.circle_coffee_tab_item);
        coffee.setData(mActivity,item);
        coffee.position = holder.getAdapterPosition();
        coffee.extraMap = extraMap;
    }
}
