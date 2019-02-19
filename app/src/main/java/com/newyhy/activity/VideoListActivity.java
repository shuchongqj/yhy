package com.newyhy.activity;


import android.os.Message;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.newyhy.contract.presenter.CircleStandardVideoPresenter;
import com.newyhy.fragment.circle.CircleStandardVideoFragment;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.yhy.router.RouterPath;

/**
 * 精彩视频列表
 * Created by nandy on 2018/7/7
 */
@Route(path = RouterPath.ACTIVITY_VIDEO_LIST)
public class VideoListActivity extends VideoSupportActivity {
    // 标题
    private BaseNavView mBaseNavView;
    private CircleStandardVideoFragment circle_standard_video;//视频
    private CircleStandardVideoPresenter standardPresenter;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_video_list;
    }

    @Override
    protected void initVars() {
        super.initVars();
    }

    @Override
    protected void initView() {
        super.initView();
        mImmersionBar.fitsSystemWindows(true).transparentStatusBar().statusBarDarkFont(true).init();
        mBaseNavView = findViewById(R.id.title_view);
        mBaseNavView.setTitleText("精彩视频");
        circle_standard_video = (CircleStandardVideoFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_content);
        standardPresenter = new CircleStandardVideoPresenter(this, circle_standard_video, true);
    }

    @Override
    protected void setListener() {
        super.setListener();
        mBaseNavView.setLeftClick((v) -> finish());
    }

    @Override
    protected void initData() {
        super.initData();
    }


    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        standardPresenter.release();
        standardPresenter = null;
    }

}
