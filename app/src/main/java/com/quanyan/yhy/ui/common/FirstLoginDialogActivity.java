package com.quanyan.yhy.ui.common;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanyan.base.BaseActivity;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.yhy.common.utils.SPUtils;

/**
 * Created with Android Studio.
 * Title:FirstLoginDialogActivity
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-8-30
 * Time:16:36
 * Version 1.1.0
 */

public class FirstLoginDialogActivity extends BaseActivity implements View.OnClickListener {
    private Dialog mDialogShow;
    private TextView mStringDesc;
    private ImageView mIvCloseImg;
    private ImageView mIvBackImg;
    private ImageView mIvPedImg;

    public static void gotoFirstLoginDialogActivity(Context context, String content) {
        Intent intent = new Intent(context, FirstLoginDialogActivity.class);
        intent.putExtra(SPUtils.EXTRA_DATA, content);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        String content = getIntent().getStringExtra(SPUtils.EXTRA_DATA);
        findId();
        initClick();
        mStringDesc.setText(content);
        /*mDialogShow = DialogUtil.showMessageDialog(this, null, content,
                getString(R.string.label_dialog_right), getString(R.string.label_dialog_left), new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //计步器首页
                        TCEventHelper.onEvent(FirstLoginDialogActivity.this, AnalyDataValue.LOGIN_RECEIVE_INTEGRAL);
                        NavUtils.gotoPedometerActivity(FirstLoginDialogActivity.this);
                        finish();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TCEventHelper.onEvent(FirstLoginDialogActivity.this, AnalyDataValue.LOGIN_LATER_RECEIVE);
                        finish();
                    }
                });
        mDialogShow.show();*/
    }

    private void initClick() {
        mIvCloseImg.setOnClickListener(this);
        mIvBackImg.setOnClickListener(this);
        mIvPedImg.setOnClickListener(this);
    }

    private void findId() {
        mStringDesc = (TextView) findViewById(R.id.tv_string_desc);
        mIvCloseImg = (ImageView) findViewById(R.id.iv_close_img);
        mIvBackImg = (ImageView) findViewById(R.id.iv_back_img);
        mIvPedImg = (ImageView) findViewById(R.id.iv_ped_img);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_first_login_dialog, null);
    }

    @Override
    public View onLoadNavView() {
        return null;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mDialogShow != null){
            mDialogShow.dismiss();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_BACK == keyCode){
            finish();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_close_img:
                finish();
                break;
            case R.id.iv_back_img:
                TCEventHelper.onEvent(FirstLoginDialogActivity.this, AnalyDataValue.LOGIN_LATER_RECEIVE);
                finish();
                break;
            case R.id.iv_ped_img:
                TCEventHelper.onEvent(FirstLoginDialogActivity.this, AnalyDataValue.LOGIN_RECEIVE_INTEGRAL);
                NavUtils.gotoPedometerActivity(FirstLoginDialogActivity.this);
                finish();
                break;
        }
    }
}
