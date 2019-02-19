package com.videolibrary.puhser.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import com.quanyan.base.BaseActivity;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.views.AppSettingItem;
import com.videolibrary.utils.IntentUtil;
import com.yhy.common.beans.net.model.msg.LiveCategoryResult;

import java.util.ArrayList;

/**
 * Created with Android Studio.
 * Title:LiveSettingActivity
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/8/25
 * Time:14:47
 * Version 1.0
 */
public class LiveSettingsActivity extends BaseActivity implements View.OnClickListener {
    BaseNavView navView;
    AppSettingItem mNumber;
    AppSettingItem mNotice;
    AppSettingItem mSort;
    long roomId;
    private String notice;
    private String categoryName;
    private long categoryCode;
    private static final int REQUEST_NOTICE_CODE = 1;
    private static final int REQUEST_CATEGORY_CODE = 2;
    ArrayList<LiveCategoryResult> categoryResults;

    @Override
    protected void initView(Bundle savedInstanceState) {
        mNumber = (AppSettingItem) findViewById(R.id.ac_live_settings_number);
        mNotice = (AppSettingItem) findViewById(R.id.ac_live_settings_notice);
        mSort = (AppSettingItem) findViewById(R.id.ac_live_settings_sort);
        navView.setTitleText("直播设置");
        initListener();
        initData();
    }

    private void initData() {
        notice = getIntent().getStringExtra(IntentUtil.BUNDLE_NOTICE);
        categoryName = getIntent().getStringExtra(IntentUtil.BUNDLE_CATEGORY_NAME);
        categoryCode = getIntent().getLongExtra(IntentUtil.BUNDLE_CATEGORY_CODE, 0);
        roomId = getIntent().getLongExtra(IntentUtil.BUNDLE_ROOM_ID, 0);
        categoryResults = (ArrayList<LiveCategoryResult>) getIntent().getSerializableExtra(IntentUtil.BUNDLE_CATEGORY_RESULT);
        mNumber.setTitle(R.string.home_number);
        mNumber.setSummary(String.valueOf(roomId));
        mNumber.findViewById(R.id.iv_go).setVisibility(View.GONE);

        mNotice.setTitle(R.string.live_notice);
        mNotice.setSummary(notice);

        mSort.setTitle(R.string.live_sort);
        mSort.setSummary(categoryName);
    }

    private void initListener() {
        mNotice.setOnClickListener(this);
        mSort.setOnClickListener(this);
        navView.setLeftClick(mBackListener);
    }

    View.OnClickListener mBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finishAc();
        }
    };

    private void finishAc() {
        Intent intent = new Intent();
        intent.putExtra(IntentUtil.BUNDLE_NOTICE, notice);
        intent.putExtra(IntentUtil.BUNDLE_CATEGORY_NAME, categoryName);
        intent.putExtra(IntentUtil.BUNDLE_CATEGORY_CODE, categoryCode);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event != null && keyCode == KeyEvent.KEYCODE_BACK) {
            finishAc();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public View onLoadContentView() {
        return LayoutInflater.from(this).inflate(R.layout.ac_live_settings, null);
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
        if (id == mNotice.getId()) {
            TCEventHelper.onEvent(this, AnalyDataValue.LIVE_SETTING_NOTIC_CLICK);
            IntentUtil.startLiveNoticeActivity(this, notice, REQUEST_NOTICE_CODE);
        } else if (id == mSort.getId()) {
            TCEventHelper.onEvent(this, AnalyDataValue.LIVE_SETTING_TYPE_CLICK);
            IntentUtil.startLiveCategoryActivity(this, categoryCode, categoryResults, REQUEST_CATEGORY_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_NOTICE_CODE) {
            if (resultCode == RESULT_OK) {
                notice = data.getStringExtra(IntentUtil.BUNDLE_NOTICE);
                refreshData();
            }
        } else if (requestCode == REQUEST_CATEGORY_CODE) {
            if (resultCode == RESULT_OK) {
                categoryCode = data.getLongExtra(IntentUtil.BUNDLE_CATEGORY_CODE, 0);
                refreshData();
            }
        }
    }

    private void refreshData() {
        for (int i = 0; i < categoryResults.size(); i++) {
            if (categoryResults.get(i).code == categoryCode) {
                categoryName = categoryResults.get(i).name;
                break;
            }
        }
        mNotice.setSummary(notice);
        mSort.setSummary(categoryName);
    }
}
