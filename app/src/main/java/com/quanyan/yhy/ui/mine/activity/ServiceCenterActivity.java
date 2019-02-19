package com.quanyan.yhy.ui.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.yhy.common.utils.SPUtils;


/**
 * Created with Android Studio.
 * Title:ServiceCenterActivity
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-11-14
 * Time:16:33
 * Version 1.1.0
 */


public class ServiceCenterActivity extends BaseActivity implements View.OnClickListener {

    private BaseNavView mBaseNavView;
    private RelativeLayout mRLOnline;
    private RelativeLayout mRLPhone;


    public static void gotoServiceCenterActivity(Context context) {
        Intent intent = new Intent(context, ServiceCenterActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        mBaseNavView.setTitleText(R.string.labe_service_certer_title);
        findId();
        setListener();
    }

    private void setListener() {
        mRLOnline.setOnClickListener(this);
        mRLPhone.setOnClickListener(this);
    }

    private void findId() {
        mRLOnline = (RelativeLayout) findViewById(R.id.rl_online);
        mRLPhone = (RelativeLayout) findViewById(R.id.rl_phone);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_service_center, null);
    }

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_online:
//                NavUtils.gotoMessageActivity(ServiceCenterActivity.this, (int) SPUtils.getServiceUID(this));
                NavUtils.gotoMessageActivity(ServiceCenterActivity.this, 1002);
                break;
            case R.id.rl_phone:
                LocalUtils.call(ServiceCenterActivity.this, SPUtils.getServicePhone(this));
                break;
        }
    }
}
