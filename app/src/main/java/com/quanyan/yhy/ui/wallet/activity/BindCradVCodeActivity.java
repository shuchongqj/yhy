package com.quanyan.yhy.ui.wallet.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.harwkin.nb.camera.TimeUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.BaseApplication;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.wallet.Controller.WalletController;
import com.quanyan.yhy.ui.wallet.WalletUtils;
import com.quanyan.yhy.ui.wallet.view.WalletTopView;
import com.quanyan.yhy.ui.wallet.view.WalletXEditText;
import com.yhy.common.beans.net.model.paycore.PayCoreBaseResult;
import com.yhy.common.constants.ValueConstants;

/**
 * Created with Android Studio.
 * Title:BindCradVCodeActivity
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-9-19
 * Time:16:54
 * Version 1.0
 * Description:
 */
public class BindCradVCodeActivity extends BaseActivity {
    private static final String PHONE = "PHONE";
    private static final String IDCODE = "IDCODE";
    private static final String TYPE = "TYPE";
    private static final int TIME = 0x100;
    private static final long DelayMillis = 1000;
    private WalletTopView mOrderTopView;
    private String phone;
    private String verifyIdentityCode;

    @ViewInject(R.id.tv_tel_tips)
    private TextView mTelTips;
    @ViewInject(R.id.btn_auth_code)
    private Button mAuthCodeBtn;
    @ViewInject(R.id.et_code_num)
    private WalletXEditText mCodeNum;
    @ViewInject(R.id.btn_next_03)
    private Button mNextBtn;

    private WalletController mWalletController;

    private String type;

    private int count = 59;

    public static void gotoBindCradVCodeActivity(Context context, String phone, String verifyIdentityCode, String type) {
        Intent intent = new Intent(context, BindCradVCodeActivity.class);
        intent.putExtra(PHONE, phone);
        intent.putExtra(IDCODE, verifyIdentityCode);
        intent.putExtra(TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        phone = getIntent().getStringExtra(PHONE);
        verifyIdentityCode = getIntent().getStringExtra(IDCODE);
        type = getIntent().getStringExtra(TYPE);
        mWalletController = new WalletController(this, mHandler);
        mTelTips.setText("本次操作需要短信确认，验证码已发送至手机：" + WalletUtils.formatePhone(phone) + "，请按提示操作。");
        mNextBtn.setBackgroundResource(R.drawable.btn_pay_unselect);
        mNextBtn.setEnabled(false);

        mCodeNum.setPattern(new int[]{6});
        mCodeNum.setSeparator("");
        if (type.equals(WalletUtils.BIND_BANK_CARD)) {
            mNextBtn.setText("确定");
        }else{
            mNextBtn.setText("下一步");
        }

        doSendVerifyCode(type, phone, 1);

        mAuthCodeBtn.setText("获取中(" + count + "s)...");
        mAuthCodeBtn.setEnabled(false);
        mAuthCodeBtn.setBackgroundResource(R.drawable.btn_pay_unselect);
        mHandler.sendEmptyMessageDelayed(TIME, DelayMillis);
        initClick();
    }

    private void initClick() {
        mOrderTopView.setBackClickListener(new WalletTopView.BackClickListener() {
            @Override
            public void back() {
                BindCradVCodeActivity.this.finish();
            }
        });

        mAuthCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSendVerifyCode(type, phone, 1);
                mAuthCodeBtn.setText("获取验证码\n(" + count + ")");
                mAuthCodeBtn.setEnabled(false);
                mAuthCodeBtn.setBackgroundResource(R.drawable.wallet_vcode_btn_background);
                mAuthCodeBtn.setTextColor(getResources().getColor(R.color.neu_999999));
                mHandler.sendEmptyMessageDelayed(TIME, DelayMillis);
            }
        });

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TimeUtil.isFastDoubleClick()) {
                    return;
                }

                mNextBtn.setBackgroundResource(R.drawable.btn_pay_unselect);
                mNextBtn.setEnabled(false);

                String code = mCodeNum.getText().toString();
                doCheckVerifyCode(type, code, verifyIdentityCode);
            }
        });

        mCodeNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    if (s.toString().length() == 6) {
                        mNextBtn.setBackgroundResource(R.drawable.btn_pay_orange_selector);
                        mNextBtn.setEnabled(true);
                    } else {
                        mNextBtn.setBackgroundResource(R.drawable.btn_pay_unselect);
                        mNextBtn.setEnabled(false);
                    }
                } else {
                    mNextBtn.setBackgroundResource(R.drawable.btn_pay_unselect);
                    mNextBtn.setEnabled(false);
                }
            }
        });
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_bindcardvcode, null);
    }

    @Override
    public View onLoadNavView() {
        mOrderTopView = new WalletTopView(this);
        mOrderTopView.setTitle("验证手机号", "安全支付");
        return mOrderTopView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    //发送验证码
    private void doSendVerifyCode(String verifyCodeType, String mobilePhone, int type) {
        mWalletController.doSendVerifyCode(verifyCodeType, mobilePhone, this, type);
    }

    //验证输入的验证码
    private void doCheckVerifyCode(String verifyCodeType, String verifyCode, String verifyIdentityCode) {
        mWalletController.doCheckVerifyCode(verifyCodeType, verifyCode, verifyIdentityCode, BindCradVCodeActivity.this);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case ValueConstants.PAY_SendVerifyCode_SUCCESS:
                PayCoreBaseResult result = (PayCoreBaseResult) msg.obj;
                if (result.success) {

                } else {
                    ToastUtil.showToast(BindCradVCodeActivity.this, result.errorMsg);
                }
                break;
            case ValueConstants.PAY_SendVerifyCode_ERROR:
                ToastUtil.showToast(BindCradVCodeActivity.this, StringUtil.handlerErrorCode(BindCradVCodeActivity.this, msg.arg1));
                break;
            case TIME:
                count--;
                if (count <= 0) {
                    count = 59;
                    mAuthCodeBtn.setText("重获验证码");
                    mAuthCodeBtn.setEnabled(true);
                    mAuthCodeBtn.setBackgroundResource(R.drawable.btn_pay_orange_selector);
                    mAuthCodeBtn.setTextColor(getResources().getColor(R.color.black));
                } else {
                    mAuthCodeBtn.setText("获取验证码\n(" + count + ")");
                    mAuthCodeBtn.setEnabled(false);
                    mAuthCodeBtn.setBackgroundResource(R.drawable.wallet_vcode_btn_background);
                    mAuthCodeBtn.setTextColor(getResources().getColor(R.color.neu_999999));
                    mHandler.sendEmptyMessageDelayed(TIME, DelayMillis);
                }
                break;
            case ValueConstants.PAY_CheckVerifyCode_SUCCESS:
                mNextBtn.setBackgroundResource(R.drawable.btn_pay_orange_selector);
                mNextBtn.setEnabled(true);
                PayCoreBaseResult payCoreBaseResult = (PayCoreBaseResult) msg.obj;
                if (payCoreBaseResult.success) {
                    if (type.equals(WalletUtils.BIND_BANK_CARD)) {
                        ((BaseApplication) getApplication()).exitBindCard();
                        mHandler.sendEmptyMessageDelayed(0x10, 1000);
                    } else {
                        ((BaseApplication) getApplication()).exitForBindCard();
                        NavUtils.gotoSettingPayPassActivity(BindCradVCodeActivity.this, mCodeNum.getText().toString(), verifyIdentityCode, type,"");
                        BindCradVCodeActivity.this.finish();
                    }
                } else {
                    ToastUtil.showToast(BindCradVCodeActivity.this, payCoreBaseResult.errorMsg);
                }
                break;
            case ValueConstants.PAY_CheckVerifyCode_ERROR:
                mNextBtn.setBackgroundResource(R.drawable.btn_pay_orange_selector);
                mNextBtn.setEnabled(true);
                ToastUtil.showToast(BindCradVCodeActivity.this, StringUtil.handlerErrorCode(BindCradVCodeActivity.this, msg.arg1));
                break;
            case 0x10:
                BindCradVCodeActivity.this.finish();
                break;
        }
    }
}
