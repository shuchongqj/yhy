package com.quanyan.yhy.ui.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.LocalUtils;
import com.yhy.common.utils.NetworkUtil;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.SmsType;
import com.quanyan.yhy.service.controller.LoginController;
import com.quanyan.yhy.ui.base.utils.NativeUtils;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.views.LoginInputView;
import com.yhy.common.beans.net.model.NativeBean;
import com.yhy.common.utils.AndroidUtils;
import com.yhy.common.utils.SPUtils;
import com.yhy.network.YhyCallback;
import com.yhy.network.YhyCaller;
import com.yhy.network.YhyCode;
import com.yhy.network.api.snscenter.SnsCenterApi;
import com.yhy.network.req.snscenter.SaveUserCorrelationReq;
import com.yhy.network.resp.Response;
import com.yhy.network.resp.snscenter.SaveUserCorrelationResp;
import com.yhy.router.RouterPath;

/**
 * Created by dengmingjia on 2015/11/3.
 * 登录页面
 */
@Route(path = RouterPath.ACTIVITY_LOGIN)
public class LoginActivity extends BaseActivity implements View.OnClickListener, LoginInputView.OnTextChangeListener {

    public static final int REQ_CODE_THIRD_PARTY_LOGIN = 0x11;
    /**
     * 用户名
     */
    @ViewInject(R.id.phone)
    private LoginInputView phone;
    /**
     * 密码
     */
    @ViewInject(R.id.password)
    private LoginInputView password;
    /**
     * 重置密码
     */
    @ViewInject(R.id.tv_reset_password)
    private TextView btnResetPassword;
    /**
     * 登录
     */
    @ViewInject(R.id.tv_login)
    private TextView tvLogin;
    /**
     * 注册
     */
    @ViewInject(R.id.tv_register)
    private TextView tvRegister;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    /**
     * 登录相关控制器
     */
    private LoginController controller;

    @ViewInject(R.id.tv_mobile_password_login)
    private TextView mAccountLogin;
    @ViewInject(R.id.tv_dynamic_password_login)
    private TextView mDynPWDLogin;

    @ViewInject(R.id.ll_mobile_pwd)
    private LinearLayout mLLMobilePWD;
    @ViewInject(R.id.ll_dn_pwd)
    private LinearLayout mLLDynPWD;

    /**
     * 动态验证码的用户名
     */
    @ViewInject(R.id.phone_dn)
    private LoginInputView mDnPhoneView;
    /**
     * 动态验证码的验证码
     */
    @ViewInject(R.id.password_dn)
    private LoginInputView mDnPWDView;
    //获取动态验证码
    @ViewInject(R.id.tv_get_validate_code)
    private TextView mGetDynCodeView;

    @ViewInject(R.id.ll_text)
    private LinearLayout mLLRegView;
    //外面传来的Action
    private NativeBean mBean;

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_login, null);
    }

    @Override
    public View onLoadNavView() {
        return null;
    }

    @Override
    public boolean isTopCover() {
        return true;
    }

    private YhyCaller saveUserCorrelationCaller;

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        setTitleBarBackground(Color.TRANSPARENT);
        init();
        setListener();

    }

    private void setListener() {
        phone.setOnTextChangeListener(this);
        password.setOnTextChangeListener(this);
        btnResetPassword.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        ivBack.setOnClickListener(this);

//        mAccountLogin.setOnClickListener(this);
//        mDynPWDLogin.setOnClickListener(this);

        mGetDynCodeView.setOnClickListener(this);
        mDnPhoneView.setOnTextChangeListener(this);
        mDnPWDView.setOnTextChangeListener(this);

        findViewById(R.id.tv_left_login).setOnClickListener(this);
        findViewById(R.id.tv_right_login).setOnClickListener(this);
    }

    private void init() {
        mBean = (NativeBean) getIntent().getSerializableExtra(SPUtils.EXTRA_DATA);
        if (controller == null) {
            controller = new LoginController(this, mHandler);
        }
        phone.setMode(LoginInputView.Mode.LOGIN_PHONE);
        password.setMode(LoginInputView.Mode.LOGIN_PASSWORD);
        mDnPhoneView.setMode(LoginInputView.Mode.LOGIN_PHONE);
        mDnPWDView.setMode(LoginInputView.Mode.VALIDATE_CODE);

        mDynPWDLogin.setSelected(true);
        mAccountLogin.setSelected(false);

//        mGetDynCodeView.setEnabled(LocalUtils.isMobileNO(mDnPhoneView.getText()));
    }

    /**
     * 跳转到登录页面
     *
     * @param context
     */
    public static void gotoLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * 登录页面
     *
     * @param context
     */
    public static void gotoLoginActivity(Context context, NativeBean bean) {
        Intent intent = new Intent(context, LoginActivity.class);
        if (bean != null) {
            intent.putExtra(SPUtils.EXTRA_DATA, bean);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == tvLogin.getId()) {
            if (mAccountLogin.isSelected()) {
                //登录

                String phoneNumber = phone.getText();
                String password = this.password.getText();
                //验证手机号，密码

                if (validate(phoneNumber, password)) {
                    showLoadingView(null);
                    tvLogin.setEnabled(false);
                    controller.doLogin(LoginActivity.this, phoneNumber, password);
                }
            } else if (mDynPWDLogin.isSelected()) {
                //动态验证码登录
                String phoneNumber = mDnPhoneView.getText();
                String code = mDnPWDView.getText();

                //验证手机号，验证码
                if (validateCode(phoneNumber, code)) {
                    showLoadingView("");
                    tvLogin.setEnabled(false);
                    controller.doLoginByDynamicCode(LoginActivity.this, phoneNumber, code);
                }
            }
        } else if (id == btnResetPassword.getId()) {
            //找回密码
            NavUtils.gotoFindPasswordActivity(this);
        } else if (id == tvRegister.getId()) {
            //注册
            NavUtils.gotoRegisterActivity(this, LoginController.REQUEST_CODE_REGISTER);
        } else if (id == ivBack.getId()) {
            finish();
            closeKeyboard();
//        } else if (id == mAccountLogin.getId() || id == R.id.tv_right_login) {
        } else if (id == R.id.tv_right_login) {
            //帐号登录
            mLLMobilePWD.setVisibility(View.VISIBLE);
            mLLDynPWD.setVisibility(View.GONE);
            mDynPWDLogin.setSelected(false);
            mAccountLogin.setSelected(true);
            mAccountLogin.setTextColor(getResources().getColor(R.color.white));
            mDynPWDLogin.setTextColor(getResources().getColor(R.color.qun_gray));
            mLLRegView.setVisibility(View.VISIBLE);
//
            phone.setText(mDnPhoneView.getText().toString());
//        } else if (id == mDynPWDLogin.getId() || id == R.id.tv_left_login) {
        } else if (id == R.id.tv_left_login) {
            //动态验证码登录
            mLLMobilePWD.setVisibility(View.GONE);
            mLLDynPWD.setVisibility(View.VISIBLE);
            mDynPWDLogin.setSelected(true);
            mAccountLogin.setSelected(false);
            mDynPWDLogin.setTextColor(getResources().getColor(R.color.white));
            mAccountLogin.setTextColor(getResources().getColor(R.color.qun_gray));
            mLLRegView.setVisibility(View.GONE);

            mDnPhoneView.setText(phone.getText().toString());
//            mGetDynCodeView.setEnabled(LocalUtils.isMobileNO(mDnPhoneView.getText()));
//            mGetDynCodeView.setTextColor(!LocalUtils.isMobileNO(mDnPhoneView.getText())?getResources().getColor(R.color.white):
//                    getResources().getColor(R.color.neu_333333));
            mGetDynCodeView.setTextColor(getResources().getColor(R.color.quan_red));
        } else if (id == mGetDynCodeView.getId()) {
            //获取验证码
            String phoneNumber = mDnPhoneView.getText();
            if (validatePhoneNumber(phoneNumber)) {
                //验证手机号码合理性
                controller.doGetValidateCode(this, phoneNumber, SmsType.LOGIN);
                mLeftTime = 60;
                controller.sendValidateCodeTime(mLeftTime);
                mGetDynCodeView.setEnabled(false);
                mGetDynCodeView.setTextColor(getResources().getColor(R.color.qun_gray));

            }
        }
    }

    /**
     * 验证手机号码是否合乎规则
     *
     * @param phoneNumber
     * @return
     */
    private boolean validatePhoneNumber(String phoneNumber) {
        if (!NetworkUtil.isNetworkAvailable(this)) {
            ToastUtil.showToast(this, getString(R.string.error_network_retry));
            return false;
        }
        if (!LocalUtils.isMobileNO(phoneNumber)) {
            AndroidUtils.showToast(this, getString(R.string.phone_number_error_title));
            return false;
        }
        return true;
    }

    /**
     * 验证用户名密码的正确性
     */
    private boolean validate(String phoneNumber, String password) {
        if (!LocalUtils.isMobileNO(phoneNumber) || password.length() < 6 || password.length() > 20) {
            AndroidUtils.showToast(this, getString(R.string.login_failmsg));
            return false;
        }
        if (!NetworkUtil.isNetworkAvailable(this)) {
            AndroidUtils.showToast(this, getString(R.string.error_network_retry));
            return false;
        }
        return true;
    }

    /**
     * 验证手机号\动态验证码
     *
     * @param phoneNumber  手机号
     * @param validateCode 验证码
     */
    private boolean validateCode(String phoneNumber, String validateCode) {
        if (!NetworkUtil.isNetworkAvailable(this)) {
            AndroidUtils.showToast(this, getString(R.string.error_network_retry));
            return false;
        }
        if (!validatePhoneNumber(phoneNumber))
            return false;

        if (!isVerifyCodeRight(validateCode)) {
            AndroidUtils.showToast(this, R.string.verify_code_error);
            return false;
        }

        return true;
    }

    /**
     * @return
     * @Description 判断是否为6位验证码
     */

    private boolean isVerifyCodeRight(String validateCode) {
        return validateCode.matches("\\d{6}");
    }

    @Override
    public void handleMessage(Message msg) {
        hideLoadingView();
        switch (msg.what) {
            case LoginController.LOGIN_SCUESS:
                synchronizeInterest();
                tvLogin.setEnabled(true);
                TCEventHelper.onEvent(this, AnalyDataValue.USER_LOGIN);
                AndroidUtils.showToast(this, R.string.login_success);
                setResult(RESULT_OK);
                if (mBean != null) {
                    NativeUtils.htmlSkipNative(this, mBean);
                }
                finish();
                closeKeyboard();
                break;
            case LoginController.LOGIN_FAIL:
                tvLogin.setEnabled(true);
                AndroidUtils.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
                break;
            case LoginController.VALIDATE_TIME:
                refreshGetValidateButton(msg.arg1);
                break;
            case LoginController.GET_VALIDATE_CODE_SCUESS:
                AndroidUtils.showToast(this, R.string.verify_code_has_send_string);
//                mLeftTime = 60;
//                controller.sendValidateCodeTime(mLeftTime);
//                mGetDynCodeView.setEnabled(false);
//                mGetDynCodeView.setTextColor(getResources().getColor(R.color.qun_gray));
//                //TODO 焦点自动切换到验证码输入框
                mDnPWDView.setText("");
                break;
            case LoginController.GET_VALIDATE_CODE_FAIL:
                AndroidUtils.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //当注册成功后 关闭登录页
        if (requestCode == LoginController.REQUEST_CODE_REGISTER && resultCode == RESULT_OK) {
            if (mBean != null) {
                NativeUtils.htmlSkipNative(this, mBean);
            }
            finish();
            closeKeyboard();
        } else if (requestCode == REQ_CODE_THIRD_PARTY_LOGIN) {
            if (resultCode == RESULT_OK) {
                if (mBean != null) {
                    NativeUtils.htmlSkipNative(this, mBean);
                }
                finish();
                closeKeyboard();
            } else {
                AndroidUtils.showToast(this, getString(R.string.label_toast_cancel_login));
            }
        }
    }

    /**
     * 刷新获取验证码按钮
     *
     * @param time 单位秒
     */
    private void refreshGetValidateButton(int time) {
        mLeftTime = time;
        if (time > 0) {
            mGetDynCodeView.setEnabled(false);
            mGetDynCodeView.setText(getString(R.string.validate_re_send, time));
            mGetDynCodeView.setTextColor(getResources().getColor(R.color.qun_gray));
            controller.sendValidateCodeTime(time - 1);
        } else {
            mGetDynCodeView.setEnabled(true);
            mGetDynCodeView.setTextColor(getResources().getColor(R.color.quan_red));
            mGetDynCodeView.setText(R.string.req_dynamic_code);
        }
    }

    private int mLeftTime = 0;

    @Override
    public void onTextChange(LoginInputView loginInputView, String text) {
        if (mAccountLogin.isSelected()) {
            tvLogin.setEnabled(LocalUtils.isMobileNO(phone.getText()) && !TextUtils.isEmpty(password.getText()));
        } else if (mDynPWDLogin.isSelected()) {
            if (mLeftTime == 0) {
//                mGetDynCodeView.setEnabled(LocalUtils.isMobileNO(mDnPhoneView.getText()));
//                mGetDynCodeView.setTextColor(!LocalUtils.isMobileNO(mDnPhoneView.getText())?getResources().getColor(R.color.white):
//                        getResources().getColor(R.color.neu_333333));
//                mGetDynCodeView.setTextColor(getResources().getColor(R.color.quan_red));
            }
            if (LocalUtils.isMobileNO(mDnPhoneView.getText()) && !TextUtils.isEmpty(mDnPWDView.getText())) {
                tvLogin.setEnabled(true);
                tvLogin.setTextColor(getResources().getColor(R.color.white));
            } else {
                tvLogin.setEnabled(false);
                tvLogin.setTextColor(getResources().getColor(R.color.white));
            }
        }
    }

    /**
     * 将设备ID对应的兴趣标签与用户ID关联起来
     */
    private void synchronizeInterest() {
        YhyCallback<Response<SaveUserCorrelationResp>> callback = new YhyCallback<Response<SaveUserCorrelationResp>>() {
            @Override
            public void onSuccess(Response<SaveUserCorrelationResp> data) {

            }

            @Override
            public void onFail(YhyCode code, Exception exception) {
            }
        };
        saveUserCorrelationCaller = new SnsCenterApi().saveUserCorrlation(new SaveUserCorrelationReq(null), callback).execAsync();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (saveUserCorrelationCaller != null) {
            saveUserCorrelationCaller.cancel();
            saveUserCorrelationCaller = null;
        }
    }

    private void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
