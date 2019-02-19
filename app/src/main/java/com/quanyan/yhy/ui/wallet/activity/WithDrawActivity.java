package com.quanyan.yhy.ui.wallet.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.MD5Utils;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.wallet.Controller.WalletController;
import com.quanyan.yhy.ui.wallet.InputTools;
import com.quanyan.yhy.ui.wallet.WalletUtils;
import com.quanyan.yhy.ui.wallet.view.WalletTopView;
import com.quanyan.yhy.ui.wallet.view.gridpasswordview.GridPasswordView;
import com.yhy.common.beans.net.model.paycore.BankCard;
import com.yhy.common.beans.net.model.paycore.BankCardList;
import com.yhy.common.beans.net.model.paycore.PayCoreBaseResult;
import com.yhy.common.beans.net.model.paycore.WithdrawParam;
import com.yhy.common.constants.ValueConstants;

/**
 * Created with Android Studio.
 * Title:WithDrawActivity
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-8-10
 * Time:14:13
 * Version 1.0
 * Description: 提现
 */
public class WithDrawActivity extends BaseActivity {

    private static final String BALACE = "BALACE";
    private static final int PAGE = 1;
    private static final int MAX = 1;

    private WalletTopView mOrderTopView;
    @ViewInject(R.id.select_card_layout)
    private RelativeLayout mSelectCardLayout;
    @ViewInject(R.id.tv_card_type)
    private TextView mCardType;
    @ViewInject(R.id.et_withdraw_num)
    private EditText mWithDrawNum;
    @ViewInject(R.id.tv_remain_sum)
    private TextView mRemainSum;
    @ViewInject(R.id.tv_all_withdraw)
    private TextView mAllWithDraw;
    @ViewInject(R.id.btn_config)
    private Button mConfigBtn;

    private Dialog mPayDialog;

    private long balance;

    private WalletController mWalletController;
    private BankCard mBankCard;

    private BankCard mBankCardCallback=null;
    boolean isObtainData = true;

    public static void gotoWithDrawActivity(Context context, long balance) {
        Intent intent = new Intent(context, WithDrawActivity.class);
        intent.putExtra(BALACE, balance);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        balance = getIntent().getLongExtra(BALACE, 0);
        mWalletController = new WalletController(this, mHandler);
//        WalletUtils.setPricePoint(mWithDrawNum);
        mConfigBtn.setBackgroundResource(R.drawable.btn_pay_unselect);
        mConfigBtn.setEnabled(false);
        mRemainSum.setText("零钱余额" + StringUtil.formatWalletMoney(this, balance) + "，");


        mRemainSum.setTextColor(getResources().getColor(R.color.neu_999999));
        mAllWithDraw.setVisibility(View.VISIBLE);
        initClick();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mWithDrawNum.requestFocus();
        InputTools.KeyBoard(mWithDrawNum, "open");
        if (mBankCardCallback==null){
            getUserBindBankCard();

        }
    }

    private void initClick() {
        mOrderTopView.setBackClickListener(new WalletTopView.BackClickListener() {
            @Override
            public void back() {
                WithDrawActivity.this.finish();
            }
        });

        mConfigBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBankCard == null) {
                    ToastUtil.showToast(WithDrawActivity.this, "请先选择银行卡");
                } else {
                    double a = Double.parseDouble(mWithDrawNum.getText().toString());
                    if (a <= balance) {
                        mPayDialog = showPayDialog(WithDrawActivity.this, WalletUtils.formateMoney(a), mBankCard);
                    } else {
                        ToastUtil.showToast(WithDrawActivity.this, "超过可提现金额");
                    }
                }
            }
        });

        mSelectCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.gotoSelectCardActivity(WithDrawActivity.this, 0x11, mBankCard);
            }
        });

        mWithDrawNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        mWithDrawNum.setText(s);
                        mWithDrawNum.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    mWithDrawNum.setText(s);
                    mWithDrawNum.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        mWithDrawNum.setText(s.subSequence(0, 1));
                        mWithDrawNum.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(mWithDrawNum.getText().toString())) {
                    double a = Double.parseDouble(mWithDrawNum.getText().toString());
                    long b = WalletUtils.formateMoney(a);
                    if (b > 0) {
                        if (b <= balance) {
                            mRemainSum.setText("零钱余额" + StringUtil.formatWalletMoney(WithDrawActivity.this, balance) + "，");
                            mRemainSum.setTextColor(getResources().getColor(R.color.neu_999999));
                            mAllWithDraw.setVisibility(View.VISIBLE);
                            mConfigBtn.setBackgroundResource(R.drawable.btn_pay_orange_selector);
                            mConfigBtn.setEnabled(true);
                        } else {
                            mRemainSum.setText("提现金额已超出当前余额");
                            mRemainSum.setTextColor(getResources().getColor(R.color.main));
                            mAllWithDraw.setVisibility(View.GONE);
                            mConfigBtn.setBackgroundResource(R.drawable.btn_pay_unselect);
                            mConfigBtn.setEnabled(false);
                        }
                    } else {
                        mRemainSum.setText("零钱余额" + StringUtil.formatWalletMoney(WithDrawActivity.this, balance) + "，");
                        mRemainSum.setTextColor(getResources().getColor(R.color.neu_999999));
                        mAllWithDraw.setVisibility(View.VISIBLE);
                        mConfigBtn.setBackgroundResource(R.drawable.btn_pay_unselect);
                        mConfigBtn.setEnabled(false);
                    }
                } else {
                    mRemainSum.setText("零钱余额" + StringUtil.formatWalletMoney(WithDrawActivity.this, balance) + "，");
                    mRemainSum.setTextColor(getResources().getColor(R.color.neu_999999));
                    mAllWithDraw.setVisibility(View.VISIBLE);
                    mConfigBtn.setBackgroundResource(R.drawable.btn_pay_unselect);
                    mConfigBtn.setEnabled(false);
                }
            }
        });

        mAllWithDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWithDrawNum.setText(StringUtil.formatWalletMoneyNoFlg(balance)
                );

                mWithDrawNum.setSelection(mWithDrawNum.length());
            }
        });
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_withdraw, null);
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

    private void getUserBindBankCard() {
        mWalletController.doPageQueryUserBindBankCard(PAGE, MAX, this);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case ValueConstants.PAY_PageQueryUserBindBankCard_SUCCESS:
                BankCardList mBanks = (BankCardList) msg.obj;
                if (mBanks != null && mBanks.bankCardList != null && mBanks.bankCardList.size() != 0) {
                    mBankCard = mBanks.bankCardList.get(0);
                    handleData(mBankCard);
                } else {
                    mCardType.setText("请选择到账银行卡");
                }
                break;
            case ValueConstants.PAY_PageQueryUserBindBankCard_ERROR:
                mCardType.setText("请选择到账银行卡");
                break;

            case ValueConstants.PAY_Withdraw_SUCCESS://提现
                hideLoadingView();
                PayCoreBaseResult result = (PayCoreBaseResult) msg.obj;
                if (result != null) {
                    if (result.success) {
                        double a = Double.parseDouble(mWithDrawNum.getText().toString());
                        NavUtils.gotoWithDrawDetailsActivity(this, mBankCard, WalletUtils.formateMoney(a));
                        WithDrawActivity.this.finish();
                    } else {
                        if (result.errorCode.equals(WalletUtils.PAY_PWD_ERROR)) {
                            showPassErrorDialog("支付密码错误，请重试");
                        } else if (result.errorCode.equals(WalletUtils.PAY_PWD_MORE_THAN_MAXIMUM_RETRIES)) {
                            showSurPassErrorDialog("支付密码输入错误过多账户已被锁定，请点击忘记密码进行找回或10分钟后重试");
                        } else {
                            ToastUtil.showToast(WithDrawActivity.this, "提现失败");
                        }
                    }
                } else {
                    ToastUtil.showToast(WithDrawActivity.this, "提现失败");
                }
                break;
            case ValueConstants.PAY_Withdraw_ERROR:
                hideLoadingView();
                ToastUtil.showToast(WithDrawActivity.this, StringUtil.handlerErrorCode(WithDrawActivity.this, msg.arg1));
                break;
            case 0x110:
                WithdrawParam param = (WithdrawParam) msg.obj;
                mWalletController.doWithdraw(param, WithDrawActivity.this);
                if (mPayDialog != null && mPayDialog.isShowing()) {
                    mPayDialog.dismiss();
                }
                showLoadingView("正在支付，请稍候...");
                break;
        }
    }

    private void handleData(BankCard mBankCard) {
        if (mBankCard != null && !TextUtils.isEmpty(mBankCard.bankName) && !TextUtils.isEmpty(mBankCard.bankCardNo)) {
            String no = mBankCard.bankCardNo;
            if (no.length() > 4) {
                mCardType.setText(mBankCard.bankName + "(" + no.substring(no.length() - 4, no.length()) + ")");
            } else {
                mCardType.setText(mBankCard.bankName);
            }
        } else {
            mCardType.setText("请选择到账银行卡");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0x11:
                    mBankCard = (BankCard) data.getSerializableExtra("BankCard");
                    mBankCardCallback= (BankCard) data.getSerializableExtra("BankCard");
                    handleData(mBankCard);
                    break;
            }
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
                            double a = Double.parseDouble(mWithDrawNum.getText().toString());
                            mPayDialog = showPayDialog(WithDrawActivity.this, WalletUtils.formateMoney(a), mBankCard);
                        }
                    },
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mErrorDilog != null) {
                                mErrorDilog.dismiss();
                            }
                            NavUtils.gotoForgetPasSelectCardActivity(WithDrawActivity.this);
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
                            NavUtils.gotoForgetPasSelectCardActivity(WithDrawActivity.this);
                        }
                    });
        }
        mSurErrorDialog.show();
    }

    /**
     * 显示提现输入金额dialog
     *
     * @param context
     * @return
     */
    public Dialog showPayDialog(final Context context, final long sub, final BankCard mBankCard) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        View view = mLayoutInflater.inflate(R.layout.pay_pass_dialog, null);
        final Dialog dialog = new Dialog(context, R.style.kangzai_dialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        final GridPasswordView mCustomUi = (GridPasswordView) view.findViewById(R.id.gpv_customUi);
        TextView mMoney = (TextView) view.findViewById(R.id.tv_money);
        mMoney.setText(StringUtil.formatWalletMoneyNoFlg(sub));
        mCustomUi.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
                if (!TextUtils.isEmpty(psw) && psw.length() == 6) {
                    WalletUtils.hideSoft(context, mCustomUi.getWindowToken());
                    WithdrawParam param = new WithdrawParam();
                    param.bindCardId = mBankCard.bindCardId;
                    param.payPwd = MD5Utils.toMD5(psw);
                    param.withdrawAmount = sub;


                    Message msg = Message.obtain();
                    msg.obj = param;
                    msg.what = 0x110;
                    mHandler.sendMessageDelayed(msg, 500);
                }
            }

            @Override
            public void onInputFinish(String psw) {

            }
        });

        dialog.show();
        return dialog;
    }
}
