package com.quanyan.yhy.ui.tab.homepage.order;

import android.view.View;
import android.widget.LinearLayout;

import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.IdentityType;
import com.quanyan.yhy.ui.views.OrderDetailsItemView;
import com.yhy.common.beans.net.model.tm.OrderDetailResult;
import com.yhy.common.beans.net.model.tm.TmUserContactInfo;

/**
 * Created with Android Studio.
 * Title:TravelOrderDetailsActivity
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/5/18
 * Time:10:54
 * Version 1.0
 */
public class TravelOrderDetailsActivity extends MyBaseOrderDetailsActivity {

    private LinearLayout mLLSku;

    private OrderDetailsItemView mPrice;
    private OrderDetailsItemView mPriceDiscount;
    private OrderDetailsItemView mPriceShould;
    private OrderDetailsItemView mPricePoint;

    private LinearLayout mLLGuest;

    private LinearLayout mLLContact;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_travel_order_details;
    }

    @Override
    protected void initChildView() {
        mLLSku = (LinearLayout) findViewById(R.id.linearLayout_sku);

        mPrice = (OrderDetailsItemView) findViewById(R.id.tv_order_price);
        mPricePoint = (OrderDetailsItemView) findViewById(R.id.tv_order_price_point);
        mPriceDiscount = (OrderDetailsItemView) findViewById(R.id.tv_order_discount);
        mPriceShould = (OrderDetailsItemView) findViewById(R.id.tv_order_price_should);
        mPriceShould.mright.setTextSize(14);
        mPriceShould.mright.setTextColor(getResources().getColor(R.color.main));
        mLLGuest = (LinearLayout) findViewById(R.id.linearLayout_guest);
        mLLContact = (LinearLayout) findViewById(R.id.linearLayout_contact);
    }

    @Override
    protected void reloadChildData(OrderDetailResult detail) {
        mLLSku.removeAllViews();
        addSkuData(mLLSku, detail.mainOrder.detailOrders);
        mPrice.setMessage(getString(R.string.order_amount), StringUtil.converRMb2YunWithFlag(TravelOrderDetailsActivity.this,detail.mainOrder.originalTotalFee));
        if (detail.mainOrder.voucherDiscountFee <= 0) {
            mPriceDiscount.setVisibility(View.GONE);
        } else {
            mPriceDiscount.setVisibility(View.VISIBLE);
            mPriceDiscount.setMessage(getString(R.string.coupon), "-" + StringUtil.converRMb2YunWithFlag(TravelOrderDetailsActivity.this,detail.mainOrder.voucherDiscountFee));
        }

        if (detail.mainOrder.usePoint <= 0) {
            mPricePoint.setVisibility(View.GONE);
        } else {
            mPricePoint.setVisibility(View.VISIBLE);
            mPricePoint.setMessage(getString(R.string.point), "-" + StringUtil.converRMb2YunWithFlag(TravelOrderDetailsActivity.this,detail.mainOrder.usePoint));
        }

        mPriceShould.setMessage(getString(R.string.amount_payable), StringUtil.converRMb2YunWithFlag(TravelOrderDetailsActivity.this,detail.mainOrder.totalFee));

        mLLGuest.removeAllViews();
        if (detail.mainOrder.touristList != null && detail.mainOrder.touristList.size() > 0) {
            for (TmUserContactInfo info : detail.mainOrder.touristList) {
                mLLGuest.addView(createOrderDetailsItemView(getString(R.string.visitor), info.name));
                mLLGuest.addView(createOrderDetailsItemView(IdentityType.showIdType(this,info.CertificateType), info.idNumber));
            }
            mLLGuest.setVisibility(View.VISIBLE);
        } else {
            mLLGuest.setVisibility(View.GONE);
        }

        mLLContact.removeAllViews();
        if (detail.mainOrder.contactInfo != null) {
            mLLContact.addView(createOrderDetailsItemView(getString(R.string.contact), detail.mainOrder.contactInfo.name));
            mLLContact.addView(createOrderDetailsItemView(getString(R.string.contact_phone), detail.mainOrder.contactInfo.phoneNum));
            if (!StringUtil.isEmpty(detail.mainOrder.email)) {
                mLLContact.addView(createOrderDetailsItemView(getString(R.string.line_order_email_title), detail.mainOrder.email));
            }
            mLLContact.setVisibility(View.VISIBLE);
        } else {
            mLLContact.setVisibility(View.GONE);
        }
    }

}
