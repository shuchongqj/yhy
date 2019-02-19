package com.quanyan.yhy.ui.signed.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.view.BaseTransparentNavView;
import com.quanyan.base.view.customview.pulltorefresh.PullToRefreshScrollView;
import com.quanyan.yhy.R;
import com.yhy.common.utils.SPUtils;


/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:任务说明
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:wjm
 * Date:16/7/4
 * Version 1.3.0
 */
public class TaskDescriptionActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.trasparent_topbar_title)
    private TextView mTopTitle;
    @ViewInject(R.id.trasparent_topbar_left)
    private ImageView mTopLeft;
    @ViewInject(R.id.trasparent_topbar_right)
    private ImageView mTopRightImg;
    @ViewInject(R.id.tv_taskdescription_content)
    private TextView tvContent;

    @ViewInject(R.id.pull_refresh_scrollview)
    private PullToRefreshScrollView mScrollview;


    /**
     * 跳转
     *
     * @param context 上下文对象
     */
    public static void gotoTaskDescriptionActivity(Context context ,String title,String content) {
        Intent intent = new Intent(context, TaskDescriptionActivity.class);
        intent.putExtra(SPUtils.EXTRA_TITLE, title);
        intent.putExtra(SPUtils.EXTRA_CONTENT, content);
        context.startActivity(intent);
    }



    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_task_description, null);
    }


    private BaseTransparentNavView mBaseTransparentNavView;

    @Override
    public View onLoadNavView() {
        mBaseTransparentNavView = new BaseTransparentNavView(this);
        return mBaseTransparentNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        mScrollview.setMode(PullToRefreshScrollView.Mode.DISABLED);
        //顶部栏
        mTopTitle.setVisibility(View.VISIBLE);
//        mTopTitle.setText(getIntent().getStringExtra(SPUtils.EXTRA_TITLE));
        mTopTitle.setText(R.string.label_title_tast_desc);
        tvContent.setText( getIntent().getStringExtra(SPUtils.EXTRA_CONTENT));
        mTopTitle.setTextColor(getResources().getColor(R.color.color_norm_000000));
        mTopLeft.setImageResource(R.mipmap.arrow_back_gray);
        mTopRightImg.setVisibility(View.GONE);
        ((ImageView) findViewById(R.id.trasparent_topbar_right_share)).setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.trasparent_topbar_left_layout:
                onBackPressed();
                break;
        }
    }


}


