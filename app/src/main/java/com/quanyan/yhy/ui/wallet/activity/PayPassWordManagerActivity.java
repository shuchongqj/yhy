package com.quanyan.yhy.ui.wallet.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.wallet.Controller.WalletController;
import com.quanyan.yhy.ui.wallet.WalletUtils;
import com.quanyan.yhy.ui.wallet.view.WalletTopView;
import com.yhy.common.beans.net.model.paycore.EleAccountInfo;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;

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
public class PayPassWordManagerActivity extends BaseActivity {

    private WalletTopView mOrderTopView;
    @ViewInject(R.id.rl_update_pas)
    private RelativeLayout mUpdatePas;
    @ViewInject(R.id.rl_forget_pas)
    private RelativeLayout mForgetPas;

    private WalletController mWalletController;
    private boolean isAuth = false;
    private String type;

    private Dialog mWalletDialog;

    private boolean isPerson = false;

    private boolean isCompany = false;

    public static void gotoPassWordManagerActivity(Context context) {
        Intent intent = new Intent(context, PassWordManagerActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        mWalletController = new WalletController(this, mHandler);
        initClick();
    }

    private void initClick() {
        mOrderTopView.setBackClickListener(new WalletTopView.BackClickListener() {
            @Override
            public void back() {
                PayPassWordManagerActivity.this.finish();
            }
        });

        mUpdatePas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPerson) {
                    if (!isAuth) {
                     //   mWalletDialog = WalletDialog.showIdentificationDialog(PayPassWordManagerActivity.this, type);

                        NavUtils.gotoRealNameAuthActivity(PayPassWordManagerActivity.this, type,"");
                    } else {
                        NavUtils.gotoUpdatePassWordActivity(PayPassWordManagerActivity.this);
                    }
                } else {
                    if(isCompany){
                        ToastUtil.showToast(PayPassWordManagerActivity.this, "企业用户请登录商户中心查看钱包信息");
                    }else {
                        ToastUtil.showToast(PayPassWordManagerActivity.this, "网络异常,请稍候重试");
                    }
                }
            }
        });
        mForgetPas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPerson) {
                    if (!isAuth) {
                       // mWalletDialog = WalletDialog.showIdentificationDialog(PayPassWordManagerActivity.this, type);\
                        NavUtils.gotoRealNameAuthActivity(PayPassWordManagerActivity.this, type,"");
                    } else {
                        NavUtils.gotoForgetPasSelectCardActivity(PayPassWordManagerActivity.this);
                    }
                } else {
                    if(isCompany){
                        ToastUtil.showToast(PayPassWordManagerActivity.this, "企业用户请登录商户中心查看钱包信息");
                    }else {
                        ToastUtil.showToast(PayPassWordManagerActivity.this, "网络异常,请稍候重试");
                    }
                }
            }
        });
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_password_manager, null);
    }

    @Override
    public View onLoadNavView() {
        mOrderTopView = new WalletTopView(this);
        mOrderTopView.setTitle("支付密码管理", null);
        return mOrderTopView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWalletDialog != null) {
            mWalletDialog.dismiss();
        }
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
                ToastUtil.showToast(PayPassWordManagerActivity.this, StringUtil.handlerErrorCode(PayPassWordManagerActivity.this, msg.arg1));
                break;
        }
    }

    private void handMsg(EleAccountInfo info) {
        if (info == null) {
            isPerson = true;
            isAuth = false;
            type = WalletUtils.OPEN_ELE_ACCOUNT;
        } else {
            if (!TextUtils.isEmpty(info.accountType)) {
                if (info.accountType.equals(WalletUtils.PERSON)) {
                    isPerson = true;
                    if (!TextUtils.isEmpty(info.status)) {
                        if (info.status.equals(WalletUtils.TACK_EFFECT)) {
                            if (info.existPayPwd) {
                                isAuth = true;
                                if (!TextUtils.isEmpty(info.userName)) {
                                    SPUtils.saveWalletName(PayPassWordManagerActivity.this, info.userName);
                                }
                            } else {
                                type = WalletUtils.SETUP_PAY_PWD;
                                isAuth = false;
                            }
                        } else {
                            type = WalletUtils.OPEN_ELE_ACCOUNT;
                            isAuth = false;
                        }
                    } else {
                        type = WalletUtils.OPEN_ELE_ACCOUNT;
                        isAuth = false;
                    }
                } else if(info.accountType.equals(WalletUtils.COMPANY)) {
                    isCompany = true;
                    ToastUtil.showToast(this, "企业用户请登录商户中心查看钱包信息");
                    PayPassWordManagerActivity.this.finish();
                }else{
                    ToastUtil.showToast(this, "参数错误");
                    PayPassWordManagerActivity.this.finish();
                }
            } else {
                ToastUtil.showToast(this, "参数错误");
                PayPassWordManagerActivity.this.finish();
            }
        }
    }
}
