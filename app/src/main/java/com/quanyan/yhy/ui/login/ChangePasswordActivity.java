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
import com.quanyan.base.util.RegexUtil;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.service.controller.LoginController;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.smart.sdk.api.request.ApiCode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by dengmingjia on 2015/11/3.
 * 修改密码界面
 */
public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 老密码
     */
    @ViewInject(R.id.et_old_password)
    private EditText etOldPassword;
    /**
     * 新密码
     */
    @ViewInject(R.id.et_new_password)
    private EditText etNewPassword;
    /**
     * 眼睛旧密码
     */
    @ViewInject(R.id.iv_eye_old_password)
    private ImageView eyeOld;
    /**
     * 眼睛新密码
     */
    @ViewInject(R.id.iv_eye_new_password)
    private ImageView eyeNew;
    /**
     * 忘记密码
     */
    @ViewInject(R.id.tv_forget_pas)
    private TextView tv_forget_pas;
    LoginController controller;

    public static void goChangePasswordActivity(Context context, int requestCode) {
        Intent intent = new Intent(context, ChangePasswordActivity.class);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_change_password, null);
    }

    private BaseNavView mBaseNavView;
    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        mBaseNavView.setTitleText(R.string.label_change_pwd);
        mBaseNavView.setRightText(getString(R.string.label_btn_finish));
        mBaseNavView.setRightTextClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = etOldPassword.getText().toString();
                String newPassword = etNewPassword.getText().toString();
                if (validate(oldPassword, newPassword)) {
                    controller.doChangePassword(ChangePasswordActivity.this,oldPassword, newPassword);
                }
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
        setListener();
    }

    private void init() {
        if (controller == null) {
            controller = new LoginController(this, mHandler);
            etNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            etOldPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            etNewPassword.addTextChangedListener(new MTextWatcher(etNewPassword));
            etOldPassword.addTextChangedListener(new MTextWatcher(etOldPassword));
        }
    }

    private void setListener() {
        eyeOld.setOnClickListener(this);
        eyeNew.setOnClickListener(this);
        tv_forget_pas.setOnClickListener(this);
    }

    /**
     * 验证edittext数据合理性
     *
     * @return
     */
    private boolean validate(String oldPassword, String newPassword) {

        /*if(!RegexTool.isPassword(oldPassword)){
            ToastUtil.showToast(this, R.string.old_pwd_error);
            return false;
        }*/
        if (oldPassword.length() < 6) {
            ToastUtil.showToast(this, R.string.old_pwd_error);
        }
        if (!RegexUtil.isPassword(newPassword)) {
            ToastUtil.showToast(this, R.string.password_error_retry);
            return false;
        }

        /*if (newPassword.length() < 6) {
            ToastUtil.showToast(this, R.string.new_pwd_lenth_error);
            return false;
        }*/

        if (oldPassword.equals(newPassword)) {
            ToastUtil.showToast(this, R.string.same_pwd);
            return false;
        }
        return true;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        int what = msg.what;
        if (what == LoginController.GET_CHANGE_PASSWORD_SUCESS) {
            ToastUtil.showToast(this, R.string.change_password_success);
            doLogout();
        } else if (what == LoginController.GET_CHANGE_PASSWORD_FAIL) {
//            System.out.println("ApiContext" + msg.arg1);
            if(ApiCode.USERNAME_OR_PASSWORD_ERROR_1003010 == msg.arg1){
                ToastUtil.showToast(this,R.string.old_pwd_error);
            }else {
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
            }
        }
    }

    private void doLogout() {
        LocalUtils.JumpToLogin(ChangePasswordActivity.this);
        NetManager.getInstance(ChangePasswordActivity.this).changeLogoutStatus();
        setResult(RESULT_OK);
        NavUtils.gotoLoginActivity(this);
        this.finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.iv_eye_old_password:
                if(!StringUtil.isEmpty(etOldPassword.getText().toString())){
                    //eyeOld.setImageResource(R.drawable.ic_change_password_eye);
                    if(etOldPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD){
                        etOldPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        eyeOld.setImageResource(R.mipmap.ic_change_password_eye);
                    }else {
                        etOldPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        eyeOld.setImageResource(R.mipmap.ic_normal_password_eye);//ic_normal_password_eye
                    }

                    etOldPassword.setSelection(etOldPassword.getText().toString().length());
                }

                break;
            case R.id.iv_eye_new_password:
                if(!StringUtil.isEmpty(etNewPassword.getText().toString())){
                    //eyeOld.setImageResource(R.drawable.ic_change_password_eye);
                    if(etNewPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD){
                        etNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        eyeNew.setImageResource(R.mipmap.ic_change_password_eye);
                    }else {
                        etNewPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        eyeNew.setImageResource(R.mipmap.ic_normal_password_eye);
                    }
                    etNewPassword.setSelection(etNewPassword.getText().toString().length());
                }

                break;

            case R.id.tv_forget_pas:
                NavUtils.goSettingFindPasswordActivity(ChangePasswordActivity.this,0);
                break;
        }

        /*if (id == eyeNew.getId() || id == eyeOld.getId()) {

            EditText editText = null;
            if (id == eyeNew.getId()) {
                editText = etNewPassword;
                if(StringUtil.isEmpty(editText.getText().toString())){
                    eyeNew.setImageResource(R.drawable.ic_change_password_eye);
                }
            } else {
                editText = etOldPassword;
                if(StringUtil.isEmpty(editText.getText().toString())){
                    eyeOld.setImageResource(R.drawable.ic_change_password_eye);
                }
            }
            int inputType = editText.getInputType();
            if (inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                if(id == eyeOld.getId()){
                    eyeOld.setImageResource(R.drawable.ic_normal_password_eye);
                }else if(id == eyeNew.getId()){
                    eyeNew.setImageResource(R.drawable.ic_normal_password_eye);
                }
            } else {
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                if(id == eyeOld.getId()){
                    eyeOld.setImageResource(R.drawable.ic_change_password_eye);
                }else if(id == eyeNew.getId()){
                    eyeNew.setImageResource(R.drawable.ic_change_password_eye);
                }
            }
            editText.setSelection(editText.getText().toString().length());
        }*/
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
}
