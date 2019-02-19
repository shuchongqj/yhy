package com.quanyan.yhy.ui.spcart.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;

/**
 * Created with Android Studio.
 * Title:SpcartOrderBottomView
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-11-1
 * Time:11:15
 * Version 1.0
 * Description:
 */
public class SpcartOrderBottomView extends LinearLayout {

    private TextView mAllPriceTv;
    private TextView mDiscountPriceTv;
    private TextView mGotoDeatl;

    private Context mContext;

    private SpcartOrderBottomViewClick mSpcartOrderBottomViewClick;

    public SpcartOrderBottomView(Context context) {
        super(context);
        initView(context);
    }

    public SpcartOrderBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SpcartOrderBottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SpcartOrderBottomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        View.inflate(context, R.layout.view_spcartorder_bottom, this);
        mAllPriceTv = (TextView) this.findViewById(R.id.tv_all_price);
        mDiscountPriceTv = (TextView) this.findViewById(R.id.tv_discount_price);
        mGotoDeatl = (TextView) this.findViewById(R.id.tv_go_deal);
        mDiscountPriceTv.setVisibility(View.GONE);
        initClick();
    }

    private void initClick() {
        mGotoDeatl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSpcartOrderBottomViewClick != null) {
                    mSpcartOrderBottomViewClick.gotoDeal();
                }
            }
        });
    }

    public void setAllPrice(long totalPrice) {
//        mAllPriceTv.setText(StringUtil.converRMb2YunWithFlag(mContext, totalPrice));
        mAllPriceTv.setText(StringUtil.pointToYuan(totalPrice));
    }

    public void setDiscountPrice(long discountPrice) {
        if (discountPrice <= 0) {
            mDiscountPriceTv.setVisibility(View.GONE);
        } else {
            mDiscountPriceTv.setVisibility(View.VISIBLE);
//            mDiscountPriceTv.setText("已优惠" + StringUtil.converRMb2YunWithFlag(mContext, discountPrice));
            mDiscountPriceTv.setText("已优惠" + StringUtil.pointToYuanOne(discountPrice));
        }
    }

    public void setSpcartOrderBottomViewClickListener(SpcartOrderBottomViewClick mSpcartOrderBottomViewClick) {
        this.mSpcartOrderBottomViewClick = mSpcartOrderBottomViewClick;
    }

    public interface SpcartOrderBottomViewClick {
        void gotoDeal();
    }

}
