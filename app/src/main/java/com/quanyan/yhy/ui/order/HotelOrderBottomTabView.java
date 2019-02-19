package com.quanyan.yhy.ui.order;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.harwkin.nb.camera.TimeUtil;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;

/**
 * Created with Android Studio.
 * Title:HotelOrderBottomTabView
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-5-18
 * Time:14:03
 * Version 1.0
 * Description:
 */
public class HotelOrderBottomTabView extends LinearLayout implements View.OnClickListener {

    private TextView tv_order_price;
    private Button tv_order_submit;
    private LinearLayout mLayoutDetails;
    private SubmitClick submitClick;
    private RelativeLayout rl_discount;
    private TextView tv_order_disprice;

    public HotelOrderBottomTabView(Context context) {
        super(context);
        init(context);
    }

    public HotelOrderBottomTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HotelOrderBottomTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HotelOrderBottomTabView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    private void init(Context context) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        View view = mLayoutInflater.inflate(R.layout.hotelorderbottomtabview, null);

        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        rlp.bottomMargin = 0;
        rlp.topMargin = 0;
        rlp.leftMargin = 0;
        rlp.rightMargin = 0;

        tv_order_price = (TextView) view.findViewById(R.id.tv_order_price);
        tv_order_submit = (Button) view.findViewById(R.id.tv_order_submit);
        mLayoutDetails = (LinearLayout) view.findViewById(R.id.ll_sales_details);
        rl_discount = (RelativeLayout) view.findViewById(R.id.rl_discount);
        tv_order_disprice = (TextView) view.findViewById(R.id.tv_order_disprice);
        tv_order_submit.setOnClickListener(this);
        mLayoutDetails.setOnClickListener(this);
        addView(view, rlp);
    }


    public void setBottomPrice(String price) {
        if (!TextUtils.isEmpty(price)) {
            tv_order_price.setText("￥" + price);
        } else {
            tv_order_price.setText("");
        }
    }

    public void setSubmitText(String title) {
        if (!TextUtils.isEmpty(title)) {
            tv_order_submit.setText(title);
        }
    }

    public void setDetailsLayoutGone() {
        mLayoutDetails.setVisibility(View.GONE);
    }

    public void setDiscountLayout(long disPrice) {
        if (disPrice <= 0) {
            rl_discount.setVisibility(View.GONE);
        } else {
            rl_discount.setVisibility(View.VISIBLE);
//            tv_order_disprice.setText("已优惠" + StringUtil.converRMb2YunWithFlag(getContext(), disPrice));
            tv_order_disprice.setText("已优惠" + StringUtil.pointToYuanOne(disPrice*10));
        }
    }

    public void setSubmitClickListener(SubmitClick submitClick) {
        this.submitClick = submitClick;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_order_submit:
                if (TimeUtil.isFastDoubleClick()) {
                    return;
                }
                if (submitClick != null) {
                    submitClick.submit();
                }
                break;
            case R.id.ll_sales_details:
                if (TimeUtil.isFastDoubleClick()) {
                    return;
                }
                if (submitClick != null) {
                    submitClick.gotoSalesDetails();
                }
                break;
        }
    }

    public interface SubmitClick {
        //点击提交按钮
        void submit();

        //点击明细进入明细详情界面
        void gotoSalesDetails();
    }

}
