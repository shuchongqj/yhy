package com.quanyan.yhy.ui.wallet.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.quanyan.base.BaseActivity;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.order.OrderTopView;
import com.quanyan.yhy.ui.wallet.WalletUtils;
import com.quanyan.yhy.ui.wallet.view.ViewHelper;
import com.yhy.common.beans.net.model.paycore.Bill;
import com.yhy.common.utils.SPUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with Android Studio.
 * Title:***Acitivity
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:田海波
 * Date:2016/6/11
 * Time:16:46
 * Version 2.0.1
 */
public class DetailAccountActivity extends BaseActivity {
    private OrderTopView mOrderTopView;
    @ViewInject(R.id.ll_detial_account)
    private LinearLayout llDetialAccount;
    private  Bill  bill;
    @ViewInject(R.id.tv_income)
    private   TextView  tvBill ;
    @ViewInject(R.id.tv_type)

    private   TextView  tvType;
    public static void gotoDetailAccountActivity(Context context,Bill bill) {
        Intent intent = new Intent();
        intent.setClass(context, DetailAccountActivity.class);
        intent.putExtra(SPUtils.BILL_DATA,bill);
        context.startActivity(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ViewUtils.inject(this);
        init();
        mOrderTopView.setBackClickListener(new OrderTopView.BackClickListener() {
            @Override
            public void clickBack() {
             finish();
            }
        });
    }

    @Override
    public void handleMessage(Message msg) {
        init();
    }

    public void init() {
        bill = (Bill) getIntent().getSerializableExtra(SPUtils.BILL_DATA);
        if(bill!=null){
            long account=  WalletUtils.getIncomeExpenceAccount(bill);
            tvBill.setText(account>0 ? "+"+ StringUtil.formatWalletMoneyNoFlg(account) : ""+StringUtil.formatWalletMoneyNoFlg(account) );
            tvType.setText(account>0 ? "收入金额" : "支出金额" );
            tvBill.setTextColor(account>0 ? getResources().getColor(R.color.neu_fa4619 ): getResources().getColor(R.color.neu_333333 ));
            Map<String, String> tempMap = new LinkedHashMap<>();
            tempMap.put("类        型", WalletUtils.getIncomeExpenceType(bill));
            tempMap.put("时        间", WalletUtils.getFormatData(bill.transTime));
            tempMap.put("交易单号", bill.transOrderId==null?"":bill.transOrderId);
            tempMap.put("钱包余额", "￥"+StringUtil.formatWalletMoneyNoFlg(bill.balance));
            tempMap.put("备        注", bill.remark==null?"":bill.remark);
            ViewHelper
                    .get()
                    .data(tempMap)
                    .parent(llDetialAccount)
                    .build(DetailAccountActivity.this);
        }



    }

    @Override
    public View onLoadContentView() {
        return View.inflate(this, R.layout.activity_detial_account, null);
    }

    @Override
    public View onLoadNavView() {
        mOrderTopView = new OrderTopView(this);
        mOrderTopView.setOrderTopTitle("明细详情");
        return mOrderTopView;
    }

    @Override
    public boolean isTopCover() {
        return false;
    }
}