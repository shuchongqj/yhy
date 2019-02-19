package com.quanyan.yhy.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.newyhy.utils.StringUtils;
import com.newyhy.views.ToastPopuWindow;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.SmsType;
import com.quanyan.yhy.eventbus.EvBusChangePhone;
import com.quanyan.yhy.net.NetManager;
import com.quanyan.yhy.net.lsn.OnResponseListener;
import com.quanyan.yhy.service.controller.LoginController;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.smart.sdk.api.resp.Api_BoolResp;
import com.yhy.common.utils.SPUtils;

import de.greenrobot.event.EventBus;

/**
 * 更换手机号码页面
 * Created by yangboxue on 2018/5/14.
 */

public class ChangePhoneActivity extends BaseActivity implements View.OnClickListener {

    // 当前登录手机号板块
    @ViewInject(R.id.llyt_login_phone)
    private LinearLayout llytLoginPhone;
    // 当前登录手机号
    @ViewInject(R.id.tv_current_phone)
    private TextView tvCurPhone;
    // 变更手机按钮
    @ViewInject(R.id.tv_change_phone)
    private TextView tvChangePhone;

    // 更换手机号板块
    @ViewInject(R.id.llyt_change_phone)
    private LinearLayout llytChangePhone;
    // 手机号输入框
    @ViewInject(R.id.et_phone)
    private EditText etPhone;
    // 手机号码删除按钮
    @ViewInject(R.id.iv_delete_phone)
    private ImageView ivDeletePhone;
    // 手机号信息错误提示
    @ViewInject(R.id.tv_phone_error)
    private TextView tvPhoneError;
    // 验证码输入框
    @ViewInject(R.id.et_code)
    private EditText etCode;
    // 验证码删除按钮
    @ViewInject(R.id.iv_delete)
    private ImageView ivDelete;
    // 获取验证码按钮
    @ViewInject(R.id.tv_get_code)
    private TextView tvGetCode;
    // 验证码错误提示
    @ViewInject(R.id.tv_code_error)
    private TextView tvCodeError;
    // 提交变更手机按钮
    @ViewInject(R.id.tv_confirm)
    private TextView tvConfirm;
    // 手机号是否已绑定标记
    private boolean isNumberExist;

    LoginController controller;

    private BaseNavView mBaseNavView;
    private int type;              //  1是确认登录手机号页面  2是更换手机号页面

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_change_phone, null);
    }

    @Override
    public View onLoadNavView() {
        mBaseNavView = new BaseNavView(this);
        return mBaseNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        EventBus.getDefault().register(this);

        initVars();
        initViews();
        initEvents();
    }

    private void initVars() {
        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getIntExtra("type", 0);
        }

        controller = new LoginController(this, mHandler);
    }

    private void initViews() {
        if (type == 1) {    //  当前登录手机号
            llytLoginPhone.setVisibility(View.VISIBLE);
            llytChangePhone.setVisibility(View.GONE);
            tvCurPhone.setText(StringUtils.transformPhone(SPUtils.getMobilePhone(ChangePhoneActivity.this)));
        } else {            //   更换手机号
            llytLoginPhone.setVisibility(View.GONE);
            llytChangePhone.setVisibility(View.VISIBLE);
        }
        mBaseNavView.setTitleText(type == 1 ? R.string.login_phone_number : R.string.change_phone_number);

    }

    private void initEvents() {
        tvChangePhone.setOnClickListener(this);

        ivDeletePhone.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        tvGetCode.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ivDeletePhone.setVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);
                if (!TextUtils.isEmpty(s) && s.length() > 10) {
                    if (checkPhone(s.toString())) {
                        checkPhoneExiste(s.toString());
                    }
                } else {
                    setErrorView(tvPhoneError, "");
                    isNumberExist = false;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ivDelete.setVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);

                setErrorView(tvCodeError, "");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /**
     * 手机号验证
     *
     * @param phoneNumber
     * @return
     */
    private boolean checkPhone(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            ToastUtil.showToast(ChangePhoneActivity.this, "请输入手机号");
            return false;
        }
        if (LocalUtils.isMobileNO(phoneNumber)) {
            return true;
        } else {
            setErrorView(tvPhoneError, "手机号码错误");
            return false;
        }
    }

    /**
     * 手机号是否已绑定验证
     *
     * @param phoneNumber
     */
    private void checkPhoneExiste(String phoneNumber) {

        NetManager.getInstance(ChangePhoneActivity.this).isMobileExisted(phoneNumber, new OnResponseListener<Api_BoolResp>() {
            @Override
            public void onComplete(boolean isOK, Api_BoolResp result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result.value) {
                        setErrorView(tvPhoneError, "该手机号已被绑定");
                        isNumberExist = true;
                    }
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {

            }
        });
    }

    /**
     * 验证码验证
     *
     * @param code
     * @return
     */
    private boolean checkCode(String code) {
        if (TextUtils.isEmpty(code)) {
            ToastUtil.showToast(ChangePhoneActivity.this, "请输入验证码");
            return false;
        }
        return true;
    }

    /**
     * 设置错误提示
     *
     * @param tv
     * @param error
     */
    private void setErrorView(TextView tv, String error) {
        if (TextUtils.isEmpty(error)) {
            tv.setVisibility(View.INVISIBLE);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(error);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_change_phone:
                ChangePhoneActivity.goToThisActivity(ChangePhoneActivity.this, 2);
                break;
            case R.id.iv_delete_phone:
                etPhone.setText("");
                break;
            case R.id.iv_delete:
                etCode.setText("");
                break;
            case R.id.tv_get_code:
                if (checkPhone(etPhone.getText().toString())) {
                    if (isNumberExist) {
//                        ToastUtil.showToast(ChangePhoneActivity.this, "该手机号已被绑定");
                        return;
                    }
                    tvGetCode.setEnabled(false);
                    controller.doGetValidateCodeByLogin(ChangePhoneActivity.this, etPhone.getText().toString(), SmsType.CHANGE_PHONE);
                }
                break;
            case R.id.tv_confirm:
                if (checkPhone(etPhone.getText().toString()) && checkCode(etCode.getText().toString()))
                    doChangePhone(etPhone.getText().toString(), etCode.getText().toString());
                break;
        }
    }

    /**
     * 变更手机号
     *
     * @param phone
     * @param code
     */
    private void doChangePhone(String phone, String code) {
        NetManager.getInstance(ChangePhoneActivity.this).userChangePhone(phone, code, new OnResponseListener<Api_BoolResp>() {
            @Override
            public void onComplete(boolean isOK, Api_BoolResp result, int errorCode, String errorMsg) {
                if (isOK) {
                    if (result.value) {
                        ToastPopuWindow.makeText(ChangePhoneActivity.this, "更换成功", "请用新手机号登录").show();
                        tvConfirm.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                doLogout();
                                EventBus.getDefault().post(new EvBusChangePhone());
                            }
                        }, 300);
                    }
                } else {
                    if (errorCode == 1015007 || errorCode == -260) {
                        setErrorView(tvCodeError, "验证码错误，请重新输入");
                    } else {
                        ToastUtil.showToast(ChangePhoneActivity.this, errorMsg);
                    }
                }
            }

            @Override
            public void onInternError(int errorCode, String errorMessage) {

            }
        });
    }

    /**
     * 推出登录
     */
    private void doLogout() {
        LocalUtils.JumpToLogin(this);
        NetManager.getInstance(this).changeLogoutStatus();
        setResult(RESULT_OK);
        NavUtils.gotoLoginActivity(this);
        this.finish();
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case LoginController.VALIDATE_TIME:
                refreshGetValidateButton(msg.arg1);
                break;
            case LoginController.GET_VALIDATE_CODE_SCUESS:
                controller.sendValidateCodeTime(60);

                ToastUtil.showToast(this, R.string.verify_code_has_send_string);
//                mLeftTime = 60;
//                controller.sendValidateCodeTime(mLeftTime);
//                mGetDynCodeView.setEnabled(false);
//                mGetDynCodeView.setTextColor(getResources().getColor(R.color.qun_gray));
//                //TODO 焦点自动切换到验证码输入框
                etCode.setText("");
                break;
            case LoginController.GET_VALIDATE_CODE_FAIL:
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
                tvGetCode.setEnabled(true);

                break;
//            case LoginController.GET_RESET_PASSWORD_SUCESS:
//                ToastUtil.showToast(this, getString(R.string.reset_password_success));
//                finish();
//                doLogout();
//                break;
//            case LoginController.GET_RESET_PASSWORD_FAIL:
//                ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
//                break;
        }
    }

    public void onEvent(EvBusChangePhone event) {
        finish();
    }

    /**
     * 刷新获取验证码按钮
     *
     * @param time 单位秒
     */
    private void refreshGetValidateButton(int time) {
        if (time > 0) {
            tvGetCode.setEnabled(false);
            tvGetCode.setText(getString(R.string.validate_re_send, time));
            tvGetCode.setTextColor(getResources().getColor(R.color.qun_gray));
            controller.sendValidateCodeTime(time - 1);
        } else {
            tvGetCode.setEnabled(true);
            tvGetCode.setText(R.string.req_dynamic_code);
            tvGetCode.setTextColor(getResources().getColor(R.color.neu_666666));
        }
    }

    public static void goToThisActivity(Context context, int type) {
        Intent intent = new Intent(context, ChangePhoneActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

}
