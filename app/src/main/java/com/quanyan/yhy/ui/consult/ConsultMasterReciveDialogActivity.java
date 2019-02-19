package com.quanyan.yhy.ui.consult;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.mogujie.tt.protobuf.helper.EntityChangeEngine;
import com.mogujie.tt.ui.activity.ChatAcitivity;
import com.newyhy.activity.HomeMainTabActivity;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;

import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.consult.controller.ConsultController;
import com.quanyan.yhy.util.MobileUtil;
import com.yhy.common.beans.net.model.tm.AcceptProcessOrderResult;
import com.yhy.common.constants.DBConstant;
import com.yhy.common.constants.IntentConstant;
import com.yhy.common.constants.ValueConstants;

/**
 * Created with Android Studio.
 * Title:ConsultRequestDialogActivity
 * Description:达人接受到应答的提示框
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/6/30
 * Time:10:59
 * Version 1.0
 */
public class ConsultMasterReciveDialogActivity extends BaseActivity {
    public static void gotoConsultMasterReciveDialogActivity(Context context) {
        Intent intent = new Intent(context, ConsultMasterReciveDialogActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    ConsultController controller;

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
                TCEventHelper.onEvent(v.getContext(), AnalyDataValue.IM_CONSULTING_TALENT_REMIND_LATER);
                finish();
            }
        });
        findViewById(R.id.tv_answer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingView(null);
                findViewById(R.id.tv_answer).setEnabled(false);
                TCEventHelper.onEvent(v.getContext(), AnalyDataValue.IM_CONSULTING_ACCEPT);
                controller.acceptProcessOrder(ConsultMasterReciveDialogActivity.this);
            }
        });
        controller = new ConsultController(this, mHandler);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_consult_recive_dialog, null);
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
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        int what = msg.what;
        hideLoadingView();
        if (what == ValueConstants.ACCEPT_PROCESS_ORDER_OK) {
            AcceptProcessOrderResult result = (AcceptProcessOrderResult) msg.obj;
            if (result.success) {
                long itemId =result.processOrder.itemId;
                long userId = result.processOrder.buyerInfo.userId;
                if (MobileUtil.isActivityRunning(getBaseContext(), HomeMainTabActivity.class.getName())) {
                    NavUtils.gotoMessageActivity(this, userId, null, itemId);
                } else {
                    Intent intent = new Intent(this, ChatAcitivity.class);
                    intent.putExtra(IntentConstant.KEY_SESSION_KEY, EntityChangeEngine.getSessionKey(userId, DBConstant.SESSION_TYPE_CONSULT, itemId));
                    startActivity(intent);
                }
                this.finish();
            } else {
                if (!StringUtil.isEmpty(result.message)) {
                    ToastUtil.showToast(this, result.message);
                }
                findViewById(R.id.tv_answer).setEnabled(true);
            }

        } else if (what == ValueConstants.ACCEPT_PROCESS_ORDER_KO) {
            ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
            findViewById(R.id.tv_answer).setEnabled(true);
        }
    }
}
