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
import android.view.View;
import android.widget.Button;
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
import com.quanyan.yhy.ui.wallet.view.WalletXEditText;
import com.yhy.common.beans.net.model.paycore.BankCard;
import com.yhy.common.constants.ValueConstants;
import com.yhy.common.utils.SPUtils;

/**
 * Created with Android Studio.
 * Title:BindCardActivity
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-8-11
 * Time:15:54
 * Version 1.0
 * Description:
 */
public class BindCardActivity extends BaseActivity {

    private static final String TYPE = "TYPE";

    private WalletTopView mOrderTopView;

    @ViewInject(R.id.et_card_num)
    private WalletXEditText mCardNum;
    @ViewInject(R.id.btn_next_01)
    private Button mNextBtn_01;
    @ViewInject(R.id.tv_include01_tips)
    private TextView mInclude01Tips;
    @ViewInject(R.id.et_card_name)
    private TextView mCardName;

    private WalletController mWalletController;

    private int mType;

    /**
     * @param context
     * @param type    1是绑定银行卡,2是忘记密码
     */
    public static void gotoBindCardActivity(Context context, int type) {
        Intent intent = new Intent(context, BindCardActivity.class);
        intent.putExtra(TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        mType = getIntent().getIntExtra(TYPE, -1);
        mWalletController = new WalletController(this, mHandler);
        mCardNum.setPattern(new int[]{4, 4, 4, 4, 3});
        mCardNum.setSeparator(" ");
        if (mType == 1) {
            mOrderTopView.setTitle("添加银行卡", "安全支付");
            mOrderTopView.setRBackVisibility();
            mInclude01Tips.setText("请绑定持卡人的银行卡");
            mCardName.setText(WalletUtils.formatUserName(SPUtils.getWalletName(this)));
        } else if (mType == 2) {
            mOrderTopView.setTitle("忘记支付密码", "安全支付");
            mInclude01Tips.setText("填写持卡人本人的银行卡号");
            mCardName.setText(WalletUtils.formatUserName(SPUtils.getWalletName(this)));
        }
        mNextBtn_01.setBackgroundResource(R.drawable.btn_pay_unselect);
        mNextBtn_01.setEnabled(false);
        initClick();
    }

    private void initClick() {
        mOrderTopView.setBackClickListener(new WalletTopView.BackClickListener() {
            @Override
            public void back() {
                if (mType == 1) {
                    showBack("是否放弃绑定银行卡");
                } else {
                    showBack("是否放弃找回密码");
                }
            }
        });

        mNextBtn_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doGetBankMsg(mCardNum.getNonSeparatorText());
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
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.bindcard_include_01, null);
    }

    @Override
    public View onLoadNavView() {
        mOrderTopView = new WalletTopView(this);
        return mOrderTopView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }

    //获取银行卡信息
    private void doGetBankMsg(String cardNo) {
        if (TextUtils.isEmpty(cardNo)) {
            return;
        }
        mWalletController.doGetBankCardByCardNo(cardNo, this);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case ValueConstants.PAY_BankCardByCardNo_SUCCESS:
                BankCard bankCard = (BankCard) msg.obj;
                if (bankCard != null) {
                    if (mType == 1) {
                        if (bankCard.isBind) {
                            ToastUtil.showToast(BindCardActivity.this, "该银行卡已绑定");
                        } else {
                            NavUtils.gotoBindCardInforActivity(BindCardActivity.this, bankCard, mCardNum.getNonSeparatorText());
                        }
                    } else {
                        if (bankCard.isBind) {
                            ToastUtil.showToast(BindCardActivity.this, "该银行卡已绑定");
                        } else {
                            NavUtils.gotoForgetPasBindCardActivity(BindCardActivity.this, bankCard, 0, mCardNum.getNonSeparatorText());
                        }
                    }
                } else {
                    if (mType == 1) {
                        NavUtils.gotoBindCardInforActivity(BindCardActivity.this, null, mCardNum.getNonSeparatorText());
                    } else {
                        NavUtils.gotoForgetPasBindCardActivity(BindCardActivity.this, null, 0, mCardNum.getNonSeparatorText());
                    }
                }
                break;
            case ValueConstants.PAY_BankCardByCardNo_ERROR:
                ToastUtil.showToast(BindCardActivity.this, StringUtil.handlerErrorCode(BindCardActivity.this, msg.arg1));
                break;
        }
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
                            BindCardActivity.this.finish();
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
            if (mType == 1) {
                showBack("是否放弃绑定银行卡");
            } else {
                showBack("是否放弃找回密码");
            }
        }
        return false;
    }
}
