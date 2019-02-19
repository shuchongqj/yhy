package com.quanyan.yhy.ui.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.RegexUtil;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.yhy.common.constants.ValueConstants;

/**
 * Created with Android Studio.
 * Title:FeedbackActivity
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2015-11-12
 * Time:9:36
 * Version 1.0
 */
public class FeedbackActivity extends BaseActivity implements TextWatcher {

    @ViewInject(R.id.add_feed_content)
    private EditText add_feed_content;

    @ViewInject(R.id.add_feed_info)
    private EditText add_feed_info;

    @ViewInject(R.id.add_feed_num_change)
    private TextView add_feed_num_change;

    private AboutAndFeedController mController;

    //跳转到意见反馈
    public static void gotoFeedbackActivity(Context context) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void handleMessage(Message msg) {
        hideLoadingView();
        switch (msg.what) {
            case ValueConstants.MSG_FEEDBACK_OK://提交成功
                ToastUtil.showToast(this, getString(R.string.feedback_finish));
                finish();
                break;
            case ValueConstants.MSG_FEEDBACK_KO:
                ToastUtil.showToast(this, getString(R.string.toast_feedback_failed));
                break;
        }

    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_feedback, null);
    }

    private BaseNavView mBaseNavView;

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText(getString(R.string.label_settings_feedback));
        mBaseNavView.setRightText(getString(R.string.btn_send));
        mBaseNavView.setRightTextClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onfinish();
            }
        });
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        mController = new AboutAndFeedController(this, mHandler);
        add_feed_content.addTextChangedListener(this);
        changeButton();
    }

    private void onfinish() {
        String content = add_feed_content.getText().toString().trim();
        String infomsg = add_feed_info.getText().toString().trim();
        if (StringUtil.isEmpty(content)) {
            return;
        }
        if (!StringUtil.isEmpty(infomsg)) {
            if (infomsg.length() < 6 || infomsg.length() > 40) {
                ToastUtil.showToast(this, getString(R.string.phone_email_error));
                return;
            }
            if (RegexUtil.isEmail(infomsg) || RegexUtil.isMobile(infomsg)) {

            } else {
                ToastUtil.showToast(this, getString(R.string.phone_email_error));
                return;
            }

        }
        showLoadingView(getString(R.string.loading_text));
        mController.doFeedback(FeedbackActivity.this, content, infomsg);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        add_feed_num_change.setText(s.length() + "/" + "300字");
        changeButton();
    }

    //完成按钮点击监听事件
    private void changeButton() {
        if (!StringUtil.isEmpty(add_feed_content.getText().toString())) {
            /*sm_title_bar_btn_right.setTextColor(getResources().getColor(R.color.color_norm_ff922b));
            sm_title_bar_btn_right.setClickable(true);
            sm_title_bar_btn_right.setOnClickListener(this);*/
            //System.out.println("ApiContext111" + add_feed_content.getText().toString());
            mBaseNavView.setRightTextColor(R.color.neu_333333);
            mBaseNavView.setRightTextEnable(true);
        } else {
            /*sm_title_bar_btn_right.setTextColor(getResources().getColor(R.color.neu_cccccc));
            sm_title_bar_btn_right.setClickable(false);*/
            //System.out.println("ApiContext222" + add_feed_content.getText().toString());
            mBaseNavView.setRightTextColor(R.color.neu_cccccc);
            mBaseNavView.setRightTextEnable(true);
        }
    }
}
