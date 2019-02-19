package com.mogujie.tt.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.mogujie.tt.DB.sp.ConfigurationSp;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.views.AppSettingItem;
import com.yhy.common.constants.SysConstant;
import com.yhy.common.utils.SPUtils;
import com.yhy.service.IUserService;

/**
 * Created with Android Studio.
 * Title:MessageNotificationSettingActivity
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/3/31
 * Time:10:50
 * Version 1.0
 */
public class MessageNotificationSettingActivity extends BaseActivity {
    @ViewInject(R.id.settings_noti_switch)
    private AppSettingItem mNotificationSwitch;
    @ViewInject(R.id.settings_sound_switch)
    private AppSettingItem mSoundSwitch;
    private ConfigurationSp configurationSp;

    @Autowired
    IUserService userService;

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.tt_activity_message_noti_setting, null);
    }

    private BaseNavView mBaseNavView;

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText(R.string.label_settings_im_switch);
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        initData();
        initView();
    }

    private void initData() {
        configurationSp = ConfigurationSp.instance(this, userService.getLoginUserId());
        boolean notificationSwitchable = configurationSp.getCfg(SysConstant.SETTING_GLOBAL, ConfigurationSp.CfgDimension.NOTIFICATION);
        mNotificationSwitch.setSwitch(notificationSwitchable);
        if (!notificationSwitchable) {
            mSoundSwitch.setSwitchable(false);
        }
        mSoundSwitch.setSwitch(configurationSp.getCfg(SysConstant.SETTING_GLOBAL, ConfigurationSp.CfgDimension.SOUND));

    }

    private void initView() {
        mNotificationSwitch.initItem(-1, R.string.receive_new_message_notification);
        mNotificationSwitch.setSwitchListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                configurationSp.setCfg(SysConstant.SETTING_GLOBAL, ConfigurationSp.CfgDimension.NOTIFICATION, isChecked);
                mSoundSwitch.setSwitch(isChecked);
                mSoundSwitch.setSwitchable(isChecked);
            }
        });
        mSoundSwitch.initItem(-1, R.string.sound);
        mSoundSwitch.setSwitchListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                configurationSp.setCfg(SysConstant.SETTING_GLOBAL, ConfigurationSp.CfgDimension.SOUND, isChecked);
            }
        });
    }
}
