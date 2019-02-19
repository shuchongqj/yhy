package com.quanyan.yhy.ui.servicerelease;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.views.MineMenuItem;

/**
 * Created with Android Studio.
 * Title:ServiceReleaseTypeActivity
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-6-27
 * Time:10:12
 * Version 1.1.0
 */

public class ServiceReleaseTypeActivity extends BaseActivity implements View.OnClickListener {

    private BaseNavView mBaseNavView;

    @ViewInject(R.id.item_activity)
    private MineMenuItem mActivityRelease;//发布活动

    @ViewInject(R.id.item_guide)
    private MineMenuItem mGuideRelease;//导游服务

    @ViewInject(R.id.item_consultation)
    private MineMenuItem mConsultationRelease;//咨询服务

    @ViewInject(R.id.item_expert)
    private MineMenuItem mExpertRelease;//领域专家

    @ViewInject(R.id.item_drafts)
    private MineMenuItem mDrafts;//草稿箱

    public static void gotoServiceReleaseTypeActivity(Context context) {
        Intent intent = new Intent(context, ServiceReleaseTypeActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        init();
        setListener();
    }

    private void setListener() {
        mActivityRelease.setOnClickListener(this);
        mGuideRelease.setOnClickListener(this);
        mConsultationRelease.setOnClickListener(this);
        mExpertRelease.setOnClickListener(this);
        mDrafts.setOnClickListener(this);
    }

    private void init() {
        mActivityRelease.initItem(R.mipmap.ic_my_setting, R.string.item_release_activity, -1);
        mGuideRelease.initItem(R.mipmap.ic_my_setting, R.string.item_release_guide, -1);
        mConsultationRelease.initItem(R.mipmap.ic_my_setting, R.string.item_release_consultation, -1);
        mExpertRelease.initItem(R.mipmap.ic_my_setting, R.string.item_release_expert, -1);
        mDrafts.initItem(R.mipmap.ic_my_setting, R.string.item_release_drafts, -1);
    }

    @Override
    public View onLoadContentView() {
        View view = View.inflate(this, R.layout.ac_release_service_type, null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText(R.string.label_release_service);
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_activity://活动
                break;
            case R.id.item_guide://导游
                break;
            case R.id.item_consultation://咨询
                break;
            case R.id.item_expert://专家
                break;
            case R.id.item_drafts://草稿箱
                break;
        }
    }
}
