package com.quanyan.yhy.ui.wallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.newyhy.utils.StringUtils;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.eventbus.EvBusChangePhone;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.login.ChangePasswordActivity;
import com.quanyan.yhy.ui.login.ChangePhoneActivity;
import com.quanyan.yhy.ui.views.AppSettingItem;
import com.quanyan.yhy.ui.wallet.Controller.WalletController;
import com.quanyan.yhy.ui.wallet.WalletUtils;
import com.quanyan.yhy.ui.wallet.view.WalletTopView;
import com.yhy.common.beans.net.model.paycore.EleAccountInfo;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;

import de.greenrobot.event.EventBus;

/**
 * Created with Android Studio.
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-8-11
 * Time:14:04
 * Version 1.0
 * Description:
 */
public class PassWordManagerActivity extends BaseActivity {

    private WalletTopView mOrderTopView;
//    @ViewInject(R.id.rl_update_pas)
//    private RelativeLayout mUpdatePas;
    @ViewInject(R.id.rl_forget_pas)
    private RelativeLayout mForgetPas;

    @ViewInject(R.id.settings_password)
    private AppSettingItem settings_password;
    @ViewInject(R.id.settings_change_phone)
    private AppSettingItem settings_change_phone;

    private WalletController mWalletController;

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        EventBus.getDefault().register(this);

        mWalletController = new WalletController(this, mHandler);

        settings_change_phone.initItem(-1, R.string.phone_number);
        settings_change_phone.setSummary(StringUtils.transformPhone(SPUtils.getMobilePhone(PassWordManagerActivity.this)));

        settings_password.initItem(-1, R.string.label_settings_password);
        settings_password.setSummary("修改密码");

        initClick();
    }

    private void initClick() {
        mOrderTopView.setBackClickListener(PassWordManagerActivity.this::finish);

        settings_password.setOnClickListener(v -> startActivity(new Intent(PassWordManagerActivity.this, ChangePasswordActivity.class)));

        settings_change_phone.setOnClickListener(v -> ChangePhoneActivity.goToThisActivity(PassWordManagerActivity.this, 1));

        mForgetPas.setOnClickListener(v -> startActivity(new Intent(PassWordManagerActivity.this, PayPassWordManagerActivity.class)));
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_pass_word_manager, null);
    }

    @Override
    public View onLoadNavView() {
        mOrderTopView = new WalletTopView(this);
        mOrderTopView.setTitle("账号与安全", null);
        return mOrderTopView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserPayMsg();
    }

    //请求接口获取个人钱包信息
    private void getUserPayMsg() {
        mWalletController.doGetEleAccountInfo(this);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case ValueConstants.PAY_GETELEACCOUNTINFO_SUCCESS:
                EleAccountInfo info = (EleAccountInfo) msg.obj;
                handMsg(info);
                break;
            case ValueConstants.PAY_GETELEACCOUNTINFO_ERROR:
                ToastUtil.showToast(PassWordManagerActivity.this, StringUtil.handlerErrorCode(PassWordManagerActivity.this, msg.arg1));
                break;
        }
    }

    private void handMsg(EleAccountInfo info) {
        if (info == null) {
        } else {
            if (!TextUtils.isEmpty(info.accountType)) {
                if (info.accountType.equals(WalletUtils.PERSON)) {
                    if (!TextUtils.isEmpty(info.status)) {
                        if (info.status.equals(WalletUtils.TACK_EFFECT)) {
                            if (info.existPayPwd) {
                                if (!TextUtils.isEmpty(info.userName)) {
                                    SPUtils.saveWalletName(PassWordManagerActivity.this, info.userName);
                                }
                            }
                        }
                    }
                } else if (info.accountType.equals(WalletUtils.COMPANY)) {
                    ToastUtil.showToast(this, "企业用户请登录商户中心查看钱包信息");
                    PassWordManagerActivity.this.finish();
                } else {
                    ToastUtil.showToast(this, "参数错误");
                    PassWordManagerActivity.this.finish();
                }
            } else {
                ToastUtil.showToast(this, "参数错误");
                PassWordManagerActivity.this.finish();
            }
        }
    }

    public void onEvent(EvBusChangePhone event) {
        finish();
    }

}
