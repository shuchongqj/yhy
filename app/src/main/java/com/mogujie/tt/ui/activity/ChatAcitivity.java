package com.mogujie.tt.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mogujie.tt.utils.IMUIHelper;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.yhy.common.constants.IntentConstant;
import com.yhy.common.constants.IntentConstants;

/**
 * Created with Android Studio.
 * Title:MessageCenterAcitivity
 * Description:消息中心
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/3/3
 * Time:9:55
 * Version 1.0
 */
public class ChatAcitivity extends BaseActivity {

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_message_center, null);
    }

    private BaseNavView mBaseNavView;

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText(R.string.mine_msg);
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (!gotoOtherActivity(getIntent())) return;
    }

    private boolean gotoOtherActivity(Intent intent) {
//        boolean isFromGonaActivity = intent.getBooleanExtra("isFromGonaActivity", false);
//        if (!isFromGonaActivity && !MobileUtil.isActivityRunning(this, GonaActivity.class.getName())) {
//            intent.setClass(this, GonaActivity.class);
//            intent.putExtra(SPUtils.EXTRA_TYPE, IntentConstants.START_FROM_NOTIFICATION);
//            startActivity(intent);
//            this.finish();
//            return false;
//        }

        String sessionKey = intent.getStringExtra(IntentConstant.KEY_SESSION_KEY);
        int subType = intent.getIntExtra(IntentConstants.EXTRA_BIZ_TYPE, -1);
        if (sessionKey != null) {
            IMUIHelper.openChatActivity(this, sessionKey);
        }
        if (subType != -1) {
            NavUtils.gotoNotificationListActivity(this, subType);
        }
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        gotoOtherActivity(intent);
    }
}
