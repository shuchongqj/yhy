package com.quanyan.yhy.ui.wallet.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.wallet.Controller.WalletController;
import com.quanyan.yhy.ui.wallet.WalletUtils;
import com.quanyan.yhy.ui.wallet.view.WalletTopView;
import com.quanyan.yhy.ui.wallet.view.gridpasswordview.GridPasswordView;
import com.yhy.common.beans.net.model.paycore.PayCoreBaseResult;
import com.yhy.common.constants.ValueConstants;

/**
 * Created with Android Studio.
 * Title:VerifyPassWordActivity
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-8-10
 * Time:16:43
 * Version 1.0
 * Description: 验证支付密码
 */
public class VerifyPassWordActivity extends BaseActivity {

    private WalletTopView mOrderTopView;
    @ViewInject(R.id.tv_forget_pas)
    private TextView mForgetPas;
    @ViewInject(R.id.gpv_normal)
    private GridPasswordView gpvCustomUi;

    private WalletController mWalletController;

    public static void gotoVerifyPassWordActivity(Context context) {
        Intent intent = new Intent(context, VerifyPassWordActivity.class);
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
                VerifyPassWordActivity.this.finish();
            }
        });

        mForgetPas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.gotoBindCardActivity(VerifyPassWordActivity.this, 2);
            }
        });

        gpvCustomUi.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
                if (psw.length() == 6) {
                    WalletUtils.hideSoft(VerifyPassWordActivity.this, gpvCustomUi.getWindowToken());
                    doVerifyPayPwd(psw);
                }
            }

            @Override
            public void onInputFinish(String s) {

            }
        });
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_verifypassword, null);
    }

    @Override
    public View onLoadNavView() {
        mOrderTopView = new WalletTopView(this);
        mOrderTopView.setTitle("提现", "安全支付");
        mOrderTopView.setRBackVisibility();
        return mOrderTopView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    private void doVerifyPayPwd(String pas) {
        mWalletController.doVerifyPayPwd(pas, this);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case ValueConstants.PAY_VerifyPayPwd_SUCCESS:
                PayCoreBaseResult result = (PayCoreBaseResult) msg.obj;
                if (result != null) {
                    if (result.success) {
                        NavUtils.gotoBindCardActivity(VerifyPassWordActivity.this, 1);
                        VerifyPassWordActivity.this.finish();
                    } else {
                        if (result.errorCode.equals(WalletUtils.PAY_PWD_ERROR)) {
                            showPassErrorDialog("支付密码错误，请重试");
                        } else if (result.errorCode.equals(WalletUtils.PAY_PWD_MORE_THAN_MAXIMUM_RETRIES)) {
                            showSurPassErrorDialog("支付密码输入错误过多账户已被锁定，请点击忘记密码进行找回或10分钟后重试");
                        } else {
                            ToastUtil.showToast(this, result.errorMsg);
                        }
                    }
                } else {
                    ToastUtil.showToast(this, "参数错误");
                }
                break;
            case ValueConstants.PAY_VerifyPayPwd_ERROR:
                ToastUtil.showToast(VerifyPassWordActivity.this, StringUtil.handlerErrorCode(VerifyPassWordActivity.this, msg.arg1));
                break;
        }
    }

    /**
     * 显示输入密码错误弹框
     */

    private Dialog mErrorDilog;

    private void showPassErrorDialog(String msg) {
        if (mErrorDilog == null) {
            mErrorDilog = DialogUtil.showMessageDialog(this,
                    null,
                    msg,
                    "重试",
                    "忘记密码",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mErrorDilog != null) {
                                mErrorDilog.dismiss();
                            }
                            gpvCustomUi.clearPassword();
                        }
                    },
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mErrorDilog != null) {
                                mErrorDilog.dismiss();
                            }
                            NavUtils.gotoForgetPasSelectCardActivity(VerifyPassWordActivity.this);
                        }
                    });
        }
        mErrorDilog.show();
    }

    private Dialog mSurErrorDialog;

    private void showSurPassErrorDialog(String msg) {
        if (mSurErrorDialog == null) {
            mSurErrorDialog = DialogUtil.showMessageDialog(this,
                    null,
                    msg,
                    "取消",
                    "忘记密码",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mSurErrorDialog != null) {
                                mSurErrorDialog.dismiss();
                            }
                        }
                    },
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mSurErrorDialog != null) {
                                mSurErrorDialog.dismiss();
                            }
                            NavUtils.gotoForgetPasSelectCardActivity(VerifyPassWordActivity.this);
                        }
                    });
        }
        mSurErrorDialog.show();
    }
}
