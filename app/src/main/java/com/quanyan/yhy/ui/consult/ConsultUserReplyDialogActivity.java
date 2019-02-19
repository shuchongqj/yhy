package com.quanyan.yhy.ui.consult;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.mogujie.tt.protobuf.helper.EntityChangeEngine;
import com.mogujie.tt.ui.activity.ChatAcitivity;
import com.newyhy.activity.HomeMainTabActivity;
import com.quanyan.base.BaseActivity;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;

import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.util.MobileUtil;
import com.yhy.common.constants.ConsultContants;
import com.yhy.common.constants.DBConstant;
import com.yhy.common.constants.IntentConstant;

/**
 * Created with Android Studio.
 * Title:ConsultRequestDialogActivity
 * Description:达人确定应答，用户的提醒窗口
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/6/30
 * Time:10:59
 * Version 1.0
 */
public class ConsultUserReplyDialogActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(layoutParams);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitleBarBackground(Color.TRANSPARENT);
        findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TCEventHelper.onEvent(v.getContext(), AnalyDataValue.IM_CONSULTING_USER_REMIND_LATER);
                finish();
            }
        });
        findViewById(R.id.tv_answer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long itemId = getIntent().getLongExtra(ConsultContants.ITEMID, 0);
                long sellerId = getIntent().getLongExtra(ConsultContants.SELLERID, 0);
                TCEventHelper.onEvent(v.getContext(), AnalyDataValue.IM_CONSULTING_ACK);
                if (MobileUtil.isActivityRunning(getBaseContext(), HomeMainTabActivity.class.getName())) {
                    NavUtils.gotoMessageActivity(ConsultUserReplyDialogActivity.this, sellerId, null, itemId);
                } else {
                    Intent intent = new Intent(ConsultUserReplyDialogActivity.this, ChatAcitivity.class);
                    intent.putExtra(IntentConstant.KEY_SESSION_KEY, EntityChangeEngine.getSessionKey(sellerId, DBConstant.SESSION_TYPE_CONSULT, itemId));
                    startActivity(intent);
                }
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_consult_user_recive_dialog, null);
    }

    @Override
    public View onLoadNavView() {
        return null;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    public static void gotoConsultUserReplyDialogActivity(Context context, long itemId, long sellerId) {
        Intent intent = new Intent(context, ConsultUserReplyDialogActivity.class);
        intent.putExtra(ConsultContants.ITEMID, itemId);
        intent.putExtra(ConsultContants.SELLERID, sellerId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
