package com.quanyan.yhy.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.harwkin.nb.camera.TimeUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.LocalUtils;
import com.yhy.common.utils.NetworkUtil;
import com.quanyan.base.util.RegexUtil;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.SmsType;
import com.quanyan.yhy.service.controller.LoginController;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.views.LoginInputView;
import com.yhy.common.utils.SPUtils;

/**
 * Created by dengmingjia on 2015/11/3.
 * 注册 找回密码同用页
 */
public class RegisterFindPasswrodActivity extends BaseActivity implements View.OnClickListener, LoginInputView.OnTextChangeListener {
    private static final String TYPE_ACTIVITY = "type";
    /**
     * 页面跳转类型 注册
     */
    private static final int TYPE_REGISTER = 1;
    /**
     * 页面跳转类型 注册
     */
    private static final int TYPE_FIND_PASSWORD = 2;
    /**
     * 当前页面类型
     */
    private Integer typeActivity;
    /**
     * 手机号
     */
    @ViewInject(R.id.phone)
    private LoginInputView phone;

    /**
     * 验证码
     */
    @ViewInject(R.id.validate_code)
    private LoginInputView validateCode;

    /**
     * 密码输入
     */
    @ViewInject(R.id.password)
    private LoginInputView password;

    /**
     * 昵称
     */
//    @ViewInject(R.id.nickname)
//    private LoginInputView nickname;

    /**
     * 获取验证码按钮
     */
    @ViewInject(R.id.tv_get_validate_code)
    private TextView btGetValidateCode;
    /**
     * 请求分发按钮
     */
    @ViewInject(R.id.tv_dispatch)
    private TextView tvDispatch;
    @ViewInject(R.id.tv_agree)
    private TextView cbAgree;
    @ViewInject(R.id.iv_back)
    private ImageView ivBack;
    @ViewInject(R.id.title)
    private TextView tvTitle;
    private LoginController controller;

    /**
     * 跳转到注册界面
     */
    public static void gotoRegisterActivity(Activity context, int requestCode) {
        gotoActivity(TYPE_REGISTER, context, requestCode);
    }

    /**
     * 跳转到注册界面
     */
    public static void gotoRegisterActivity(Context context) {
        Intent intent = new Intent(context, RegisterFindPasswrodActivity.class);
        intent.putExtra(TYPE_ACTIVITY, TYPE_REGISTER);
        context.startActivity(intent);
    }

    /**
     * 跳转到注册界面
     */
    public static void gotoFindPasswordActivity(Activity context) {
        gotoActivity(TYPE_FIND_PASSWORD, context, -1);
    }

    private static void gotoActivity(int type, Activity context, int requestCode) {
        Intent intent = new Intent(context, RegisterFindPasswrodActivity.class);
        intent.putExtra(TYPE_ACTIVITY, type);
        if (type == TYPE_REGISTER) {
            context.startActivityForResult(intent, requestCode);
        } else {
            context.startActivity(intent);
        }
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_register_find_password, null);
    }

    @Override
    public View onLoadNavView() {
        return null;
    }

    @Override
    public boolean isTopCover() {
        return true;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);

        setTitleBarBackground(Color.TRANSPARENT);
    }
    @Override
    protected void onStart() {
        super.onStart();
        init();
        setListener();
    }

    /**
     * 初始化
     */
    private void init() {
        if (controller == null) {
            controller = new LoginController(this, mHandler);
            boolean isTypeRegister = isRegisterType();
            tvDispatch.setText(isTypeRegister ? R.string.register : R.string.commit);
            //tvDispatch.setText(R.string.register);
            phone.setMode(isTypeRegister ? LoginInputView.Mode.REGISTER_PHONE : LoginInputView.Mode.FIND_PASSWORD_PHONE);
            validateCode.setMode(LoginInputView.Mode.VALIDATE_CODE);
            password.setMode(isTypeRegister ? LoginInputView.Mode.REGISTER_PWD : LoginInputView.Mode.FIND_PASSWORD_PWD);
//            nickname.setMode(LoginInputView.Mode.NICK_NAME);
//            nickname.setVisibility(isTypeRegister ? View.VISIBLE : View.GONE);
            cbAgree.setVisibility(isTypeRegister ? View.VISIBLE : View.GONE);
            tvTitle.setText(isTypeRegister ? R.string.register : R.string.pwd_get);
            //tvTitle.setText(R.string.register);
            if (isTypeRegister) {
                initAgree();
            }

        }
    }

    //注册服务条款
    private void initAgree() {
        cbAgree.append(getString(R.string.register_bottom_before));
        String str = getString(R.string.register_agree_hint);
        SpannableString spannableString = new SpannableString(str);
        //UnderlineSpan span = new UnderlineSpan();
        //spannableString.setSpan(span, 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ClickableSpan() {

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.qun_gray));  //设置文件颜色
                ds.setUnderlineText(true); //设置下划线
            }

            @Override
            public void onClick(View widget) {
                if (SPUtils.getServiceProtocol(RegisterFindPasswrodActivity.this) != null) {
                    //WebParams wp = new WebParams();
                    //wp.setUrl(ImageUtils.getH5FullUrl(SPUtils.getServiceProtocol(RegisterFindPasswrodActivity.this)));
                    NavUtils.startWebview(RegisterFindPasswrodActivity.this, "",SPUtils.getServiceProtocol(RegisterFindPasswrodActivity.this), 10001);
                } else {
                    ToastUtil.showToast(RegisterFindPasswrodActivity.this, getString(R.string.toast_server_sys_config_error));
                }
            }
        }, 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        cbAgree.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
        cbAgree.append(spannableString);
        cbAgree.setMovementMethod(LinkMovementMethod.getInstance());//响应点击事件

    }

    /**
     * 设置监听
     */
    private void setListener() {
        btGetValidateCode.setOnClickListener(this);
        tvDispatch.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        phone.setOnTextChangeListener(this);
        validateCode.setOnTextChangeListener(this);
//        nickname.setOnTextChangeListener(this);
        password.setOnTextChangeListener(this);
    }

    /**
     * 判断是否为注册类型
     */
    private boolean isRegisterType() {
        if (typeActivity == null)
            typeActivity = getIntent().getIntExtra(TYPE_ACTIVITY, TYPE_REGISTER);
        return typeActivity == TYPE_REGISTER;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == btGetValidateCode.getId()) {
            if (isRegisterType()) {
//                TCEventHelper.onEvent(this, AnalyDataValue.REGISTER_REQ_DYNCOD);
            } else {
//                TCEventHelper.onEvent(this, AnalyDataValue.REPWD_REQ_DYNCOD);
            }
            //获取验证码
            //String phoneNumber = phone.getText().toString();
            String phoneNumber = phone.getText();
            if (validatePhoneNumber(phoneNumber)) {
                //验证手机号码合理性
                controller.doGetValidateCode(RegisterFindPasswrodActivity.this,phoneNumber,isRegisterType()? SmsType.REGISTER:SmsType.RETRIVE_PASSWORD);
                controller.sendValidateCodeTime(60);
//            btGetValidateCode.setTextColor(getResources().getColor(R.color.qun_gray));
                btGetValidateCode.setEnabled(false);
            }
        } else if (id == tvDispatch.getId()) {

            if (TimeUtil.isFastDoubleClick()) {
                return;
            }
            String phoneNumber = phone.getText();
            String code = validateCode.getText();
            String pwd = password.getText();
//            String name = nickname.getText();
            if (validate(phoneNumber, code, pwd, null)) {
                if (isRegisterType()) {
                    controller.doRegister(RegisterFindPasswrodActivity.this,phoneNumber, code, pwd, null);
                } else {
                    controller.doResetPassword(RegisterFindPasswrodActivity.this,phoneNumber, code, pwd);
                }
            }
        } else if (id == ivBack.getId()) {
            finish();
        }
    }

    /**
     * 验证输入框信息
     *
     * @param phoneNumber  手机号
     * @param validateCode 验证码
     * @param password     密码
     */
    private boolean validate(String phoneNumber, String validateCode, String password, String nickName) {
        if (!NetworkUtil.isNetworkAvailable(this)) {
            ToastUtil.showToast(this, getString(R.string.error_network_retry));
            return false;
        }
        if (!validatePhoneNumber(phoneNumber))
            return false;

        if (!isVerifyCodeRight(validateCode)) {
            ToastUtil.showToast(this, R.string.verify_code_error);
            return false;
        }

        if (!RegexUtil.isPassword(password)) {
            ToastUtil.showToast(this, getString(R.string.password_error_retry));
            return false;
        }

        /*if (password.length() < 6 || password.length() > 16) {
            ToastUtil.showToast(this, getString(R.string.password_error_retry));
            return false;
        }*/
//        if (isRegisterType() && (nickName.length() < 2 || nickName.length() > 15)) {
//            ToastUtil.showToast(this, getString(R.string.hint_input_nickname));
//            return false;
//        }
        return true;
    }

    /**
     * @return
     * @Description 判断是否为6位验证码
     */

    private boolean isVerifyCodeRight(String validateCode) {
        return validateCode.matches("\\d{6}");
    }


    private boolean validatePhoneNumber(String phoneNumber) {
        if (!NetworkUtil.isNetworkAvailable(this)) {
            ToastUtil.showToast(this, getString(R.string.error_network_retry));
            return false;
        }
        if (!LocalUtils.isMobileNO(phoneNumber)) {
            ToastUtil.showToast(this, getString(R.string.phone_number_error_title));
            return false;
        }
        return true;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        int what = msg.what;
        if (what == LoginController.VALIDATE_TIME) {
            refreshGetValidateButton(msg.arg1);
        } else if (what == LoginController.GET_VALIDATE_CODE_SCUESS) {
            ToastUtil.showToast(this, R.string.verify_code_has_send_string);
//            controller.sendValidateCodeTime(60);
////            btGetValidateCode.setTextColor(getResources().getColor(R.color.qun_gray));
//            btGetValidateCode.setEnabled(false);
        } else if (what == LoginController.GET_VALIDATE_CODE_FAIL) {
            ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
        } else if (what == LoginController.GET_REGISTER_SUCESS) {
            ToastUtil.showToast(this, R.string.register_success);
//            NavUtils.gotoWelcomeActivity(this);
            setResult(RESULT_OK);
            finish();
        } else if (what == LoginController.GET_REGISTER_FAIL) {
            ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
        } else if (what == LoginController.GET_RESET_PASSWORD_SUCESS) {
            ToastUtil.showToast(this, R.string.reset_password_success);
            finish();
        } else if (what == LoginController.GET_RESET_PASSWORD_FAIL) {
            ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
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
            btGetValidateCode.setEnabled(false);
            btGetValidateCode.setText(time + "s"/*getString(R.string.validate_re_send, time)*/);
            btGetValidateCode.setTextColor(getResources().getColor(R.color.qun_gray));
            controller.sendValidateCodeTime(time - 1);
        } else {
            btGetValidateCode.setEnabled(true);
            btGetValidateCode.setText(R.string.req_dynamic_code);
            btGetValidateCode.setTextColor(getResources().getColor(R.color.quan_red));
        }
    }
    private int mLeftTime = 0;
    @Override
    public void onTextChange(LoginInputView loginInputView, String text) {
        if (mLeftTime == 0) {
//            btGetValidateCode.setEnabled(LocalUtils.isMobileNO(phone.getText()));
        }
        if (isRegisterType()) {
            tvDispatch.setEnabled(!TextUtils.isEmpty(password.getText()) && LocalUtils.isMobileNO(phone.getText()) && !TextUtils.isEmpty(validateCode.getText()));
        } else {
            tvDispatch.setEnabled(!TextUtils.isEmpty(password.getText()) && LocalUtils.isMobileNO(phone.getText()) && !TextUtils.isEmpty(validateCode.getText()));
        }
    }
}
