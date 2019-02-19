package com.quanyan.yhy.ui.discovery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;

import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ShieldType;
import com.quanyan.yhy.ui.club.clubcontroller.ClubController;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.views.BlackSettingItem;
import com.yhy.common.constants.ValueConstants;

import de.greenrobot.event.EventBus;

/**
 * Created with Android Studio.
 * Title:BlackSettingActivity
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/5/14
 * Time:下午6:07
 * Version 1.1.0
 */
public class BlackSettingActivity extends BaseActivity {
    private ClubController mController;
    private long userId;
    private long id;
    private String nickname;
//    private UgcInfoResult mSubjectInfo ;

    private BlackSettingItem mBlackUserItem;
    private BlackSettingItem mBlackDynamicItem;

    private BaseNavView mBaseNavView;

    /**
     * 屏蔽
     *
     * @param context
     */
    public static void gotoBlackSetting(Context context, long userId, long id, String nickname) {
        Intent intent = new Intent(context, BlackSettingActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("id", id);
        intent.putExtra("nickname", nickname);
        context.startActivity(intent);
    }

    @Override
    public void handleMessage(Message msg) {
        hideLoadingView();
        switch (msg.what) {
            case ValueConstants.MSG_BLACK_OK:
                ToastUtil.showToast(this, getString(R.string.toast_black_ok));
                EventBus.getDefault().post(msg.obj);
                finish();
                break;
            case ValueConstants.MSG_BLACK_KO:
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mController = new ClubController(this, mHandler);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        userId = getIntent().getLongExtra("userId", -1);
        id = getIntent().getLongExtra("id", -1);
        nickname = getIntent().getStringExtra("nickname");

        mBlackUserItem = (BlackSettingItem) findViewById(R.id.menu_black_user);
        mBlackDynamicItem = (BlackSettingItem) findViewById(R.id.menu_black_dynamic);

        mBlackUserItem.initItem(R.mipmap.ic_black_user, String.format(getString(R.string.label_black_user),  nickname));
        mBlackUserItem.setSwitch(false);
        mBlackUserItem.setSummary(getString(R.string.label_summary_black_user));
        mBlackUserItem.setSwitchListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (mBlackDynamicItem.getCheckedValue()) {
                    mBlackDynamicItem.setSwitch(false);
                }
            }
        });

        mBlackDynamicItem.initItem(R.mipmap.ic_black_dynamic, R.string.label_black_dynamic);
        mBlackDynamicItem.setSwitch(false);
        mBlackDynamicItem.setSummary(getString(R.string.label_summary_black_dynamic));
        mBlackDynamicItem.setSwitchListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (mBlackUserItem.getCheckedValue()) {
                    mBlackUserItem.setSwitch(false);
                }
            }
        });
    }

    /**
     * 更新屏蔽用户的状态
     *
     */
    private void changeBlackUserStatus( ) {
        showLoadingView("");
        mController.doBlackUserOrDynamic(BlackSettingActivity.this, ShieldType.USER_SUBJECT,  userId);
    }

    /**
     * 更新屏蔽动态的状态
     *
     */
    private void changeBlackDynamicStatus( ) {
        showLoadingView("");
        mController.doBlackUserOrDynamic(BlackSettingActivity.this, ShieldType.SUBJECT,  id);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_black_settings, null);
    }

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText(getString(R.string.label_title_black_settings));
        mBaseNavView.setRightText(getString(R.string.label_btn_submit));
        mBaseNavView.setRightTextClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBlackUserItem.getCheckedValue()) {
                    changeBlackUserStatus();
                } else if (mBlackDynamicItem.getCheckedValue()) {
                    changeBlackDynamicStatus();
                }
            }
        });
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }
}
