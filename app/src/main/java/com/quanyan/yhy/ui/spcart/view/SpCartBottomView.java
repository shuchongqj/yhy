package com.quanyan.yhy.ui.spcart.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.yhy.common.beans.net.model.tm.CartInfoListResult;

/**
 * Created with Android Studio.
 * Title:SpCartBottomView
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-10-21
 * Time:11:20
 * Version 1.0
 * Description:
 */
public class SpCartBottomView extends LinearLayout {

    private ImageView mAllCheckIv;
    private TextView mAllPriceTv;
    private TextView mAllIntegralTv;
    private TextView mGotoDeal;
    private Context mContext;

    private SpCartBottomClick mSpCartBottomClick;


    public SpCartBottomView(Context context) {
        super(context);
        initView(context);
    }

    public SpCartBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SpCartBottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SpCartBottomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        View.inflate(context, R.layout.spcart_bottom_view, this);
        mAllCheckIv = (ImageView) this.findViewById(R.id.iv_spcart_state);
        mAllPriceTv = (TextView) this.findViewById(R.id.tv_spcart_all_price);
        mAllIntegralTv = (TextView) this.findViewById(R.id.tv_spcart_all_integral);
        mGotoDeal = (TextView) this.findViewById(R.id.tv_go_deal);

        initClick();
    }

    private void initClick() {
        mAllCheckIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSpCartBottomClick != null) {
                    mSpCartBottomClick.checked();
                }
            }
        });

        mGotoDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSpCartBottomClick != null) {
                    if (count > 0) {
                        mSpCartBottomClick.gotoDeal();
                    } else {
                        ToastUtil.showToast(mContext, "您还没有选择商品哦~");
                    }
                }
            }
        });
    }

    private int count = 0;

    public void setBottomView(CartInfoListResult info) {
        if (info == null) {
            count = 0;
            mGotoDeal.setEnabled(true);
            mGotoDeal.setBackgroundColor(getResources().getColor(R.color.tc_e1e1e1));
            mAllPriceTv.setText("￥0");
            mAllIntegralTv.setText("￥0");
            mGotoDeal.setText("去结算(" + 0 + ")");
            return;
        }
        count = info.count;
        if (info.count > 0) {
            mGotoDeal.setEnabled(true);
            mGotoDeal.setBackgroundColor(getResources().getColor(R.color.red_ying));
            mAllPriceTv.setText(StringUtil.pointToYuan(info.actualTotalFee));
//            mAllPriceTv.setText(StringUtil.converRMb2YunWithFlag(mContext, info.actualTotalFee));
//            mAllIntegralTv.setText(StringUtil.converRMb2YunWithFlag(mContext, info.pointTotalFee));
            mAllIntegralTv.setText(StringUtil.pointToYuan(info.pointTotalFee*10));
            mGotoDeal.setText("去结算(" + info.count + ")");
        } else {
            mGotoDeal.setEnabled(true);
            mGotoDeal.setBackgroundColor(getResources().getColor(R.color.red_ying));
            mAllPriceTv.setText("￥0");
            mAllIntegralTv.setText("￥0");
            mGotoDeal.setText("去结算(" + info.count + ")");
        }
    }

    public void setBottomChecked(boolean isChecked) {
        if (isChecked) {
//            mAllCheckIv.setImageResource(R.mipmap.spcart_checked);
            mAllCheckIv.setImageResource(R.mipmap.ic_share_cb_checked);
        } else {
            mAllCheckIv.setImageResource(R.mipmap.spcart_unchecked);
        }
    }

    public void setBottomClickListener(SpCartBottomClick mSpCartBottomClick) {
        this.mSpCartBottomClick = mSpCartBottomClick;
    }

    public interface SpCartBottomClick {
        void checked();

        void gotoDeal();
    }

}
