package com.quanyan.yhy.ui.spcart.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.yhy.common.beans.net.model.common.address.MyAddressContentInfo;
import com.yhy.common.beans.net.model.tm.Address;

/**
 * Created with Android Studio.
 * Title:SpcartOrderHeadView
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-11-1
 * Time:11:31
 * Version 1.0
 * Description:
 */
public class SpcartOrderHeadView extends LinearLayout {

    private RelativeLayout mNoAddressLayout;
    private RelativeLayout mHasAddressLayout;
    private ImageView mAddressImage;
    private ImageView mArrowImage;
    private TextView mReciverName;
    private TextView mReciverTips;
    private TextView mReciverTel;
    private TextView mReciverAddress;

    private TextView mIntegralTipsTv;
    private TextView mIntegralMoneyTv;
    private TextView mMimeUsePoint;
    private CheckBox mSwithCheckBox;

    private SpcartOrderHeadViewClick mSpcartOrderHeadViewClick;

    public SpcartOrderHeadView(Context context) {
        super(context);
        initView(context);
    }

    public SpcartOrderHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SpcartOrderHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SpcartOrderHeadView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.spcart_order_head_view, this);
        mNoAddressLayout = (RelativeLayout) this.findViewById(R.id.rl_noadress);
        mHasAddressLayout = (RelativeLayout) this.findViewById(R.id.rl_hasadress);
        mAddressImage = (ImageView) this.findViewById(R.id.iv_adress);
        mArrowImage = (ImageView) this.findViewById(R.id.iv_arrow);
        mReciverName = (TextView) this.findViewById(R.id.tv_rcadress_name);
        mReciverTips = (TextView) this.findViewById(R.id.tv_rcadress_tips);
        mReciverTel = (TextView) this.findViewById(R.id.tv_rcadress_tel);
        mReciverAddress = (TextView) this.findViewById(R.id.tv_rcadress_adress);
        mIntegralTipsTv = (TextView) this.findViewById(R.id.tv_integral_tips);
        mIntegralMoneyTv = (TextView) this.findViewById(R.id.tv_integral_money);
        mMimeUsePoint = (TextView) this.findViewById(R.id.mime_use_point);
        mSwithCheckBox = (CheckBox) this.findViewById(R.id.cb_switch);
        mAddressImage.setVisibility(View.VISIBLE);
        mAddressImage.setImageResource(R.mipmap.spcart_location_image);
        initClick();
    }

    private void initClick() {
        mNoAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(mSpcartOrderHeadViewClick != null){
                  mSpcartOrderHeadViewClick.noAddressClick();
              }
            }
        });

        mHasAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSpcartOrderHeadViewClick != null){
                    mSpcartOrderHeadViewClick.arrowAddressClick();
                }
            }
        });

        mSwithCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(mSpcartOrderHeadViewClick != null){
                    mSpcartOrderHeadViewClick.swithCheck(isChecked);
                }
            }
        });

    }

    private String province = "";
    private String city = "";
    private String area = "";

    public void setAddress(Address mAddress) {
        if (mAddress != null) {
            mNoAddressLayout.setVisibility(View.GONE);
            mHasAddressLayout.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(mAddress.recipientName)) {
                mReciverName.setText(mAddress.recipientName);
            } else {
                mReciverName.setText("");
            }
            if (!TextUtils.isEmpty(mAddress.recipientPhone)) {
                mReciverTel.setText(mAddress.recipientPhone);
            } else {
                mReciverTel.setText("");
            }

            //详细地址
            if (!StringUtil.isEmpty(mAddress.detailAddress)) {
                if (!StringUtil.isEmpty(mAddress.province)) {
                    province = mAddress.province;
                }
                if (!StringUtil.isEmpty(mAddress.city) && !mAddress.city.equals(mAddress.province)) {
                    city = mAddress.city;
                } else {
                    city = "";
                }
                if (!StringUtil.isEmpty(mAddress.area)) {
                    area = mAddress.area;
                }
                String newAddress = province + city + area + mAddress.detailAddress;
                mReciverAddress.setText(newAddress);
            }
            if (mAddress.isDefault) {
                mReciverTips.setText("默认");
            } else {
                mReciverTips.setText("");
            }
        } else {
            mNoAddressLayout.setVisibility(View.VISIBLE);
            mHasAddressLayout.setVisibility(View.GONE);
        }
    }

    public void setMyInfoAddress(MyAddressContentInfo mAddress) {
        if (mAddress != null) {
            mNoAddressLayout.setVisibility(View.GONE);
            mHasAddressLayout.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(mAddress.recipientsName)) {
                mReciverName.setText(mAddress.recipientsName);
            } else {
                mReciverName.setText("");
            }
            if (!TextUtils.isEmpty(mAddress.recipientsPhone)) {
                mReciverTel.setText(mAddress.recipientsPhone);
            } else {
                mReciverTel.setText("");
            }
            //详细地址
            if (!StringUtil.isEmpty(mAddress.detailAddress)) {
                if (!StringUtil.isEmpty(mAddress.province)) {
                    province = mAddress.province;
                }
                if (!StringUtil.isEmpty(mAddress.city) && !mAddress.city.equals(mAddress.province)) {
                    city = mAddress.city;
                } else {
                    city = "";
                }
                if (!StringUtil.isEmpty(mAddress.area)) {
                    area = mAddress.area;
                }
                String newAddress = province + city + area + mAddress.detailAddress;
                mReciverAddress.setText(newAddress);
            }
            if (mAddress.isDefault.equals("1")) {
                mReciverTips.setText("默认");
            } else {
                mReciverTips.setText("");
            }
        } else {
            mNoAddressLayout.setVisibility(View.VISIBLE);
            mHasAddressLayout.setVisibility(View.GONE);
        }
    }


    public void setNoAddressLayoutVisibility(boolean isVisibility){
        if(isVisibility){
            mNoAddressLayout.setVisibility(View.VISIBLE);
            mHasAddressLayout.setVisibility(View.GONE);
        }else{
            mNoAddressLayout.setVisibility(View.GONE);
            mHasAddressLayout.setVisibility(View.VISIBLE);
        }
    }

    public void setIntegralTips(String str) {
        if (!TextUtils.isEmpty(str)) {
            mIntegralTipsTv.setText(str);
        }
    }

    public void setIntegralMoney(String str) {
        if (!TextUtils.isEmpty(str)) {
            mIntegralMoneyTv.setText(str);
        }
    }

    public void setSwitchCheckBoxChecked(boolean isCheck) {
        if (isCheck) {
            mSwithCheckBox.setChecked(true);
        } else {
            mSwithCheckBox.setChecked(false);
        }
    }

    public void setSwitchCheckBoxClickable(boolean clickable) {
        if (clickable) {
            mSwithCheckBox.setClickable(true);
        } else {
            mSwithCheckBox.setClickable(false);
        }
    }

    public boolean getSwitchChecked() {
        return mSwithCheckBox.isChecked();
    }

    public void setMimeUsePoint(String str) {
        if (!TextUtils.isEmpty(str)) {
            mMimeUsePoint.setText(str);
        }
    }

    public void setSpcartOrderHeadViewClickListener(SpcartOrderHeadViewClick mSpcartOrderHeadViewClick){
         this.mSpcartOrderHeadViewClick = mSpcartOrderHeadViewClick;
    }

    public interface SpcartOrderHeadViewClick {
        void noAddressClick();

        void arrowAddressClick();

        void swithCheck(boolean isChecked);
    }

}
