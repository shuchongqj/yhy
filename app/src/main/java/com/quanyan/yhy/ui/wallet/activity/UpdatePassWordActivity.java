package com.quanyan.yhy.ui.wallet.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.harwkin.nb.camera.TimeUtil;
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
 * Title:UpdatePassWordActivity
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-8-11
 * Time:14:07
 * Version 1.0
 * Description:
 */
public class UpdatePassWordActivity extends BaseActivity {

    private WalletTopView mOrderTopView;

    @ViewInject(R.id.tv_title)
    private TextView mTitle;
    @ViewInject(R.id.gpv_customUi)
    private GridPasswordView mCustomUi;

    @ViewInject(R.id.ll_error_tips)
    private LinearLayout mErrorTips;
    @ViewInject(R.id.btn_config)
    private Button mConfigBtn;

    private String firstPwd;
    private int count = 1;

    private WalletController mWalletController;

    private String oldPas;

    public static void gotoUpdatePassWordActivity(Context context) {
        Intent intent = new Intent(context, UpdatePassWordActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        mTitle.setText("请输入支付密码，以验证身份");
        mWalletController = new WalletController(this, mHandler);
        mConfigBtn.setBackgroundResource(R.drawable.btn_pay_unselect);
        mConfigBtn.setEnabled(false);
        initClick();
    }


    private void initClick() {
        mOrderTopView.setBackClickListener(new WalletTopView.BackClickListener() {
            @Override
            public void back() {

                showBack();
            }
        });

        mCustomUi.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(final String psw) {
                if (psw.length() == 6 && count == 1) {
                    oldPas = psw;
                    doVerifyPayPwd(psw);
                } else if (psw.length() == 6 && count == 2) {
                    Message msg = Message.obtain();
                    msg.what = 0x10;
                    msg.obj = psw;
                    mHandler.sendMessageDelayed(msg, 300);
                } else if (psw.length() == 6 && count == 3) {
                    if (psw.equals(firstPwd)) {
                        mConfigBtn.setBackgroundResource(R.drawable.btn_pay_orange_selector);
                        mConfigBtn.setEnabled(true);
                        //doUpdatePayPwd(oldPas, firstPwd);
                    } else {
                        mHandler.sendEmptyMessageDelayed(0x11, 300);
                    }
                }
            }

            @Override
            public void onInputFinish(String psw) {

            }
        });

        //完成按钮
        mConfigBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TimeUtil.isFastDoubleClick()) {
                    return;
                }
                doUpdatePayPwd(oldPas, firstPwd);
            }
        });
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(UpdatePassWordActivity.this, R.layout.activity_updatepass, null);
    }

    @Override
    public View onLoadNavView() {
        mOrderTopView = new WalletTopView(this);
        mOrderTopView.setTitle("修改密码", "安全支付");
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


    private void doUpdatePayPwd(String oldpas, String newPas) {
        if (oldpas.equals(newPas)) {
            showSampleDialog();
        } else {
            showLoadingView("正在修改支付密码,请稍候");
            mWalletController.doUpdatePayPwd(oldpas, newPas, this);
        }
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case ValueConstants.PAY_VerifyPayPwd_SUCCESS:
                PayCoreBaseResult result = (PayCoreBaseResult) msg.obj;
                if (result != null) {
                    if (result.success) {
                        count++;
                        mCustomUi.clearPassword();
                        mTitle.setText("请设置新的支付密码，用于支付验证");
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
                ToastUtil.showToast(UpdatePassWordActivity.this, StringUtil.handlerErrorCode(UpdatePassWordActivity.this, msg.arg1));
                break;
            case ValueConstants.PAY_UpdatePayPwd_SUCCESS:
                hideLoadingView();
                PayCoreBaseResult payCoreBaseResult = (PayCoreBaseResult) msg.obj;
                if (payCoreBaseResult != null) {
                    if (payCoreBaseResult.success) {
                        ToastUtil.showToast(UpdatePassWordActivity.this, "支付密码修改成功");
                        WalletUtils.hideSoft(UpdatePassWordActivity.this, mCustomUi.getWindowToken());
                        UpdatePassWordActivity.this.finish();
                    } else {
                        count = 2;
                        if (mConfigBtn != null && mConfigBtn.getVisibility() == View.VISIBLE) {
                            mConfigBtn.setVisibility(View.GONE);
                        }
                        mCustomUi.clearPassword();
                        mTitle.setText("请设置新的支付密码，用于支付验证");
                        ToastUtil.showToast(this, payCoreBaseResult.errorMsg);
                    }
                } else {
                    count = 2;
                    if (mConfigBtn != null && mConfigBtn.getVisibility() == View.VISIBLE) {
                        mConfigBtn.setVisibility(View.GONE);
                    }
                    mCustomUi.clearPassword();
                    mTitle.setText("请设置新的支付密码，用于支付验证");
                    ToastUtil.showToast(this, "参数错误，修改失败");
                }
                break;
            case ValueConstants.PAY_UpdatePayPwd_ERROR:
                hideLoadingView();
                count = 2;
                if (mConfigBtn != null && mConfigBtn.getVisibility() == View.VISIBLE) {
                    mConfigBtn.setVisibility(View.GONE);
                }
                mCustomUi.clearPassword();
                mTitle.setText("请设置新的支付密码，用于支付验证");
                ToastUtil.showToast(UpdatePassWordActivity.this, StringUtil.handlerErrorCode(UpdatePassWordActivity.this, msg.arg1));
                break;
            case 0x10:
                String psw = (String) msg.obj;

                count++;
                firstPwd = psw;
                if (mErrorTips != null && mErrorTips.getVisibility() == View.VISIBLE) {
                    mErrorTips.setVisibility(View.GONE);
                }
                if (mConfigBtn != null && mConfigBtn.getVisibility() == View.GONE) {
                    mConfigBtn.setVisibility(View.VISIBLE);
                    mConfigBtn.setBackgroundResource(R.drawable.btn_pay_unselect);
                    mConfigBtn.setEnabled(false);
                }
                mCustomUi.clearPassword();
                mTitle.setText("请再次填写以确认");
                break;
            case 0x11:
                count = 2;
                if (mErrorTips != null && mErrorTips.getVisibility() == View.GONE) {
                    mErrorTips.setVisibility(View.VISIBLE);
                }
                if (mConfigBtn != null && mConfigBtn.getVisibility() == View.VISIBLE) {
                    mConfigBtn.setVisibility(View.GONE);
                }
                mCustomUi.clearPassword();
                mTitle.setText("请设置新的支付密码，用于支付验证");
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
                            mCustomUi.clearPassword();
                            WalletUtils.showSoft(UpdatePassWordActivity.this);
                        }
                    },
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mErrorDilog != null) {
                                mErrorDilog.dismiss();
                            }
                            NavUtils.gotoForgetPasSelectCardActivity(UpdatePassWordActivity.this);
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
                            NavUtils.gotoForgetPasSelectCardActivity(UpdatePassWordActivity.this);
                        }
                    });
        }
        mSurErrorDialog.show();
    }

    private Dialog mSampleDialog;

    private void showSampleDialog() {
        if (mSampleDialog == null) {
            mSampleDialog = DialogUtil.showMessageDialog(this,
                    null,
                    "您设置的新密码与老密码相同，请重新输入",
                    "确定",
                    null,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mSampleDialog != null) {
                                mSampleDialog.dismiss();
                            }
                            count = 2;
                            if (mConfigBtn != null && mConfigBtn.getVisibility() == View.VISIBLE) {
                                mConfigBtn.setVisibility(View.GONE);
                            }
                            mCustomUi.clearPassword();
                            mTitle.setText("请设置新的支付密码，用于支付验证");
                        }
                    },
                    null);
        }
        mSampleDialog.setCanceledOnTouchOutside(false);
        mSampleDialog.show();
    }


    private Dialog mBackDialog;

    private void showBack() {
        if (mBackDialog == null) {
            mBackDialog = DialogUtil.showMessageDialog(this,
                    null,
                    "是否放弃修改支付密码",
                    "是",
                    "否",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mBackDialog != null) {
                                mBackDialog.dismiss();
                            }
                            UpdatePassWordActivity.this.finish();
                        }
                    },
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mBackDialog != null) {
                                mBackDialog.dismiss();
                            }
                        }
                    });
        }
        mBackDialog.show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showBack();
        }
        return false;
    }
}
