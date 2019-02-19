package com.quanyan.yhy.ui.wallet.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
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
import com.quanyan.yhy.ui.wallet.view.WalletXEditText;
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
 * Title:ForgetPasBindCardActivity
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-9-19
 * Time:16:54
 * Version 1.0
 * Description:
 */
public class ForgetPasBindCardActivity extends BaseActivity {
    private static final String BANKCARD = "BANKCARD";
    private static final String SURCETYPE = "SURCETYPE";
    private static final String BANKNO = "BANKNO";
    private WalletTopView mOrderTopView;

    private BankCard mBankCard;
    private String bankName = "";
    private String bankCardType = "";

    @ViewInject(R.id.tv_card_num)
    private TextView mCardType;
    @ViewInject(R.id.et_real_name)
    private TextView mName;
    @ViewInject(R.id.et_idcard_num)
    private WalletXEditText mIDCard;
    @ViewInject(R.id.et_phone_num)
    private EditText mPhoneNum;
    @ViewInject(R.id.btn_next_02)
    private Button mNextBtn;

    @ViewInject(R.id.rl_card_type)
    private RelativeLayout mRlCardType;

    private WalletController mWalletController;

    private int sourceType;

    private View bankCardView;
    private Dialog mBandDialog;
    private TextView mCancelTv;
    private TextView mConfirmTv;

    private BankNameList bankList;
    private String[] bankType;

    private String selectBankName;

    @ViewInject(R.id.ll_deal)
    private LinearLayout mLDeal;

    @ViewInject(R.id.tv_deal)
    private TextView mDealTv;
    @ViewInject(R.id.ck_deal)
    private CheckBox mCkDeal;

    private String bankNo;

    public static void gotoForgetPasBindCardActivity(Context context, BankCard mBankCard, int sourceType, String bankNo) {
        Intent intent = new Intent(context, ForgetPasBindCardActivity.class);
        intent.putExtra(BANKCARD, mBankCard);
        intent.putExtra(SURCETYPE, sourceType);
        intent.putExtra(BANKNO, bankNo);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        mBankCard = (BankCard) getIntent().getSerializableExtra(BANKCARD);
        sourceType = getIntent().getIntExtra(SURCETYPE, 0);
        bankNo = getIntent().getStringExtra(BANKNO);
        bankType = new String[]{"储蓄卡"};
        mWalletController = new WalletController(this, mHandler);
        mNextBtn.setBackgroundResource(R.drawable.btn_pay_unselect);

        mIDCard.setPattern(new int[]{20});
        mIDCard.setSeparator("");

        mNextBtn.setEnabled(false);
        mRlCardType.setEnabled(false);
        if (mBankCard == null || TextUtils.isEmpty(mBankCard.bankName)) {
            doGetBankNames();
        } else {
            mRlCardType.setEnabled(false);
        }
        setView(mBankCard);

        initClick();
    }

    private void initClick() {
        mOrderTopView.setBackClickListener(new WalletTopView.BackClickListener() {
            @Override
            public void back() {
                if (sourceType == 1) {
                    showBack("是否放弃找回密码");
                } else {
                    ForgetPasBindCardActivity.this.finish();
                }
            }
        });

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum = mPhoneNum.getText().toString();
                String userName = SPUtils.getWalletName(ForgetPasBindCardActivity.this);
                String idNo = mIDCard.getText().toString();
                VerifyIdentityParam params = new VerifyIdentityParam();
                params.mobilePhone = phoneNum;
                params.idNo = idNo;
                params.userName = userName;
                if (sourceType == 1) {
                    params.bindCardId = mBankCard.bindCardId;
                } else {
                    if (mBankCard == null || TextUtils.isEmpty(mBankCard.bankName)) {
                        params.bankName = selectBankName;
                        params.bankCardNo = bankNo;
                    } else {
                        params.bankCardNo = mBankCard.bankCardNo;
                        params.bankName = mBankCard.bankName;
                    }
                }
                params.verifyIdentityType = WalletUtils.FORGET_PAY_PWD;
                params.merchantType = WalletUtils.NOT_MERCHANT;

                mNextBtn.setBackgroundResource(R.drawable.btn_pay_unselect);
                mNextBtn.setEnabled(false);

                doVerifyIdentity(params);
            }
        });

        mIDCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String idCard = s.toString();
                String phoneNum = mPhoneNum.getText().toString();
                setNextBtn_02(idCard, phoneNum);
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
                String phoneNum = s.toString();
                String idCard = mIDCard.getText().toString();
                setNextBtn_02(idCard, phoneNum);
            }
        });

        mRlCardType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBandDialog != null) {
                    mBandDialog.show();
                }
            }
        });

        mCkDeal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setNextBtn_02(mIDCard.getText().toString(), mPhoneNum.getText().toString());
            }
        });

        mDealTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(SPUtils.getWalletDeal(ForgetPasBindCardActivity.this))) {
                    WebParams params = new WebParams();
                    params.setUrl(SPUtils.getWalletDeal(ForgetPasBindCardActivity.this));
                    params.setTitle("用户协议");
                    NavUtils.openBrowser(ForgetPasBindCardActivity.this, params);
                }
            }
        });
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_forgetpas_bindcard, null);
    }

    @Override
    public View onLoadNavView() {
        mOrderTopView = new WalletTopView(this);
        mOrderTopView.setTitle("忘记支付密码", "安全支付");
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
            case ValueConstants.PAY_VerifyIdentity_SUCCESS:
                mNextBtn.setBackgroundResource(R.drawable.btn_pay_orange_selector);
                mNextBtn.setEnabled(true);

                VerifyIdentityResult verifyIdentityResult = (VerifyIdentityResult) msg.obj;
                if (verifyIdentityResult.success) {
                    if (!TextUtils.isEmpty(verifyIdentityResult.verifyIdentityCode)) {
                        NavUtils.gotoBindCradVCodeActivity(ForgetPasBindCardActivity.this, mPhoneNum.getText().toString(), verifyIdentityResult.verifyIdentityCode, WalletUtils.FORGET_PAY_PWD);
                    } else {
                        ToastUtil.showToast(ForgetPasBindCardActivity.this, "身份认证失败");
                    }
                } else {
                    ToastUtil.showToast(ForgetPasBindCardActivity.this, verifyIdentityResult.errorMsg);
                }
                break;
            case ValueConstants.PAY_VerifyIdentity_ERROR:
                mNextBtn.setBackgroundResource(R.drawable.btn_pay_orange_selector);
                mNextBtn.setEnabled(true);

                ToastUtil.showToast(ForgetPasBindCardActivity.this, StringUtil.handlerErrorCode(ForgetPasBindCardActivity.this, msg.arg1));
                break;

            case ValueConstants.PAY_GetBankNameList_SUCCESS:
                bankList = (BankNameList) msg.obj;
                if (bankList != null && bankList.bankNameList != null && bankList.bankNameList.size() != 0) {
                    mRlCardType.setEnabled(true);
                    showBankCardDialog(bankList.bankNameList, bankType);
                }
                break;
            case ValueConstants.PAY_GetBankNameList_ERROR:

                break;
        }
    }

    private void setView(BankCard mBankCard) {
        if (mBankCard == null) {
            return;
        }
        if (!TextUtils.isEmpty(mBankCard.bankName)) {
            bankName = mBankCard.bankName;
        }
        if (!TextUtils.isEmpty(mBankCard.bankCardType)) {
            bankCardType = WalletUtils.getBankByCode(mBankCard.bankCardType);
        }
        mCardType.setText(bankName + "    (" + bankCardType + ")");

        if (!TextUtils.isEmpty(SPUtils.getWalletName(this))) {
            mName.setText(WalletUtils.formatUserName(SPUtils.getWalletName(this)));
        }
    }

    private void setNextBtn_02(String idCard, String phoneNum) {
        if (RegexUtil.isIdCard(idCard)) {
            if (RegexUtil.isMobile(phoneNum)) {
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
        } else {
            mNextBtn.setBackgroundResource(R.drawable.btn_pay_unselect);
            mNextBtn.setEnabled(false);
        }
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
                mCardType.setText(a[mW1.getCurrentItem()] + "    (储蓄卡)");
            }
        });
    }


    private Dialog mSurErrorDialog;

    private void showBack(String msg) {
        if (mSurErrorDialog == null) {
            mSurErrorDialog = DialogUtil.showMessageDialog(this,
                    null,
                    msg,
                    "是",
                    "否",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mSurErrorDialog != null) {
                                mSurErrorDialog.dismiss();
                            }
                            ForgetPasBindCardActivity.this.finish();
                        }
                    },
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mSurErrorDialog != null) {
                                mSurErrorDialog.dismiss();
                            }
                        }
                    });
        }
        mSurErrorDialog.show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (sourceType == 1) {
                showBack("是否放弃找回密码");
            } else {
                ForgetPasBindCardActivity.this.finish();
            }
        }
        return false;
    }

}
