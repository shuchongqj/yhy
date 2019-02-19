package com.quanyan.yhy.ui.wallet.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.eventbus.GuangDaPaySuccessEvBus;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.wallet.Controller.WalletController;
import com.quanyan.yhy.ui.wallet.InputTools;
import com.quanyan.yhy.ui.wallet.WalletUtils;
import com.quanyan.yhy.ui.wallet.view.WalletTopView;
import com.yhy.common.beans.net.model.param.WebParams;
import com.yhy.common.beans.net.model.paycore.PcPayResult;
import com.yhy.common.beans.net.model.paycore.RechargeResult;
import com.yhy.common.constants.ValueConstants;

import de.greenrobot.event.EventBus;

/**
 * Created with Android Studio.
 * Title:RechargeActivity
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-8-10
 * Time:14:15
 * Version 1.0
 * Description: 充值界面
 */
public class RechargeActivity extends BaseActivity {

    private WalletTopView mOrderTopView;

    @ViewInject(R.id.et_card_num)
    private EditText mRechargeNum;
    @ViewInject(R.id.btn_config)
    private Button mConfigBtn;

    @ViewInject(R.id.include_02)
    private View mInclude02;
    @ViewInject(R.id.include_01)
    private View mInclude01;

    @ViewInject(R.id.iv_pay_status)
    private ImageView mPayStatusImg;
    @ViewInject(R.id.tv_pay_status)
    private TextView mPayStatusTV;
    @ViewInject(R.id.tv_recharge_money)
    private TextView mRechargeMoney;
    @ViewInject(R.id.btn_pay)
    private Button mPay;
    @ViewInject(R.id.tv_recharge_money_title)
    private TextView mRechargeMoneyTitle;

    private WalletController mWalletController;

    private long payOrderId = 0;

    private boolean isRecharge = false;

    private int count = 0;
    private static final int MAXCOUNT = 5;

    public static void gotoRechargeActivity(Activity context, int reqCode) {
        Intent intent = new Intent(context, RechargeActivity.class);
        context.startActivityForResult(intent, reqCode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        mWalletController = new WalletController(this, mHandler);
        mInclude01.setVisibility(View.VISIBLE);
        mInclude02.setVisibility(View.GONE);
        mConfigBtn.setBackgroundResource(R.drawable.btn_pay_unselect);
        mConfigBtn.setEnabled(false);

        mRechargeNum.requestFocus();
        InputTools.KeyBoard(mRechargeNum, "open");

        initClick();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initClick() {
        mOrderTopView.setBackClickListener(new WalletTopView.BackClickListener() {
            @Override
            public void back() {
                RechargeActivity.this.finish();
            }
        });

        mConfigBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double a = Double.parseDouble(mRechargeNum.getText().toString());
                doRecharge(WalletUtils.formateMoney(a), null, WalletUtils.APP);
            }
        });

        mPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecharge) {
                    RechargeActivity.this.finish();
                } else {
                    mInclude01.setVisibility(View.VISIBLE);
                    mInclude02.setVisibility(View.GONE);
                    mOrderTopView.setTitle("零钱充值", "安全支付");
                    mOrderTopView.setRBackVisibility();
                }
            }
        });

        mRechargeNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        mRechargeNum.setText(s);
                        mRechargeNum.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    mRechargeNum.setText(s);
                    mRechargeNum.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        mRechargeNum.setText(s.subSequence(0, 1));
                        mRechargeNum.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(mRechargeNum.getText().toString())) {
                    double a = Double.parseDouble(mRechargeNum.getText().toString());
                    if (a > 0) {
                        mConfigBtn.setBackgroundResource(R.drawable.btn_pay_orange_selector);
                        mConfigBtn.setEnabled(true);
                    } else {
                        mConfigBtn.setBackgroundResource(R.drawable.btn_pay_unselect);
                        mConfigBtn.setEnabled(false);
                    }
                } else {
                    mConfigBtn.setBackgroundResource(R.drawable.btn_pay_unselect);
                    mConfigBtn.setEnabled(false);
                }
            }
        });
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_recharge, null);
    }

    @Override
    public View onLoadNavView() {
        mOrderTopView = new WalletTopView(this);
        mOrderTopView.setTitle("零钱充值", "安全支付");
        mOrderTopView.setRBackVisibility();
        return mOrderTopView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    private void doRecharge(long rechargeAmount, String returnUrl, String sourceType) {
        mWalletController.doRecharge(rechargeAmount, returnUrl, sourceType, this);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case ValueConstants.PAY_Recharge_SUCCESS:
                RechargeResult result = (RechargeResult) msg.obj;
                if (result.success) {
                    if (!TextUtils.isEmpty(result.requestUrl)) {
                        WebParams params = new WebParams();
                        payOrderId = result.payOrderId;
                        params.setUrl(result.requestUrl);
                        params.setIsNeedSetResult(true);
                        params.setTitle("支付");
                        NavUtils.openBrowserByRequestCode(RechargeActivity.this, params, 0x100);
                    } else {
                        ToastUtil.showToast(RechargeActivity.this, "参数错误");
                    }
                } else {
                    ToastUtil.showToast(RechargeActivity.this, result.errorMsg);
                }
                break;
            case ValueConstants.PAY_Recharge_ERROR:
                ToastUtil.showToast(RechargeActivity.this, StringUtil.handlerErrorCode(RechargeActivity.this, msg.arg1));
                break;
            case ValueConstants.PAY_QueryTransStatus_SUCCESS:
                PcPayResult pcPayResult = (PcPayResult) msg.obj;
                count++;
                if (pcPayResult.success) {
                    hideLoadingView();
                    isRecharge = true;
                    mPayStatusImg.setImageResource(R.mipmap.pay_success_image);
                    mPayStatusTV.setText("充值成功");
                    double a = Double.parseDouble(mRechargeNum.getText().toString());
                    mRechargeMoneyTitle.setText("充值金额");
                    mRechargeMoney.setText(StringUtil.formatWalletMoney(this, WalletUtils.formateMoney(a)));
                    mPay.setText("完成");
                } else {
                    if (count < MAXCOUNT) {
                        mHandler.sendEmptyMessageDelayed(0x10, 2000);
                    } else {
                        hideLoadingView();
                        isRecharge = false;
                        mPayStatusImg.setImageResource(R.mipmap.pay_error_image);
                        mPayStatusTV.setText("充值失败");
                        double a = Double.parseDouble(mRechargeNum.getText().toString());
                        mRechargeMoneyTitle.setText("充值金额");
                        mRechargeMoney.setText(StringUtil.formatWalletMoney(this, WalletUtils.formateMoney(a)));
                        mPay.setText("重新充值");
                    }
                }
                break;
            case ValueConstants.PAY_QueryTransStatus_ERROR:
                hideLoadingView();
                ToastUtil.showToast(RechargeActivity.this, StringUtil.handlerErrorCode(RechargeActivity.this, msg.arg1));
                break;
            case 0x10:
                doQueryTransStatus(payOrderId);
                break;
        }
    }

    public void onEvent(GuangDaPaySuccessEvBus bus) {
        //TODO 光大支付成功后的通知
//        mInclude01.setVisibility(View.GONE);
//        mInclude02.setVisibility(View.VISIBLE);
//        mOrderTopView.setTitle("充值详情", "安全支付");
//        mOrderTopView.setBackGone();
//        showLoadingView("正在确认支付状态，请稍候...");
//        mPayStatusTV.setText("");
//        mPayStatusImg.setImageResource(0);
//        mRechargeMoneyTitle.setText("");
//        mHandler.sendEmptyMessageDelayed(0x10, 2000);
        setResult(RESULT_OK, getIntent());
        this.finish();
    }

    private void doQueryTransStatus(long payOrderId) {
        mWalletController.doQueryTransStatus(payOrderId, this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0x100:
//                    mPayStatusImg.setImageResource(0);
//                    mPayStatusTV.setText("");
//                    mInclude01.setVisibility(View.GONE);
//                    mInclude02.setVisibility(View.VISIBLE);
//                    mOrderTopView.setTitle("充值详情", "安全支付");
//                    mOrderTopView.setBackGone();
//                    showLoadingView("正在确认支付状态，请稍候...");
//                    mHandler.sendEmptyMessageDelayed(0x10, 2000);
                    break;
            }
        }
    }
}
