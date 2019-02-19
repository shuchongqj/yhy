package com.newyhy.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.circles.TabCirclesFragment;
import com.yhy.common.base.BaseNewActivity;
import com.yhy.router.RouterPath;

/**
 * 热门话题列表页面
 * Created by Jiervs on 2018/6/27.
 */

@Route(path = RouterPath.ACTIVITY_TOPIC_LIST)
public class HotTopicListActivity extends BaseNewActivity {

    private BaseNavView nav_view;
    private Fragment fragment;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_hot_topic_list;
    }

    @Override
    protected void initView() {
        super.initView();
        mImmersionBar.fitsSystemWindows(false).transparentStatusBar().statusBarDarkFont(true).init();
        nav_view = findViewById(R.id.nav_view);
        nav_view.setTitleText(R.string.topic_list);
        nav_view.setBackgroundColor(getResources().getColor(R.color.white));

        fragment = TabCirclesFragment.getInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_content, fragment);
        transaction.commit();

    }
}
