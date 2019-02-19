package com.videolibrary.client.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.quanyan.base.BaseActivity;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.videolibrary.client.fragment.VideoListFragment;
import com.videolibrary.utils.IntentUtil;

/**
 * Created with Android Studio.
 * Title:LiveListActivity
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/8/8
 * Time:14:06
 * Version 1.1.0
 */

/**
 * <li>直播列表</li>
 */
public class LiveListActivity extends BaseActivity {

    private String mTitle;
    @Override
    protected void initView(Bundle savedInstanceState) {
        Bundle bundle = null;
        if(getIntent()!= null) {
            bundle = getIntent().getExtras();
            mTitle = bundle.getString(IntentUtil.BUNDLE_TITLE);
        }

        mBaseNavView.setTitleText(TextUtils.isEmpty(mTitle) ? "" : mTitle);
        VideoListFragment videoListFragment = VideoListFragment.newInstance(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.ac_base_content_view,
                videoListFragment, LiveListActivity.class.getSimpleName()).commit();
    }

    @Override
    public View onLoadContentView() {
        return null;
    }

    private BaseNavView mBaseNavView;

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }
}
