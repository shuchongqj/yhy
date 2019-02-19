package com.quanyan.yhy.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.harwkin.nb.camera.TimeUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.LocalUtils;
import com.yhy.common.utils.NetworkUtil;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.view.BaseNavView;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.AnalyDataValue;
import com.quanyan.yhy.common.SmsType;
import com.quanyan.yhy.service.controller.LoginController;
import com.quanyan.yhy.ui.base.utils.TCEventHelper;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.views.LoginInputView;
import com.smart.sdk.api.request.ApiCode;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;

/**
 * 绑定手机号码
 */
public class BindMobilePhoneActivity extends BaseActivity implements View.OnClickListener, LoginInputView.OnTextChangeListener {
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
     * 获取验证码按钮
     */
    @ViewInject(R.id.tv_get_validate_code)
    private TextView btGetValidateCode;
    /**
     * 请求分发按钮
     */
    @ViewInject(R.id.tv_dispatch)
    private TextView tvDispatch;
    private LoginController controller;

    private String mUnionId;
    private String mOpenId;
    private String mAccessToken;
    private int mType;
    private String mStrType;
    /**
     * 绑定手机号
     * @param context
     * @param accessToken
     * @param type
     * @param reqCode
     */
    public static void gotoBindMobilePhoneActivity(Activity context, String unionId, String openId,String accessToken, int type, int reqCode) {
        Intent intent = new Intent(context,BindMobilePhoneActivity.class);
        if(!StringUtil.isEmpty(openId)) {
            intent.putExtra(SPUtils.EXTRA_OPEN_ID, openId);
        }
        if(!StringUtil.isEmpty(unionId)) {
            intent.putExtra(SPUtils.EXTRA_UNION_ID, unionId);
        }
        if(!StringUtil.isEmpty(accessToken)) {
            intent.putExtra(SPUtils.KEY_EXTRA_ACCESSTOKEN, accessToken);
        }
        intent.putExtra(SPUtils.EXTRA_TYPE, type);
        context.startActivityForResult(intent,reqCode);
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        mOpenId = getIntent().getStringExtra(SPUtils.EXTRA_OPEN_ID);
        mUnionId = getIntent().getStringExtra(SPUtils.EXTRA_UNION_ID);
        mAccessToken = getIntent().getStringExtra(SPUtils.KEY_EXTRA_ACCESSTOKEN);
        mType = getIntent().getIntExtra(SPUtils.EXTRA_TYPE,-1);
        if(mType == SHARE_MEDIA.SINA.ordinal()){
            mStrType = SmsType.THIRD_PARTY_TYPE_WEIBO;
        }else if(mType == SHARE_MEDIA.QQ.ordinal()){
            mStrType = SmsType.THIRD_PARTY_TYPE_QQ;
        }else if(mType == SHARE_MEDIA.WEIXIN.ordinal()){
            mStrType = SmsType.THIRD_PARTY_TYPE_WX;
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        phone.getInputBox().setHintTextColor(getResources().getColor(R.color.neu_999999));
        phone.get86TextView().setTextColor(getResources().getColor(R.color.neu_333333));
        phone.getInputBox().setTextColor(getResources().getColor(R.color.neu_333333));
        phone.getLineTextView().setBackgroundColor(getResources().getColor(R.color.neu_999999));

        validateCode.getInputBox().setHintTextColor(getResources().getColor(R.color.neu_999999));
        validateCode.getInputBox().setTextColor(getResources().getColor(R.color.neu_333333));
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
        }
        tvDispatch.setText(R.string.label_btn_ok);
        phone.setMode(LoginInputView.Mode.LOGIN_PHONE);
        phone.updateDeleteIcon(R.mipmap.ic_clear_black);
        validateCode.setMode(LoginInputView.Mode.VALIDATE_CODE);
    }

    /**
     * 设置监听
     */
    private void setListener() {
        btGetValidateCode.setOnClickListener(this);
        tvDispatch.setOnClickListener(this);
        phone.setOnTextChangeListener(this);
        validateCode.setOnTextChangeListener(this);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == btGetValidateCode.getId()) {
            //获取验证码
            String phoneNumber = phone.getText();
            if (validatePhoneNumber(phoneNumber)) {
                //验证手机号码合理性
                controller.doGetValidateCode(BindMobilePhoneActivity.this,phoneNumber,SmsType.BIND_MOBILE_PHONE);
            }
        } else if (id == tvDispatch.getId()) {
            if (TimeUtil.isFastDoubleClick()) {
                return;
            }
            String phoneNumber = phone.getText();
            String code = validateCode.getText();
            if (validate(phoneNumber, code)) {
                controller.doBindPhone(BindMobilePhoneActivity.this,phoneNumber, code,mUnionId,mOpenId,mStrType,mAccessToken);
            }
        }
    }

    /**
     * 验证输入框信息
     *
     * @param phoneNumber  手机号
     * @param validateCode 验证码
     */
    private boolean validate(String phoneNumber, String validateCode) {
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
            mLeftTime = 60;
            controller.sendValidateCodeTime(mLeftTime);
            btGetValidateCode.setEnabled(false);
        } else if (what == LoginController.GET_VALIDATE_CODE_FAIL) {
            ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
        } else if (what == ValueConstants.MSG_BIND_MOBILE_OK) {
            tcEvent();
            ToastUtil.showToast(this, R.string.toast_bind_mobile_ok);
            setResult(RESULT_OK);
            finish();
        } else if (what == ValueConstants.MSG_BIND_MOBILE_KO) {
            if(msg.arg1 == ApiCode.USER_NOT_FOUND_1003020){
                ToastUtil.showToast(this, R.string.toast_mobile_has_bound);
            }else {
                ToastUtil.showToast(this, StringUtil.handlerErrorCode(this, msg.arg1));
            }
        }
    }

    private void tcEvent() {
        if(!StringUtil.isEmpty(mStrType)){
            if(mStrType.equals(SmsType.THIRD_PARTY_TYPE_WX)){
                TCEventHelper.onEvent(BindMobilePhoneActivity.this, AnalyDataValue.THREE_PARTY_LOGIN, "WEIXIN");
            }else {
                TCEventHelper.onEvent(BindMobilePhoneActivity.this, AnalyDataValue.THREE_PARTY_LOGIN, mStrType);
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
            btGetValidateCode.setEnabled(false);
            btGetValidateCode.setText(getString(R.string.validate_re_send, time));
            controller.sendValidateCodeTime(time - 1);
        } else {
            btGetValidateCode.setEnabled(true);
            btGetValidateCode.setText(R.string.req_dynamic_code);
        }
    }
    private int mLeftTime = 0;
    @Override
    public void onTextChange(LoginInputView loginInputView, String text) {
        if(mLeftTime == 0) {
            btGetValidateCode.setEnabled(LocalUtils.isMobileNO(phone.getText()));
        }
        if(LocalUtils.isMobileNO(phone.getText()) && !TextUtils.isEmpty(validateCode.getText())){
            tvDispatch.setEnabled(true);
            tvDispatch.setTextColor(getResources().getColor(R.color.black));
        }else{
            tvDispatch.setEnabled(false);
            tvDispatch.setTextColor(getResources().getColor(R.color.transparent_black_50));
        }
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.ac_bind_mobile_phone, null);
    }

    BaseNavView mNavView;
    @Override
    public View onLoadNavView() {
        mNavView = new BaseNavView(this);
        mNavView.setTitleText(getString(R.string.label_title_bind_mobile));
        return mNavView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }
}
