package com.quanyan.yhy.ui.wallet.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.baseenum.IActionTitleBar;
import com.quanyan.base.util.StringUtil;
import com.quanyan.base.yminterface.ErrorViewClick;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.ErrorCode;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.PrefUtil;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.wallet.Controller.WalletController;
import com.quanyan.yhy.ui.wallet.WalletUtils;
import com.quanyan.yhy.ui.wallet.view.WalletDialog;
import com.quanyan.yhy.ui.wallet.view.WalletTopView;
import com.yhy.common.beans.net.model.paycore.EleAccountInfo;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;

/**
 * Created with Android Studio.
 * Title:WalletActivity
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-8-9
 * Time:14:51
 * Version 1.0
 * Description:
 */
public class WalletActivity extends BaseActivity {

    private WalletTopView mOrderTopView;

    private Dialog mWalletDialog;

    @ViewInject(R.id.tv_money)
    private TextView mMoney;
    @ViewInject(R.id.btn_recharge)
    private Button mRechargeBtn;
    @ViewInject(R.id.btn_withdraw)
    private Button mWithDrawBtn;
    @ViewInject(R.id.tv_manage_pass)
    private TextView mManagePass;

    private WalletController mWalletController;
    private boolean isAuth = false;
    private String type;
    private boolean isPerson = false;
    private boolean isCompany = false;
    private long accountBalance = 0;
    private final static int MSG_GET_ORDER_DETAIL = 0x1001;


    public static void gotoWalletActivity(Context context) {
        Intent intent = new Intent(context, WalletActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        mWalletController = new WalletController(this, mHandler);
        initClick();
//        getUserPayMsg();


    }


    private void initClick() {
        mOrderTopView.setBackClickListener(new WalletTopView.BackClickListener() {
            @Override
            public void back() {
                WalletActivity.this.finish();
            }
        });

        mOrderTopView.setRightClickListener(new WalletTopView.RightClickListener() {
            @Override
            public void rightClick() {
                if (isPerson) {
                    if (!isAuth) {
                      //  mWalletDialog = WalletDialog.showIdentificationDialog(WalletActivity.this, type);
                    } else {
                        NavUtils.gotoPrepaidOutAndInListActivity(WalletActivity.this);
                    }
                } else {
                    if (isCompany) {
                        ToastUtil.showToast(WalletActivity.this, "企业用户请登录商户中心查看钱包信息");
                    } else {
                        ToastUtil.showToast(WalletActivity.this, "网络异常，请稍候重试");
                    }
                }
            }
        });

        mRechargeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPerson) {
                    if (!isAuth) {
                   //     mWalletDialog = WalletDialog.showIdentificationDialog(WalletActivity.this, type);
                    } else {
                        NavUtils.gotoRechargeActivity(WalletActivity.this, 0x10);
                    }
                } else {
                    if (isCompany) {
                        ToastUtil.showToast(WalletActivity.this, "企业用户请登录商户中心查看钱包信息");
                    } else {
                        ToastUtil.showToast(WalletActivity.this, "网络异常，请稍候重试");
                    }
                }
            }
        });

        mWithDrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPerson) {
                    if (!isAuth) {
                     //   mWalletDialog = WalletDialog.showIdentificationDialog(WalletActivity.this, type);
                    } else {
                        NavUtils.gotoWithDrawActivity(WalletActivity.this, accountBalance);
                    }
                } else {
                    if (isCompany) {
                        ToastUtil.showToast(WalletActivity.this, "企业用户请登录商户中心查看钱包信息");
                    } else {
                        ToastUtil.showToast(WalletActivity.this, "网络异常，请稍候重试");
                    }
                }
            }
        });

        mManagePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPerson) {
                    if (!isAuth) {
                        mWalletDialog = WalletDialog.showIdentificationDialog(WalletActivity.this, type);
                    } else {
                        NavUtils.gotoPassWordManagerActivity(WalletActivity.this);
                    }
                } else {
                    if (isCompany) {
                        ToastUtil.showToast(WalletActivity.this, "企业用户请登录商户中心查看钱包信息");
                    } else {
                        ToastUtil.showToast(WalletActivity.this, "网络异常，请稍候重试");
                    }
                }
            }
        });
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_wallet, null);
    }

    @Override
    public View onLoadNavView() {
        mOrderTopView = new WalletTopView(this);
        mOrderTopView.setTitle("零钱", "安全支付");
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
        showLoadingView("正在获取个人电子钱包信息，请稍候...");
        mWalletController.doGetEleAccountInfo(this);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case ValueConstants.PAY_GETELEACCOUNTINFO_SUCCESS:
                hideLoadingView();
                hideErrorView(null);
                EleAccountInfo info = (EleAccountInfo) msg.obj;
                mOrderTopView.setRightTextView("零钱明细");
                handMsg(info);
                break;
            case ValueConstants.PAY_GETELEACCOUNTINFO_ERROR:
                hideLoadingView();
                hideErrorView(null);
//                ToastUtil.showToast(WalletActivity.this, StringUtil.handlerErrorCode(WalletActivity.this, msg.arg1));
                showNetErrorView(msg.arg1);
                break;
            case 0x10:
                mWalletController.doGetEleAccountInfo(this);
                break;

        }
    }

    private void showNetErrorView(int arg1) {
        showErrorView(null, ErrorCode.NETWORK_UNAVAILABLE == arg1 ?
                IActionTitleBar.ErrorType.NETUNAVAILABLE : IActionTitleBar.ErrorType.ERRORNET, "", "", "", new ErrorViewClick() {
            @Override
            public void onClick(View view) {
                getUserPayMsg();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0x10:
                    ToastUtil.showToast(this, "资金到账可能存在延迟，请稍后刷新");
                    mHandler.sendEmptyMessageDelayed(0x10, 3000);
                    break;
            }
        }
    }

    private void handMsg(EleAccountInfo info) {
        if (info == null) {
            isAuth = false;
            isPerson = true;
            type = WalletUtils.OPEN_ELE_ACCOUNT;
            mMoney.setText("￥0.00");

        } else {
            isUploadIdcard(info);
            if (!TextUtils.isEmpty(info.accountType)) {
                if (info.accountType.equals(WalletUtils.PERSON)) {
                    isPerson = true;
                    if (!TextUtils.isEmpty(info.status)) {
                        if (info.status.equals(WalletUtils.TACK_EFFECT)) {
                            if (info.existPayPwd) {
                                isAuth = true;
                                if (!TextUtils.isEmpty(info.userName)) {
                                    SPUtils.saveWalletName(WalletActivity.this, info.userName);
                                }
                            } else {
                                type = WalletUtils.SETUP_PAY_PWD;
                                isAuth = false;
                            }
                        } else {
                            type = WalletUtils.OPEN_ELE_ACCOUNT;
                            isAuth = false;
                        }
                        accountBalance = info.accountBalance;
                        mMoney.setText(StringUtil.formatWalletMoney(this, info.accountBalance));
                    } else {
                        mMoney.setText("￥0.00");
                        type = WalletUtils.OPEN_ELE_ACCOUNT;
                        isAuth = false;
                    }
                } else if (info.accountType.equals(WalletUtils.COMPANY)) {
                    isCompany = true;
//                    ToastUtil.showToast(this, "企业用户请登录商户中心查看钱包信息");
//                    WalletActivity.this.finish();
                } else {
//                    ToastUtil.showToast(this, "参数错误");
//                    WalletActivity.this.finish();
                }
            } else {
//                ToastUtil.showToast(this, "参数错误");
//                WalletActivity.this.finish();
            }
        }
    }


    public void isUploadIdcard(EleAccountInfo info) {

        if ("NOT_UPLOAD".equals(info.idCardPhotoStatus)) {
            if ("YES".equals(info.isForcedUploadPhoto)) {
                showDialog(info, true);
            } else {
                if (PrefUtil.getIsFirstStart(WalletActivity.this)) {
                    showDialog(info, false);
                    PrefUtil.putIsFirstStart(WalletActivity.this, false);
                }
            }
        }

    }

    public void showDialog(final EleAccountInfo info, final boolean isFocus) {
        WalletDialog.showUptateIdentityDialog(this, info.uploadMsg, isFocus, new WalletDialog.IdentifyInterface() {
            @Override
            public void OnIdentifyConfirm() {
                NavUtils.gotoIdCardUploadActivity(WalletActivity.this, info.userName);
            }

            @Override
            public void OnIdentifyCancel() {
                if (isFocus) {
                    finish();
                }
            }
        });

    }

}
