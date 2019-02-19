package com.quanyan.yhy.ui.wallet.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.wallet.view.WalletTopView;
import com.yhy.common.beans.net.model.paycore.BankCard;

/**
 * Created with Android Studio.
 * Title:WithDrawDetailsActivity
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-8-11
 * Time:13:09
 * Version 1.0
 * Description: 提现详情
 */
public class WithDrawDetailsActivity extends BaseActivity {

    private static final String BANKCARD = "BANKCARD";
    private static final String MONEY = "MONEY";

    private WalletTopView mOrderTopView;

    @ViewInject(R.id.ll_content)
    private LinearLayout mContent;
    @ViewInject(R.id.btn_config)
    private Button mConfigBtn;
    private LayoutInflater mInflater;

    private BankCard mBankCard;
    private long money;


    public static void gotoWithDrawDetailsActivity(Context context, BankCard mBankCard, long money) {
        Intent intent = new Intent(context, WithDrawDetailsActivity.class);
        intent.putExtra(BANKCARD, mBankCard);
        intent.putExtra(MONEY, money);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        mInflater = LayoutInflater.from(this);
        mBankCard = (BankCard) getIntent().getSerializableExtra(BANKCARD);
        money = getIntent().getLongExtra(MONEY, -1);
        setContent();
        initClick();
    }

    private void setContent() {
        for (int i = 0; i < 2; i++) {
            View v = mInflater.inflate(R.layout.item_withdraw_details, null);
            TextView t1 = (TextView) v.findViewById(R.id.tv_card_type);
            TextView t2 = (TextView) v.findViewById(R.id.tv_con);
            if (i == 0) {
                t1.setText("银 行 卡");
                if (mBankCard != null && !TextUtils.isEmpty(mBankCard.bankName) && !TextUtils.isEmpty(mBankCard.bankCardNo)) {
                    String no = mBankCard.bankCardNo;
                    if (no.length() > 4) {
                        t2.setText(mBankCard.bankName + "   尾号 " + no.substring(no.length() - 4, no.length()));
                    } else {
                        t2.setText(mBankCard.bankName);
                    }
                } else {

                }
            } else if (i == 1) {
                t1.setText("提现金额");
                t2.setText(StringUtil.formatWalletMoney(this, money));
            }
            mContent.addView(v);
        }
    }

    private void initClick() {
        mConfigBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WithDrawDetailsActivity.this.finish();
            }
        });
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_withdraw_details, null);
    }

    @Override
    public View onLoadNavView() {
        mOrderTopView = new WalletTopView(this);
        mOrderTopView.setTitle("提现详情", "安全支付");
        mOrderTopView.setBackGone();
        return mOrderTopView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }
}
