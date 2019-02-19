package com.quanyan.yhy.ui.order;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanyan.yhy.R;

/**
 * Created with Android Studio.
 * Title:OrderTopView
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-5-31
 * Time:10:39
 * Version 1.0
 * Description:
 */
public class OrderTopView extends LinearLayout implements View.OnClickListener {

    private LinearLayout mBackLayout;
    private TextView mTitleTextView;
    private LinearLayout rl_right;
    private TextView tv_right;

    private BackClickListener mBackClickListener;
    private RightClickListener mRightClickListener;

    public OrderTopView(Context context) {
        super(context);
        initView(context);
    }

    public OrderTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public OrderTopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OrderTopView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.order_top_view, this);
        mBackLayout = (LinearLayout) this.findViewById(R.id.ll_back);
        mTitleTextView = (TextView) this.findViewById(R.id.tv_title);
        rl_right = (LinearLayout) this.findViewById(R.id.rl_right);
        tv_right = (TextView) this.findViewById(R.id.tv_right);
        mBackLayout.setOnClickListener(this);
        rl_right.setOnClickListener(this);
    }

    public void setOrderTopTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            mTitleTextView.setText(title);
        } else {
            mTitleTextView.setText("");
        }
    }

    public void setRightViewVisible(String str) {
        rl_right.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(str)) {
            tv_right.setText(str);
        }
    }

    public void setBackClickListener(BackClickListener mBackClickListener) {
        this.mBackClickListener = mBackClickListener;
    }

    public void setRightClickListener(RightClickListener mRightClickListener) {
        this.mRightClickListener = mRightClickListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                if (mBackClickListener != null) {
                    mBackClickListener.clickBack();
                }
                break;
            case R.id.rl_right:
                if (mRightClickListener != null) {
                    mRightClickListener.rightClick();
                }
                break;
        }
    }

    public void setRightTvColor(int color) {
        tv_right.setTextColor(color);
    }

    public void setRightLayoutEnable(boolean enable) {
        rl_right.setEnabled(enable);
    }

    public interface BackClickListener {
        void clickBack();
    }

    public interface RightClickListener {
        void rightClick();
    }
}
