package com.quanyan.yhy.ui.wallet.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.base.utils.NavUtils;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.wallet.Controller.WalletController;
import com.quanyan.yhy.ui.wallet.view.WalletTopView;
import com.yhy.common.beans.net.model.paycore.BankCardList;
import com.yhy.common.constants.ValueConstants;

/**
 * Created with Android Studio.
 * Title:ForgetPasSelectCardActivity
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-9-20
 * Time:16:13
 * Version 1.0
 * Description:
 */
public class ForgetPasSelectCardActivity extends BaseActivity {

    private static final int PAGE = 1;
    private static final int MAX = 1000;

    private WalletTopView mTopView;


    @ViewInject(R.id.ll_cards)
    private LinearLayout mCrads;
    @ViewInject(R.id.add_card_layout)
    private RelativeLayout mAddCardLayout;
    private LayoutInflater mInflater;

    private WalletController mWalletController;

    public static void gotoForgetPasSelectCardActivity(Context context) {
        Intent intent = new Intent(context, ForgetPasSelectCardActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        mWalletController = new WalletController(this, mHandler);
        mInflater = LayoutInflater.from(this);
        getUserBindBankCard();
        initClick();
    }

    private void initClick() {
        mTopView.setBackClickListener(new WalletTopView.BackClickListener() {
            @Override
            public void back() {
                ForgetPasSelectCardActivity.this.finish();
            }
        });

        mAddCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.gotoBindCardActivity(ForgetPasSelectCardActivity.this, 2);
                ForgetPasSelectCardActivity.this.finish();
            }
        });
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(ForgetPasSelectCardActivity.this, R.layout.activity_forgetpas_selectcard, null);
    }

    @Override
    public View onLoadNavView() {
        mTopView = new WalletTopView(this);
        mTopView.setTitle("忘记支付密码", "安全支付");
        return mTopView;
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
                setCards(mBanks);
                break;
            case ValueConstants.PAY_PageQueryUserBindBankCard_ERROR:
                ToastUtil.showToast(ForgetPasSelectCardActivity.this, StringUtil.handlerErrorCode(ForgetPasSelectCardActivity.this, msg.arg1));
                break;
        }
    }

    private void setCards(final BankCardList mBanks) {
        if (mBanks == null) {
            return;
        }

        if (mBanks.bankCardList == null || mBanks.bankCardList.size() == 0) {
            return;
        }

        for (int i = 0; i < mBanks.bankCardList.size(); i++) {
            View convertView = mInflater.inflate(R.layout.item_forpas_selectcard, null);
            TextView mType = (TextView) convertView.findViewById(R.id.tv_card_type);
            final RelativeLayout rl = (RelativeLayout) convertView.findViewById(R.id.add_card_layout);
            if (!TextUtils.isEmpty(mBanks.bankCardList.get(i).bankName) && !TextUtils.isEmpty(mBanks.bankCardList.get(i).bankCardNo)) {
                String no = mBanks.bankCardList.get(i).bankCardNo;
                if (no.length() >= 4) {
                    mType.setText(mBanks.bankCardList.get(i).bankName + "  储蓄卡  " + "(" + no.substring(no.length() - 4, no.length()) + ")");
                } else {
                    mType.setText(mBanks.bankCardList.get(i).bankName + "  储蓄卡");
                }
            }
            rl.setTag(i);
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavUtils.gotoForgetPasBindCardActivity(ForgetPasSelectCardActivity.this, mBanks.bankCardList.get((Integer) rl.getTag()), 1, "");
                    ForgetPasSelectCardActivity.this.finish();
                }
            });
            mCrads.addView(convertView);
        }

    }


}
