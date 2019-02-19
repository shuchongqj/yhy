package com.quanyan.yhy.ui.wallet.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.harwkin.nb.camera.TimeUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.wallet.Controller.WalletController;
import com.quanyan.yhy.ui.wallet.WalletUtils;
import com.quanyan.yhy.ui.wallet.view.WalletTopView;
import com.quanyan.yhy.ui.wallet.view.gridpasswordview.GridPasswordView;
import com.yhy.common.beans.net.model.paycore.PayCoreBaseResult;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;

/**
 * Created with Android Studio.
 * Title:SettingPayPassActivity
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-8-9
 * Time:17:32
 * Version 1.0
 * Description: 设置支付密码
 */
public class SettingPayPassActivity extends BaseActivity {

    private static final String VERIFYCODE = "verifyCode";
    private static final String VICODE = "verifyIdentityCode";
    private static final String TYPE = "TYPE";
    private static final String TOWALLET = "TOWALLET";
    private WalletTopView mOrderTopView;

    @ViewInject(R.id.gpv_customUi)
    private GridPasswordView gpvCustomUi;
    @ViewInject(R.id.tv_title)
    private TextView mTitle;
    private boolean isFirst = true;
    private String firstPwd;
    @ViewInject(R.id.tv_setting_tips)
    private TextView mSettingTipsTv;

    @ViewInject(R.id.ll_error_tips)
    private LinearLayout mErrorTips;
    @ViewInject(R.id.btn_config)
    private Button mBtnConfig;

    private WalletController mWalletController;

    private String verifyCode;
    private String verifyIdentityCode;
    private String type;

    private String toWallet;
    public static void gotoSettingPayPassActivity(Context context, String verifyCode, String verifyIdentityCode, String type,String toWallet) {
        Intent intent = new Intent(context, SettingPayPassActivity.class);
        intent.putExtra(VERIFYCODE, verifyCode);
        intent.putExtra(VICODE, verifyIdentityCode);
        intent.putExtra(TYPE, type);
        intent.putExtra(TOWALLET, toWallet);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        verifyCode = getIntent().getStringExtra(VERIFYCODE);
        verifyIdentityCode = getIntent().getStringExtra(VICODE);
        type = getIntent().getStringExtra(TYPE);
        toWallet =getIntent().getStringExtra(TOWALLET);
        mWalletController = new WalletController(this, mHandler);

        if (type.equals(WalletUtils.FORGET_PAY_PWD)) {
            mOrderTopView.setTitle("修改密码", "安全支付");
            mTitle.setText("请设置支付密码，用于支付验证");
            mSettingTipsTv.setVisibility(View.GONE);
        } else {
            mOrderTopView.setTitle("设置支付密码", "安全支付");
            mTitle.setText("请输入6位数字支付密码");
            mSettingTipsTv.setText("为保护您账户安全，请设置支付密码");
        }
        gpvCustomUi.setBackground(getResources().getDrawable(R.drawable.gridpassword_bg));

        mBtnConfig.setBackgroundResource(R.drawable.btn_pay_unselect);
        mBtnConfig.setEnabled(false);

        initClick();
    }

    private void initClick() {
        mOrderTopView.setBackClickListener(new WalletTopView.BackClickListener() {
            @Override
            public void back() {
                if (type.equals(WalletUtils.FORGET_PAY_PWD)) {
                    showBack("是否放弃修改密码");
                } else {
                    showBack("是否放弃设置支付密码");
                }
            }
        });

        gpvCustomUi.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
                if (psw.length() == 6 && isFirst) {
                    Message msg = Message.obtain();
                    msg.what = 0x111;
                    msg.obj = psw;
                    mHandler.sendMessageDelayed(msg, 500);
                } else if (psw.length() == 6 && !isFirst) {
                    if (psw.equals(firstPwd)) {
                        WalletUtils.hideSoft(SettingPayPassActivity.this, gpvCustomUi.getWindowToken());
                        mBtnConfig.setBackgroundResource(R.drawable.btn_pay_orange_selector);
                        mBtnConfig.setEnabled(true);
                        // setPayPass(verifyCode, type, verifyIdentityCode, firstPwd);
                    } else {
                        mHandler.sendEmptyMessageDelayed(0x110, 500);
                    }
                }
            }

            @Override
            public void onInputFinish(String s) {

            }
        });

        //完成按钮
        mBtnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TimeUtil.isFastDoubleClick()) {
                    return;
                }
                setPayPass(verifyCode, type, verifyIdentityCode, firstPwd);
            }
        });

    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_setting_paypass, null);
    }

    @Override
    public View onLoadNavView() {
        mOrderTopView = new WalletTopView(this);
        return mOrderTopView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    //请求设置密码接口
    private void setPayPass(String verifyCode, String verifyCodeType, String verifyIdentityCode, String payPwd) {
        if (type.equals(WalletUtils.FORGET_PAY_PWD)) {
            showLoadingView("正在修改支付密码，请稍候...");
        } else {
            showLoadingView("正在设置支付密码，请稍候...");
        }
        mWalletController.doSetPayPassWord(verifyCode, verifyCodeType, verifyIdentityCode, payPwd, SettingPayPassActivity.this);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case ValueConstants.PAY_SetupPayPwd_SUCCESS:
                hideLoadingView();
                PayCoreBaseResult result = (PayCoreBaseResult) msg.obj;
                if (result.success) {
                    mHandler.sendEmptyMessageDelayed(0x10, 1000);
                } else {
                    ToastUtil.showToast(this, result.errorMsg);
                }
                break;
            case ValueConstants.PAY_SetupPayPwd_ERROR:
                hideLoadingView();
                if (mSettingTipsTv != null && mSettingTipsTv.getVisibility() == View.GONE) {
                    mSettingTipsTv.setVisibility(View.VISIBLE);
                }
                if (mErrorTips != null && mErrorTips.getVisibility() == View.VISIBLE) {
                    mErrorTips.setVisibility(View.GONE);
                }
                if (mBtnConfig != null && mBtnConfig.getVisibility() == View.VISIBLE) {
                    mBtnConfig.setVisibility(View.GONE);
                }

                if (type.equals(WalletUtils.FORGET_PAY_PWD)) {
                    mTitle.setText("请设置支付密码，用于支付验证");
                } else {
                    mSettingTipsTv.setVisibility(View.GONE);
                    mTitle.setText("请输入6位数字支付密码");
                }
                gpvCustomUi.clearPassword();
                isFirst = true;
                ToastUtil.showToast(SettingPayPassActivity.this, StringUtil.handlerErrorCode(SettingPayPassActivity.this, msg.arg1));
                break;

            case 0x10:
                 if(SPUtils.EXTRA_TO_WALLET.equals(toWallet)){
                     startActivity(new Intent(SettingPayPassActivity.this,WalletActivity.class));
                 }
                SettingPayPassActivity.this.finish();
                break;
            case 0x110:
//                ToastUtil.showToast(SettingPayPassActivity.this, "两次密码不一样，请重新输入");
                if (mErrorTips != null && mErrorTips.getVisibility() == View.GONE) {
                    mErrorTips.setVisibility(View.VISIBLE);
                }
                if (mBtnConfig != null && mBtnConfig.getVisibility() == View.VISIBLE) {
                    mBtnConfig.setVisibility(View.GONE);
                }
                if (type.equals(WalletUtils.FORGET_PAY_PWD)) {
                    mTitle.setText("请设置支付密码，用于支付验证");
                } else {
                    mSettingTipsTv.setVisibility(View.GONE);
                    mTitle.setText("请输入6位数字支付密码");
                }
                gpvCustomUi.clearPassword();
                isFirst = true;
                break;
            case 0x111:
                String psw = (String) msg.obj;
                if (mErrorTips != null && mErrorTips.getVisibility() == View.VISIBLE) {
                    mErrorTips.setVisibility(View.GONE);
                }
                if (mBtnConfig != null && mBtnConfig.getVisibility() == View.GONE) {
                    mBtnConfig.setVisibility(View.VISIBLE);
                    mBtnConfig.setBackgroundResource(R.drawable.btn_pay_unselect);
                    mBtnConfig.setEnabled(false);
                }
                if (mSettingTipsTv != null && mSettingTipsTv.getVisibility() == View.VISIBLE) {
                    mSettingTipsTv.setVisibility(View.GONE);
                }
                gpvCustomUi.clearPassword();
                isFirst = false;
                firstPwd = psw;
                mTitle.setText("再次确认支付密码");
                break;
        }
    }

    private Dialog mSurErrorDialog;

    private void showBack(String msg) {
        if (mSurErrorDialog == null) {
            mSurErrorDialog = DialogUtil.showMessageDialog(this,
                    null,
                    msg,
                    "是",
                    "否",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mSurErrorDialog != null) {
                                mSurErrorDialog.dismiss();
                            }
                            SettingPayPassActivity.this.finish();
                        }
                    },
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mSurErrorDialog != null) {
                                mSurErrorDialog.dismiss();
                            }
                        }
                    });
        }
        mSurErrorDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (type.equals(WalletUtils.FORGET_PAY_PWD)) {
                showBack("是否放弃修改密码");
            } else {
                showBack("是否放弃设置支付密码");
            }
        }
        return false;
    }
}
