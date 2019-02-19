package com.quanyan.yhy.ui.wallet.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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
import com.yhy.common.beans.net.model.paycore.BankCard;
import com.yhy.common.beans.net.model.paycore.BankCardList;
import com.yhy.common.constants.ValueConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio.
 * Title:SelectCardActivity
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-8-10
 * Time:16:19
 * Version 1.0
 * Description: 选择银行卡
 */
public class SelectCardActivity extends BaseActivity {

    private static final int PAGE = 1;
    private static final int MAX = 1000;
    private static final String BANKCARD = "BankCard";

    private WalletTopView mOrderTopView;

    @ViewInject(R.id.ll_cards)
    private LinearLayout mCrads;
    @ViewInject(R.id.add_card_layout)
    private RelativeLayout mAddCardLayout;
    private LayoutInflater mInflater;

    private WalletController mWalletController;
    private List<BankCardFlag> mB;
    private BankCard bankCard;

    public static void gotoSelectCardActivity(Activity context, int requestCode, BankCard bankCard) {
        Intent intent = new Intent(context, SelectCardActivity.class);
        intent.putExtra(BANKCARD, bankCard);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        bankCard = (BankCard) getIntent().getSerializableExtra(BANKCARD);
        mWalletController = new WalletController(this, mHandler);
        mInflater = LayoutInflater.from(this);
        mB = new ArrayList<BankCardFlag>();
        initClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserBindBankCard();
    }

    private void setCards(BankCardList mBanks) {
        if (mBanks == null) {
            return;
        }

        if (mBanks.bankCardList == null || mBanks.bankCardList.size() == 0) {
            return;
        }

        if (mB != null && mB.size() != 0) {
            mB.clear();
        }

        for (int i = 0; i < mBanks.bankCardList.size(); i++) {
            BankCardFlag flag = new BankCardFlag();
            flag.setmBankCard(mBanks.bankCardList.get(i));
            if (bankCard == null) {
                if (i == 0) {
                    flag.setIsSelect(true);
                } else {
                    flag.setIsSelect(false);
                }
            } else {
                if (bankCard.bindCardId == mBanks.bankCardList.get(i).bindCardId) {
                    flag.setIsSelect(true);
                } else {
                    flag.setIsSelect(false);
                }
            }
            mB.add(flag);
        }

        setView(mB);
    }

    private void setView(List<BankCardFlag> mList) {
        if (mList == null || mList.size() == 0) {
            return;
        }

        if (mCrads != null && mCrads.getChildCount() != 0) {
            mCrads.removeAllViews();
        }

        for (int i = 0; i < mList.size(); i++) {
            View convertView = mInflater.inflate(R.layout.item_select_card, null);
            TextView mType = (TextView) convertView.findViewById(R.id.tv_card_type);
            ImageView img = (ImageView) convertView.findViewById(R.id.pay_select_im);
            final RelativeLayout mRl = (RelativeLayout) convertView.findViewById(R.id.rl_item);
            if (!TextUtils.isEmpty(mList.get(i).getmBankCard().bankName) && !TextUtils.isEmpty(mList.get(i).getmBankCard().bankCardNo)) {
                String no = mList.get(i).getmBankCard().bankCardNo;
                if (no.length() >= 4) {
                    mType.setText(mList.get(i).getmBankCard().bankName + "(" + no.substring(no.length() - 4, no.length()) + ")");
                } else {
                    mType.setText(mList.get(i).getmBankCard().bankName);
                }
            }
            if (mList.get(i).isSelect) {
                img.setImageResource(R.mipmap.ic_checked);
            } else {
                img.setImageResource(R.mipmap.ic_uncheck);
            }

            mRl.setTag(i);

            mRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetPayList((Integer) mRl.getTag());
                }
            });
            mCrads.addView(convertView);
        }
    }

    private void initClick() {
        mOrderTopView.setBackClickListener(new WalletTopView.BackClickListener() {
            @Override
            public void back() {
                SelectCardActivity.this.finish();
            }
        });

        mAddCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.gotoVerifyPassWordActivity(SelectCardActivity.this);
            }
        });
    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_selectcard, null);
    }

    @Override
    public View onLoadNavView() {
        mOrderTopView = new WalletTopView(this);
        mOrderTopView.setTitle("选择银行卡", "安全支付");
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
                setCards(mBanks);
                break;
            case ValueConstants.PAY_PageQueryUserBindBankCard_ERROR:
                ToastUtil.showToast(SelectCardActivity.this, StringUtil.handlerErrorCode(SelectCardActivity.this, msg.arg1));
                break;
        }
    }

    /**
     * 重置数据刷新界面
     *
     * @param postion
     */
    private void resetPayList(int postion) {
        for (int i = 0; i < mB.size(); i++) {
            if (postion == i) {
                mB.get(i).setIsSelect(true);
            } else {
                mB.get(i).setIsSelect(false);
            }
        }
        setView(mB);
        back(mB.get(postion).getmBankCard());
    }


    private void back(BankCard mBankCard) {
        Intent intent = getIntent();
        intent.putExtra("BankCard", mBankCard);
        intent.putExtra("CardType", "old");
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


    class BankCardFlag {
        BankCard mBankCard;
        boolean isSelect;

        public BankCard getmBankCard() {
            return mBankCard;
        }

        public void setmBankCard(BankCard mBankCard) {
            this.mBankCard = mBankCard;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setIsSelect(boolean isSelect) {
            this.isSelect = isSelect;
        }
    }

}
