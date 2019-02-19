package com.quanyan.yhy.ui.wallet.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanyan.yhy.R;

/**
 * Created with Android Studio.
 * Title:WalletTopView
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-9-14
 * Time:11:25
 * Version 1.0
 * Description:
 */
public class WalletTopView extends LinearLayout {

    private LinearLayout mLBack;
    private RelativeLayout mRBack;
    private TextView mTitle;
    private TextView mSecondTitle;
    private LinearLayout mRLinearLayout;
    private TextView mRTextView;

    private BackClickListener mBackClickListener;
    private RightClickListener mRightClickListener;

    public WalletTopView(Context context) {
        super(context);
        initView(context);
    }

    public WalletTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public WalletTopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WalletTopView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.wallet_top_view, this);
        mLBack = (LinearLayout) this.findViewById(R.id.ll_back);
        mRBack = (RelativeLayout) this.findViewById(R.id.rl_back);
        mTitle = (TextView) this.findViewById(R.id.tv_title);
        mSecondTitle = (TextView) this.findViewById(R.id.tv_secondtitle);
        mRLinearLayout = (LinearLayout) this.findViewById(R.id.rl_right);
        mRTextView = (TextView) this.findViewById(R.id.tv_right);
        mRLinearLayout.setVisibility(View.GONE);
        mRBack.setVisibility(View.GONE);
        initClick();
    }

    private void initClick() {
        mLBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBackClickListener != null) {
                    mBackClickListener.back();
                }
            }
        });

        mRBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBackClickListener != null) {
                    mBackClickListener.back();
                }
            }
        });

        mRLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRightClickListener != null) {
                    mRightClickListener.rightClick();
                }
            }
        });
    }

    public void setLBackVisibility() {
        if (mLBack != null) {
            mLBack.setVisibility(View.VISIBLE);
        }
        if (mRBack != null) {
            mRBack.setVisibility(View.GONE);
        }
    }

    public void setRBackVisibility() {
        if (mLBack != null) {
            mLBack.setVisibility(View.GONE);
        }
        if (mRBack != null) {
            mRBack.setVisibility(View.VISIBLE);
        }
    }

    public void setBackGone(){
        if (mLBack != null) {
            mLBack.setVisibility(View.GONE);
        }
        if (mRBack != null) {
            mRBack.setVisibility(View.GONE);
        }
    }

    public void setRightTextView(@Nullable String text) {
        if (TextUtils.isEmpty(text)) {
            setRLinearLayoutGone();
        } else {
            mRTextView.setText(text);
            setRLinearLayoutVisibility();
        }
    }

    public void setRLinearLayoutVisibility() {
        if (mRLinearLayout != null) {
            mRLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    public void setRLinearLayoutGone() {
        if (mRLinearLayout != null) {
            mRLinearLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 设置标题
     *
     * @param title
     * @param subTitle
     */
    public void setTitle(String title, @Nullable String subTitle) {
        mTitle.setText(title);
        if (TextUtils.isEmpty(subTitle)) {
            mSecondTitle.setVisibility(View.GONE);
        } else {
            mSecondTitle.setVisibility(View.VISIBLE);
            mSecondTitle.setText(subTitle);
        }
    }

    public void setBackClickListener(BackClickListener mBackClickListener) {
        this.mBackClickListener = mBackClickListener;
    }

    public void setRightClickListener(RightClickListener mRightClickListener) {
        this.mRightClickListener = mRightClickListener;
    }


    public interface BackClickListener {
        void back();
    }

    public interface RightClickListener {
        void rightClick();
    }

}
