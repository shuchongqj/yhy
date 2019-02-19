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
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.harwkin.nb.camera.TimeUtil;
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
import com.quanyan.yhy.ui.wallet.InputTools;
import com.quanyan.yhy.ui.wallet.WalletUtils;
import com.quanyan.yhy.ui.wallet.view.WalletTopView;
import com.quanyan.yhy.ui.wallet.view.WalletXEditText;
import com.yhy.common.beans.net.model.paycore.BankCard;
import com.yhy.common.beans.net.model.paycore.BankNameList;
import com.yhy.common.beans.net.model.paycore.PayCoreBaseResult;
import com.yhy.common.beans.net.model.paycore.VerifyIdentityParam;
import com.yhy.common.beans.net.model.paycore.VerifyIdentityResult;
import com.yhy.common.constants.ValueConstants;

import java.util.List;

/**
 * Created with Android Studio.
 * Title:RealNameAuthActivity
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-8-9
 * Time:16:26
 * Version 1.0
 * Description: 实名认证
 */
public class RealNameAuthActivity extends BaseActivity {

    private static final String TYPE = "type";

    private static final String TOWALLET = "TOWALLET";
    private static final int TIME = 0x100;
    private static final long DelayMillis = 1000;

    private WalletTopView mOrderTopView;
    @ViewInject(R.id.et_card_num)
    private WalletXEditText mCardNum;
    @ViewInject(R.id.btn_next_01)
    private Button mNextBtn_01;
    @ViewInject(R.id.auth_01)
    private View auth_01;


    @ViewInject(R.id.auth_02)
    private View auth_02;
    @ViewInject(R.id.et_real_name)
    private EditText mRealName;
    @ViewInject(R.id.et_idcard_num)
    private WalletXEditText mIdCardNum;
    @ViewInject(R.id.et_phone_num)
    private EditText mPhoneNum;
    @ViewInject(R.id.btn_next_02)
    private Button mNextBtn_02;
    @ViewInject(R.id.tv_card_num)
    private TextView mCardType;
    @ViewInject(R.id.rl_card_type)
    private RelativeLayout mRlCardType;


    @ViewInject(R.id.auth_03)
    private View auth_03;
    @ViewInject(R.id.tv_tel_tips)
    private TextView mTelTips;
    @ViewInject(R.id.btn_auth_code)
    private Button mAuthCodeBtn;
    @ViewInject(R.id.et_code_num)
    private WalletXEditText mCodeNum;
    @ViewInject(R.id.btn_next_03)
    private Button mNextBtn_03;

    private WalletController mWalletController;

    private int count = 59;

    private BankCard bankCard;

    private VerifyIdentityResult verifyIdentityResult;

    private String type;
    private String toWallet="";
    private View bankCardView;
    private Dialog mBandDialog;
    private TextView mCancelTv;
    private TextView mConfirmTv;

    private BankNameList bankList;

    private String[] bankType;
    private String selectBankName;

    private int page = 1;

    public static void gotoRealNameAuthActivity(Context context, String type,String toWallet) {
        Intent intent = new Intent(context, RealNameAuthActivity.class);
        intent.putExtra(TYPE, type);
        intent.putExtra(TOWALLET, toWallet);
        context.startActivity(intent);
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        type = getIntent().getStringExtra(TYPE);
        toWallet=getIntent().getStringExtra(TOWALLET);
        bankType = new String[]{"储蓄卡"};
        mWalletController = new WalletController(this, mHandler);
        auth_01.setVisibility(View.VISIBLE);
        auth_02.setVisibility(View.GONE);
        auth_03.setVisibility(View.GONE);
        mNextBtn_01.setBackgroundResource(R.drawable.btn_pay_unselect);
        mNextBtn_02.setBackgroundResource(R.drawable.btn_pay_unselect);
        mNextBtn_03.setBackgroundResource(R.drawable.btn_pay_unselect);
        mNextBtn_01.setEnabled(false);
        mNextBtn_02.setEnabled(false);
        mNextBtn_03.setEnabled(false);

        mCardNum.setPattern(new int[]{4, 4, 4, 4, 3});
        mCardNum.setSeparator(" ");

        mIdCardNum.setPattern(new int[]{20});
        mIdCardNum.setSeparator("");

        mCodeNum.setPattern(new int[]{6});
        mCodeNum.setSeparator("");

        doGetBankNames();

        initClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCardNum.requestFocus();
        InputTools.KeyBoard(mCardNum, "open");
    }

    private void initClick() {
        mOrderTopView.setBackClickListener(new WalletTopView.BackClickListener() {
            @Override
            public void back() {
                if (page == 1) {
                    showBack("是否放弃钱包激活");
                } else if (page == 2) {
                    page = 1;
                    mCardType.setText("");
                    mPhoneNum.setText("");
                    mIdCardNum.setText("");
                    mRealName.setText("");
                    auth_01.setVisibility(View.VISIBLE);
                    auth_02.setVisibility(View.GONE);
                    auth_03.setVisibility(View.GONE);
                } else if (page == 3) {
                    page = 2;
                    mCodeNum.setText("");
                    auth_01.setVisibility(View.GONE);
                    auth_02.setVisibility(View.VISIBLE);
                    auth_03.setVisibility(View.GONE);
                    mOrderTopView.setTitle("钱包激活", null);
                    InputTools.KeyBoard(mCardNum, "close");
                    mHandler.removeMessages(TIME);
                }
            }
        });

        mNextBtn_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TimeUtil.isFastDoubleClick()) {
                    return;
                }
                doGetBankMsg(mCardNum.getNonSeparatorText());
            }
        });

        mNextBtn_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum = mPhoneNum.getText().toString();
                String userName = mRealName.getText().toString();
                String idNo = mIdCardNum.getText().toString();
                String bankCardNo = mCardNum.getNonSeparatorText();
                VerifyIdentityParam params = new VerifyIdentityParam();
                params.mobilePhone = phoneNum;
                params.userName = userName;
                params.idNo = idNo;
                params.bankCardNo = bankCardNo;
                if (bankCard == null || TextUtils.isEmpty(bankCard.bankName)) {
                    params.bankName = selectBankName;
                } else {
                    params.bankName = bankCard.bankName;
                }
                params.verifyIdentityType = type;
                params.merchantType = WalletUtils.NOT_MERCHANT;

                if (TimeUtil.isFastDoubleClick()) {
                    return;
                }

                mNextBtn_02.setEnabled(false);
                mNextBtn_02.setBackgroundResource(R.drawable.btn_pay_unselect);

                doVerifyIdentity(params);
            }
        });

        mNextBtn_03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TimeUtil.isFastDoubleClick()) {
                    return;
                }
                mNextBtn_03.setEnabled(false);
                mNextBtn_03.setBackgroundResource(R.drawable.btn_pay_unselect);
                String code = mCodeNum.getText().toString();
                doCheckVerifyCode(type, code, verifyIdentityResult.verifyIdentityCode);
            }
        });

        mCardNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(mCardNum.getNonSeparatorText())) {
                    if (WalletUtils.checkBankCard(mCardNum.getNonSeparatorText())) {
                        mNextBtn_01.setBackgroundResource(R.drawable.btn_pay_orange_selector);
                        mNextBtn_01.setEnabled(true);
                    } else {
                        mNextBtn_01.setBackgroundResource(R.drawable.btn_pay_unselect);
                        mNextBtn_01.setEnabled(false);
                    }
                } else {
                    mNextBtn_01.setBackgroundResource(R.drawable.btn_pay_unselect);
                    mNextBtn_01.setEnabled(false);
                }
            }
        });


        mRealName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String name = s.toString();
                String idCard = mIdCardNum.getText().toString();
                String phoneNum = mPhoneNum.getText().toString();
                setNextBtn_02(name, idCard, phoneNum);
            }
        });

        mIdCardNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String idCard = s.toString();
                String name = mRealName.getText().toString();
                String phoneNum = mPhoneNum.getText().toString();
                setNextBtn_02(name, idCard, phoneNum);
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
                String idCard = mIdCardNum.getText().toString();
                String name = mRealName.getText().toString();
                setNextBtn_02(name, idCard, phoneNum);
            }
        });


        mCodeNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    if (s.toString().length() == 6) {
                        mNextBtn_03.setBackgroundResource(R.drawable.btn_pay_orange_selector);
                        mNextBtn_03.setEnabled(true);
                    } else {
                        mNextBtn_03.setBackgroundResource(R.drawable.btn_pay_unselect);
                        mNextBtn_03.setEnabled(false);
                    }
                } else {
                    mNextBtn_03.setBackgroundResource(R.drawable.btn_pay_unselect);
                    mNextBtn_03.setEnabled(false);
                }
            }
        });

        mAuthCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum = mPhoneNum.getText().toString();
                mAuthCodeBtn.setText("获取验证码\n(" + count + ")");
                mAuthCodeBtn.setEnabled(false);
                mAuthCodeBtn.setBackgroundResource(R.drawable.wallet_vcode_btn_background);
                mAuthCodeBtn.setTextColor(getResources().getColor(R.color.neu_999999));
                doSendVerifyCode(type, phoneNum, 2);
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
    }

    private void setNextBtn_02(String name, String idCard, String phoneNum) {
        if (RegexUtil.isName(name)) {
            if (RegexUtil.isIdCard(idCard)) {
                if (RegexUtil.isMobile(phoneNum)) {
                    mNextBtn_02.setBackgroundResource(R.drawable.btn_pay_orange_selector);
                    mNextBtn_02.setEnabled(true);
                } else {
                    mNextBtn_02.setBackgroundResource(R.drawable.btn_pay_unselect);
                    mNextBtn_02.setEnabled(false);
                }
            } else {
                mNextBtn_02.setBackgroundResource(R.drawable.btn_pay_unselect);
                mNextBtn_02.setEnabled(false);
            }
        } else {
            mNextBtn_02.setBackgroundResource(R.drawable.btn_pay_unselect);
            mNextBtn_02.setEnabled(false);
        }
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_realname_auth, null);
    }

    @Override
    public View onLoadNavView() {
        mOrderTopView = new WalletTopView(this);
        mOrderTopView.setTitle("钱包激活", null);
        return mOrderTopView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    //得到银行卡信息
    private void doGetBankMsg(String cardNo) {
        if (TextUtils.isEmpty(cardNo)) {
            return;
        }
        mWalletController.doGetBankCardByCardNo(cardNo, this);
    }

    //发送验证码
    private void doSendVerifyCode(String verifyCodeType, String mobilePhone, int type) {
        mWalletController.doSendVerifyCode(verifyCodeType, mobilePhone, this, type);
    }

    //验证身份
    private void doVerifyIdentity(VerifyIdentityParam params) {
        mWalletController.doVerifyIdentity(params, this);
    }

    //验证输入的验证码
    private void doCheckVerifyCode(String verifyCodeType, String verifyCode, String verifyIdentityCode) {
        mWalletController.doCheckVerifyCode(verifyCodeType, verifyCode, verifyIdentityCode, RealNameAuthActivity.this);
    }

    private void doGetBankNames() {
        mWalletController.doGetBankNameList(this);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case ValueConstants.PAY_BankCardByCardNo_SUCCESS:
                bankCard = (BankCard) msg.obj;
                page = 2;
                auth_01.setVisibility(View.GONE);
                auth_02.setVisibility(View.VISIBLE);
                auth_03.setVisibility(View.GONE);
                if (bankCard != null) {
                    String bankName = "";
                    String bankCardType = "";
                    if (!TextUtils.isEmpty(bankCard.bankName)) {
                        bankName = bankCard.bankName;
                        mRlCardType.setEnabled(false);
                    } else {
                        mRlCardType.setEnabled(true);
                    }
                    if (!TextUtils.isEmpty(bankCard.bankCardType)) {
                        bankCardType = WalletUtils.getBankByCode(bankCard.bankCardType);
                    }
                    mCardType.setText(bankName + "    (" + bankCardType + ")");
                } else {
                    mCardType.setText("");
                    mRlCardType.setEnabled(true);
                }
                break;
            case ValueConstants.PAY_BankCardByCardNo_ERROR:
                ToastUtil.showToast(RealNameAuthActivity.this, StringUtil.handlerErrorCode(RealNameAuthActivity.this, msg.arg1));
                break;
            case ValueConstants.PAY_SendVerifyCode_SUCCESS:
                PayCoreBaseResult result = (PayCoreBaseResult) msg.obj;
                if (result.success) {
                    page = 3;
                    auth_01.setVisibility(View.GONE);
                    auth_02.setVisibility(View.GONE);
                    auth_03.setVisibility(View.VISIBLE);
                    mOrderTopView.setTitle("验证手机号", "安全支付");
                    mTelTips.setText("本次操作需要短信确认，验证码已发送至手机：" + WalletUtils.formatePhone(mPhoneNum.getText().toString()) + "，请按提示操作。");
                    mAuthCodeBtn.setText("获取验证码\n(" + count + ")");
                    mAuthCodeBtn.setEnabled(false);
                    mAuthCodeBtn.setBackgroundResource(R.drawable.wallet_vcode_btn_background);
                    mAuthCodeBtn.setTextColor(getResources().getColor(R.color.neu_999999));
                    mHandler.sendEmptyMessageDelayed(TIME, DelayMillis);
                } else {
                    ToastUtil.showToast(RealNameAuthActivity.this, result.errorMsg);
                }
                break;
            case ValueConstants.PAY_SendVerifyCode_ERROR:
                ToastUtil.showToast(RealNameAuthActivity.this, StringUtil.handlerErrorCode(RealNameAuthActivity.this, msg.arg1));
                break;
            case TIME:
                count--;
                if (count <= 0) {
                    count = 59;
                    mAuthCodeBtn.setText("重获验证码");
                    mAuthCodeBtn.setEnabled(true);
                    mAuthCodeBtn.setBackgroundResource(R.drawable.btn_pay_orange_selector);
                    mAuthCodeBtn.setTextColor(getResources().getColor(R.color.black));
                } else {
                    mAuthCodeBtn.setText("获取验证码\n(" + count + ")");
                    mAuthCodeBtn.setEnabled(false);
                    mAuthCodeBtn.setBackgroundResource(R.drawable.wallet_vcode_btn_background);
                    mAuthCodeBtn.setTextColor(getResources().getColor(R.color.neu_999999));
                    mHandler.sendEmptyMessageDelayed(TIME, DelayMillis);

                }
                break;
            case ValueConstants.PAY_SendVerifyCode_TYPE_SUCCESS:
                PayCoreBaseResult results = (PayCoreBaseResult) msg.obj;
                if (results.success) {
                    mHandler.sendEmptyMessageDelayed(TIME, DelayMillis);
                } else {
                    ToastUtil.showToast(RealNameAuthActivity.this, results.errorMsg);
                }
                break;
            case ValueConstants.PAY_CheckVerifyCode_SUCCESS:
                PayCoreBaseResult payCoreBaseResult = (PayCoreBaseResult) msg.obj;
                mNextBtn_03.setEnabled(true);
                mNextBtn_03.setBackgroundResource(R.drawable.btn_pay_orange_selector);
                if (payCoreBaseResult.success) {
                    NavUtils.gotoSettingPayPassActivity(RealNameAuthActivity.this, mCodeNum.getText().toString(), verifyIdentityResult.verifyIdentityCode, type,toWallet);
                    RealNameAuthActivity.this.finish();
                } else {
                    if (payCoreBaseResult.errorCode.equals(WalletUtils.SYSTEM_ERROR)) {
                        ToastUtil.showToast(RealNameAuthActivity.this, "开户失败，请稍候重试");
                    } else {
                        ToastUtil.showToast(RealNameAuthActivity.this, payCoreBaseResult.errorMsg);
                    }
                }
                break;
            case ValueConstants.PAY_CheckVerifyCode_ERROR:
                mNextBtn_03.setEnabled(true);
                mNextBtn_03.setBackgroundResource(R.drawable.btn_pay_orange_selector);
                ToastUtil.showToast(RealNameAuthActivity.this, StringUtil.handlerErrorCode(RealNameAuthActivity.this, msg.arg1));
                break;
            case ValueConstants.PAY_VerifyIdentity_SUCCESS:

                verifyIdentityResult = (VerifyIdentityResult) msg.obj;
                mNextBtn_02.setEnabled(true);
                mNextBtn_02.setBackgroundResource(R.drawable.btn_pay_orange_selector);
                if (verifyIdentityResult.success) {
                    if (!TextUtils.isEmpty(verifyIdentityResult.verifyIdentityCode)) {
                        doSendVerifyCode(type, mPhoneNum.getText().toString(), 1);
                    } else {
                        ToastUtil.showToast(RealNameAuthActivity.this, "身份认证失败");
                    }
                } else {
                    ToastUtil.showToast(RealNameAuthActivity.this, verifyIdentityResult.errorMsg);
                }
                break;
            case ValueConstants.PAY_VerifyIdentity_ERROR:
                mNextBtn_02.setEnabled(true);
                mNextBtn_02.setBackgroundResource(R.drawable.btn_pay_orange_selector);
                ToastUtil.showToast(RealNameAuthActivity.this, StringUtil.handlerErrorCode(RealNameAuthActivity.this, msg.arg1));
                break;

            case ValueConstants.PAY_GetBankNameList_SUCCESS:
                bankList = (BankNameList) msg.obj;
                if (bankList != null && bankList.bankNameList != null && bankList.bankNameList.size() != 0) {
                    showBankCardDialog(bankList.bankNameList, bankType);
                }
                break;
            case ValueConstants.PAY_GetBankNameList_ERROR:

                break;
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
                            RealNameAuthActivity.this.finish();
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
            if (page == 1) {
                showBack("是否放弃钱包激活");
            } else if (page == 2) {
                page = 1;
                mCardType.setText("");
                mPhoneNum.setText("");
                mIdCardNum.setText("");
                mRealName.setText("");
                auth_01.setVisibility(View.VISIBLE);
                auth_02.setVisibility(View.GONE);
                auth_03.setVisibility(View.GONE);
            } else if (page == 3) {
                page = 2;
                mCodeNum.setText("");
                auth_01.setVisibility(View.GONE);
                auth_02.setVisibility(View.VISIBLE);
                auth_03.setVisibility(View.GONE);
                mOrderTopView.setTitle("钱包激活", null);
                InputTools.KeyBoard(mCardNum, "close");
                mHandler.removeMessages(TIME);


            }
        }
        return false;
    }

}
