package com.quanyan.yhy.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.LocalUtils;
import com.yhy.common.utils.NetworkUtil;
import com.quanyan.base.util.RegexUtil;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.SmsType;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.service.controller.LoginController;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.yhy.common.utils.SPUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 个人资料中修改密码的找回密码Activity
 * Created by Jiervs on 2018/4/27.
 */

public class SettingFindPasswordActivity extends BaseActivity implements View.OnClickListener {

    //Phone
    @ViewInject(R.id.et_phone)
    private EditText et_phone;
    //Phone
    @ViewInject(R.id.et_code)
    private EditText et_code;
    //获取验证码
    @ViewInject(R.id.tv_get_validate_code)
    private TextView tv_get_validate_code;
    //新密码
    @ViewInject(R.id.et_password)
    private EditText et_password;
    //眼睛
    @ViewInject(R.id.iv_eye)
    private ImageView iv_eye;
    LoginController controller;

    public static void goSettingFindPasswordActivity(Context context, int requestCode) {
        Intent intent = new Intent(context, SettingFindPasswordActivity.class);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_find_password_setting, null);
    }

    private BaseNavView mBaseNavView;

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText(R.string.label_find_pwd);
        mBaseNavView.setRightText(getString(R.string.label_btn_submit));
        mBaseNavView.setRightTextClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提交
                String phoneNumber = et_phone.getText().toString().trim();
                String code = et_code.getText().toString().trim();
                String pwd = et_password.getText().toString().trim();
                if (validate(phoneNumber, code, pwd, null))
                    controller.doResetPassword(SettingFindPasswordActivity.this, phoneNumber, code, pwd);
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
        init();
        setListener();
        et_phone.setText(SPUtils.getMobilePhone(this));
    }

    private void init() {
        if (controller == null) {
            controller = new LoginController(this, mHandler);
            et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            et_password.addTextChangedListener(new MTextWatcher(et_password));
        }
    }

    private void setListener() {
        iv_eye.setOnClickListener(this);
        tv_get_validate_code.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_get_validate_code:
                String phoneNumber = et_phone.getText().toString().trim();
                if (validatePhoneNumber(phoneNumber)) {
                    //验证手机号码合理性
//                    controller.doGetValidateCode(this,phoneNumber,SmsType.RETRIVE_PASSWORD);
                    controller.doGetValidateCodeByLogin(this, "", SmsType.RETRIVE_PASSWORD_BY_LOGIN);
                    controller.sendValidateCodeTime(60);
                    tv_get_validate_code.setEnabled(false);
                }
                break;

            case R.id.iv_eye:
                if (!StringUtil.isEmpty(et_password.getText().toString())) {
                    if (et_password.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                        et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        iv_eye.setImageResource(R.mipmap.ic_change_password_eye);
                    } else {
                        et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        iv_eye.setImageResource(R.mipmap.ic_normal_password_eye);
                    }
                    et_password.setSelection(et_password.getText().toString().length());
                }
                break;
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

    public class MTextWatcher implements android.text.TextWatcher {
        EditText editText;

        public MTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String editable = editText.getText().toString();
            String str = stringFilter(editable.toString());
            if (!editable.equals(str)) {
                editText.setText(str);
                //设置新的光标所在位置
                editText.setSelection(str.length());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

        private String regEx = "[^a-zA-Z0-9]";

        public String stringFilter(String str) throws PatternSyntaxException {
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            return m.replaceAll("").trim();
        }
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case LoginController.VALIDATE_TIME:
                refreshGetValidateButton(msg.arg1);
                break;
            case LoginController.GET_RESET_PASSWORD_SUCESS:
                ToastUtil.showToast(this, getString(R.string.reset_password_success));
                finish();
                doLogout();
                break;
            case LoginController.GET_RESET_PASSWORD_FAIL:
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
                break;
        }
    }

    private void doLogout() {
        LocalUtils.JumpToLogin(this);
        NetManager.getInstance(this).changeLogoutStatus();
        setResult(RESULT_OK);
        NavUtils.gotoLoginActivity(this);
        this.finish();
    }

    /**
     * 刷新获取验证码按钮
     *
     * @param time 单位秒
     */
    private int mLeftTime = 0;

    private void refreshGetValidateButton(int time) {
        mLeftTime = time;
        if (time > 0) {
            tv_get_validate_code.setEnabled(false);
            tv_get_validate_code.setText(getString(R.string.validate_re_send, time));
            tv_get_validate_code.setTextColor(getResources().getColor(R.color.qun_gray));
            controller.sendValidateCodeTime(time - 1);
        } else {
            tv_get_validate_code.setEnabled(true);
            tv_get_validate_code.setText(R.string.req_dynamic_code);
            tv_get_validate_code.setTextColor(getResources().getColor(R.color.quan_red));
        }
    }
}
