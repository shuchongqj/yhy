package com.videolibrary.puhser.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.quanyan.base.BaseActivity;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.videolibrary.utils.IntentUtil;
import com.yhy.common.utils.SPUtils;

/**
 * Created with Android Studio.
 * Title:LiveNoticeActivity
 * Description:直播公告
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/8/26
 * Time:9:33
 * Version 1.0
 */
public class LiveNoticeActivity extends BaseActivity implements View.OnClickListener {
    BaseNavView navView;
    private String notice;
    private String defaultNotice;
    EditText mEdittext;
    TextView mWordCounts;
    TextView mFinish;

    @Override
    protected void initView(Bundle savedInstanceState) {
        mEdittext = (EditText) findViewById(R.id.ac_live_notice_edit);
        mWordCounts = (TextView) findViewById(R.id.ac_live_notice_words_count);
        mFinish = (TextView) findViewById(R.id.ac_live_notice_finish);
        initListener();
        initData();
    }

    private void initData() {
        defaultNotice = getString(R.string.user_live_notice, SPUtils.getNickName(this));
        notice = getIntent().getStringExtra("notice");
        mEdittext.setHint(defaultNotice);
        mEdittext.setText(notice);
        navView.setTitleText("直播公告");
    }

    private void initListener() {
        mEdittext.addTextChangedListener(mTextWatcher);
        mFinish.setOnClickListener(this);
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            notice = s.toString();
            mWordCounts.setText(String.valueOf(100 - mEdittext.getText().toString().length()));
        }
    };

    @Override
    public View onLoadContentView() {
        return LayoutInflater.from(this).inflate(R.layout.ac_live_notice, null);
    }

    @Override
    public View onLoadNavView() {
        return navView = new BaseNavView(this);
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == mFinish.getId()) {
            Intent intent = new Intent();
            intent.putExtra(IntentUtil.BUNDLE_NOTICE, TextUtils.isEmpty(notice) ? defaultNotice : notice);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
