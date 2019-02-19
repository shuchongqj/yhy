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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.RegexUtil;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.base.utils.DialogUtil;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.views.birthdaychoose.ArrayWheelAdapter;
import com.quanyan.yhy.ui.views.birthdaychoose.WheelView;
import com.quanyan.yhy.ui.wallet.Controller.WalletController;
import com.quanyan.yhy.ui.wallet.WalletUtils;
import com.quanyan.yhy.ui.wallet.view.WalletTopView;
import com.yhy.common.beans.net.model.param.WebParams;
import com.yhy.common.beans.net.model.paycore.BankCard;
import com.yhy.common.beans.net.model.paycore.BankNameList;
import com.yhy.common.beans.net.model.paycore.VerifyIdentityParam;
import com.yhy.common.beans.net.model.paycore.VerifyIdentityResult;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;

import java.util.List;

/**
 * Created with Android Studio.
 * Title:BindCardInforActivity
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-9-19
 * Time:16:52
 * Version 1.0
 * Description:
 */
public class BindCardInforActivity extends BaseActivity {

    private static final String CARDNO = "CARDNO";
    private static final String CARDNUM = "CARDNUM";

    private WalletTopView mOrderTopView;
    private BankCard cardNo;

    @ViewInject(R.id.tv_card_num_04)
    private TextView mCardTypeTv;

    @ViewInject(R.id.et_phone_num_04)
    private EditText mPhoneNum;

    @ViewInject(R.id.btn_next_04)
    private Button mNextBtn;

    @ViewInject(R.id.ll_deal)
    private LinearLayout mLDeal;

    @ViewInject(R.id.tv_deal)
    private TextView mDealTv;

    @ViewInject(R.id.ck_deal)
    private CheckBox mCkDeal;

    private WalletController mWalletController;

    String bankName = "";
    String bankCardType = "";

    private String cardNum;
    @ViewInject(R.id.rl_card_type)
    private RelativeLayout mCardTypeLayout;

    private BankNameList bankList;
    private String[] bankType;
    private View bankCardView;
    private Dialog mBandDialog;
    private TextView mCancelTv;
    private TextView mConfirmTv;
    private String selectBankName;

    public static void gotoBindCardInforActivity(Context context, BankCard cardNo, String cardNum) {
        Intent intent = new Intent(context, BindCardInforActivity.class);
        intent.putExtra(CARDNO, cardNo);
        intent.putExtra(CARDNUM, cardNum);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        cardNo = (BankCard) getIntent().getSerializableExtra(CARDNO);
        cardNum = getIntent().getStringExtra(CARDNUM);
        mWalletController = new WalletController(this, mHandler);
        bankType = new String[]{"储蓄卡"};
        mNextBtn.setBackgroundResource(R.drawable.btn_pay_unselect);
        mNextBtn.setEnabled(false);

        mCardTypeLayout.setEnabled(false);
        if (cardNo == null || TextUtils.isEmpty(cardNo.bankName)) {
            doGetBankNames();
        } else {
            mCardTypeLayout.setEnabled(false);
        }
        handleData(cardNo);

        initClick();
    }

    private void initClick() {
        mOrderTopView.setBackClickListener(new WalletTopView.BackClickListener() {
            @Override
            public void back() {
                BindCardInforActivity.this.finish();
            }
        });

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //验证身份
                String phoneNum = mPhoneNum.getText().toString();
                String userName = SPUtils.getWalletName(BindCardInforActivity.this);
                VerifyIdentityParam params = new VerifyIdentityParam();
                params.mobilePhone = phoneNum;
                params.userName = userName;
                if(cardNo == null || TextUtils.isEmpty(cardNo.bankName)){
                    params.bankCardNo = cardNum;
                    params.bankName = selectBankName;
                }else {
                    params.bankCardNo = cardNo.bankCardNo;
                    params.bankName = bankName;
                }
                params.verifyIdentityType = WalletUtils.BIND_BANK_CARD;
                params.merchantType = WalletUtils.NOT_MERCHANT;

                mNextBtn.setBackgroundResource(R.drawable.btn_pay_unselect);
                mNextBtn.setEnabled(false);

                doVerifyIdentity(params);
            }
        });

        mPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setBtn(s.toString());
            }
        });

        mCkDeal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setBtn(mPhoneNum.getText().toString());
            }
        });

        mDealTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(SPUtils.getWalletDeal(BindCardInforActivity.this))) {
                    WebParams params = new WebParams();
                    params.setUrl(SPUtils.getWalletDeal(BindCardInforActivity.this));
                    params.setTitle("用户协议");
                    NavUtils.openBrowser(BindCardInforActivity.this, params);
                }
            }
        });

        mCardTypeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBandDialog != null) {
                    mBandDialog.show();
                }
            }
        });
    }

    private void setBtn(String phone) {
        if (RegexUtil.isMobile(phone.toString())) {
            if (mCkDeal.isChecked()) {
                mNextBtn.setBackgroundResource(R.drawable.btn_pay_orange_selector);
                mNextBtn.setEnabled(true);
            } else {
                mNextBtn.setBackgroundResource(R.drawable.btn_pay_unselect);
                mNextBtn.setEnabled(false);
            }
        } else {
            mNextBtn.setBackgroundResource(R.drawable.btn_pay_unselect);
            mNextBtn.setEnabled(false);
        }
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_bindcardinfor, null);
    }

    @Override
    public View onLoadNavView() {
        mOrderTopView = new WalletTopView(this);
        mOrderTopView.setTitle("填写信息", "安全支付");
        return mOrderTopView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }


    //验证身份
    private void doVerifyIdentity(VerifyIdentityParam params) {
        mWalletController.doVerifyIdentity(params, this);
    }

    private void doGetBankNames() {
        mWalletController.doGetBankNameList(this);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case ValueConstants.PAY_BankCardByCardNo_SUCCESS:
                BankCard bankCard = (BankCard) msg.obj;
                handleData(bankCard);
                break;
            case ValueConstants.PAY_BankCardByCardNo_ERROR:
                ToastUtil.showToast(BindCardInforActivity.this, StringUtil.handlerErrorCode(BindCardInforActivity.this, msg.arg1));
                break;
            case ValueConstants.PAY_VerifyIdentity_SUCCESS:
                mNextBtn.setBackgroundResource(R.drawable.btn_pay_orange_selector);
                mNextBtn.setEnabled(true);
                VerifyIdentityResult verifyIdentityResult = (VerifyIdentityResult) msg.obj;
                if (verifyIdentityResult.success) {
                    if (!TextUtils.isEmpty(verifyIdentityResult.verifyIdentityCode)) {
                        NavUtils.gotoBindCradVCodeActivity(BindCardInforActivity.this, mPhoneNum.getText().toString(), verifyIdentityResult.verifyIdentityCode, WalletUtils.BIND_BANK_CARD);
                    } else {
                        ToastUtil.showToast(BindCardInforActivity.this, "身份认证失败");
                    }
                } else {
                    ToastUtil.showToast(BindCardInforActivity.this, verifyIdentityResult.errorMsg);
                }
                break;
            case ValueConstants.PAY_VerifyIdentity_ERROR:
                mNextBtn.setBackgroundResource(R.drawable.btn_pay_orange_selector);
                mNextBtn.setEnabled(true);

                ToastUtil.showToast(BindCardInforActivity.this, StringUtil.handlerErrorCode(BindCardInforActivity.this, msg.arg1));
                break;

            case ValueConstants.PAY_GetBankNameList_SUCCESS:
                bankList = (BankNameList) msg.obj;
                if (bankList != null && bankList.bankNameList != null && bankList.bankNameList.size() != 0) {
                    mCardTypeLayout.setEnabled(true);
                    showBankCardDialog(bankList.bankNameList, bankType);
                }
                break;
            case ValueConstants.PAY_GetBankNameList_ERROR:

                break;
        }
    }

    private void handleData(BankCard bankCard) {
        if (bankCard == null) {
            return;
        }
        if (!TextUtils.isEmpty(bankCard.bankName)) {
            bankName = bankCard.bankName;
        }
        if (!TextUtils.isEmpty(bankCard.bankCardType)) {
            bankCardType = WalletUtils.getBankByCode(bankCard.bankCardType);
        }
        mCardTypeTv.setText(bankName + "(" + bankCardType + ")");
    }


    private void showBankCardDialog(List<String> bankList, String[] type) {
        if (bankList == null || bankList.size() == 0) {
            return;
        }
        final String[] a = new String[bankList.size()];
        for (int i = 0; i < bankList.size(); i++) {
            a[i] = bankList.get(i);
        }
        bankCardView = LayoutInflater.from(this).inflate(R.layout.view_selectcard_dialog, null);
        final WheelView mW1 = (WheelView) bankCardView.findViewById(R.id.wl_bank);
        WheelView mW2 = (WheelView) bankCardView.findViewById(R.id.wl_bank_type);
        mCancelTv = (TextView) bankCardView.findViewById(R.id.tv_cancle);
        mConfirmTv = (TextView) bankCardView.findViewById(R.id.tv_confirm);

        mW1.setCyclic(false);
        mW1.setAdapter(new ArrayWheelAdapter<String>(a, 100));
        mW1.setCurrentItem(0);

        mW2.setCyclic(false);
        mW2.setAdapter(new ArrayWheelAdapter<String>(type, 100));
        mW2.setCurrentItem(0);


        mBandDialog = DialogUtil.getMenuDialog(this, bankCardView);
        mBandDialog.setCanceledOnTouchOutside(true);

        mCancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBandDialog != null) {
                    mBandDialog.dismiss();
                }
            }
        });

        mConfirmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBandDialog != null) {
                    mBandDialog.dismiss();
                }
                selectBankName = a[mW1.getCurrentItem()];
                mCardTypeTv.setText(a[mW1.getCurrentItem()] + "    (储蓄卡)");
            }
        });
    }

}
