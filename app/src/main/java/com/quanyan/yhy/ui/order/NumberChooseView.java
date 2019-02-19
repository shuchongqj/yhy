package com.quanyan.yhy.ui.order;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanyan.yhy.R;

/**
 * Created with Android Studio.
 * Title:NumberChooseView
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-3-3
 * Time:14:43
 * Version 1.0
 * Description:
 */
public class NumberChooseView extends LinearLayout implements View.OnClickListener {

    private LinearLayout mReduceLayout;//递减数量
    private LinearLayout mIncreaseLayout;//递增数量
    private TextView mNumberText;//数量显示
    private int num = 1;
    private NumberClickListener mNumberClickListener;

    private int mMaxNum = 0;
    private int mMinNum = 0;
    private int mCurrentNum = 0;

    public NumberChooseView(Context context) {
        super(context);
        init(context);
    }

    public NumberChooseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NumberChooseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NumberChooseView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    /**
     * 初始化值
     *
     * @param max
     * @param min
     * @param current
     */
    public void initCheckValue(int max, int min, int current) {
        mMaxNum = max;
        mMinNum = min;
        mCurrentNum = current;
        num = current;
        if (mMaxNum == 0) {
            mNumberText.setText("0");
        } else {
            mNumberText.setText(mCurrentNum + "");
        }
        if (num == mMaxNum && num != mMinNum) {
            mReduceLayout.setBackgroundResource(R.mipmap.number_reduce_pressed);
            mIncreaseLayout.setBackgroundResource(R.mipmap.number_add_normal);
        } else if (num == mMinNum && num != mMaxNum) {
            mReduceLayout.setBackgroundResource(R.mipmap.number_reduce_normal);
            mIncreaseLayout.setBackgroundResource(R.mipmap.number_add_pressed);
        } else if (num == mMinNum && num == mMaxNum) {
            mReduceLayout.setBackgroundResource(R.mipmap.number_reduce_normal);
            mIncreaseLayout.setBackgroundResource(R.mipmap.number_add_normal);
        } else {
            mReduceLayout.setBackgroundResource(R.mipmap.number_reduce_pressed);
            mIncreaseLayout.setBackgroundResource(R.mipmap.number_add_pressed);
        }

        setTextColor(current);
    }

    private void init(Context context) {
        setOrientation(LinearLayout.HORIZONTAL);
        LayoutInflater.from(context).inflate(R.layout.number_choose_view, this);

        mReduceLayout = (LinearLayout) findViewById(R.id.number_choose_reduce);
        mIncreaseLayout = (LinearLayout) findViewById(R.id.number_choose_increase);
        mNumberText = (TextView) findViewById(R.id.number_choose_num_text);
        mReduceLayout.setOnClickListener(this);
        mIncreaseLayout.setOnClickListener(this);
    }


    /**
     * 设置数量变化监听器，用于控制数量的显示结果
     *
     * @param clickListener 接口
     */
    public void setNumberChooseListener(NumberClickListener clickListener) {
        this.mNumberClickListener = clickListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.number_choose_reduce: {//减
                if (num > 0) {
                    num--;
                }
                if (num < mMinNum) {
                    num = mMinNum;
                    if (mNumberClickListener != null) {
                        mNumberClickListener.onReduce();
                    }
                } else if (num == mMinNum) {
                    mReduceLayout.setBackgroundResource(R.mipmap.number_reduce_normal);
                    if (num < mMaxNum) {
                        mIncreaseLayout.setBackgroundResource(R.mipmap.number_add_pressed);
                    } else {
                        mIncreaseLayout.setBackgroundResource(R.mipmap.number_add_normal);
                    }
                    if (mNumberClickListener != null) {
                        mNumberClickListener.onReduce(num);
                    }
                } else {
                    mReduceLayout.setBackgroundResource(R.mipmap.number_reduce_pressed);
                    mIncreaseLayout.setBackgroundResource(R.mipmap.number_add_pressed);
                    if (mNumberClickListener != null) {
                        mNumberClickListener.onReduce(num);
                    }
                }

                break;
            }
            case R.id.number_choose_increase: {//加
                num++;
                if (num > mMaxNum) {
                    num = mMaxNum;
                    mIncreaseLayout.setBackgroundResource(R.mipmap.number_add_normal);
                    if (num > mMinNum) {
                        mReduceLayout.setBackgroundResource(R.mipmap.number_reduce_pressed);
                    } else {
                        mReduceLayout.setBackgroundResource(R.mipmap.number_reduce_normal);
                    }
                    if (mNumberClickListener != null) {
                        mNumberClickListener.onIncrease();
                    }
                } else if (num == mMaxNum) {
                    mIncreaseLayout.setBackgroundResource(R.mipmap.number_add_normal);
                    if (num > mMinNum) {
                        mReduceLayout.setBackgroundResource(R.mipmap.number_reduce_pressed);
                    } else {
                        mReduceLayout.setBackgroundResource(R.mipmap.number_reduce_normal);
                    }
                    if (mNumberClickListener != null) {
                        mNumberClickListener.onIncrease(num);
                    }
                } else {
                    mIncreaseLayout.setBackgroundResource(R.mipmap.number_add_pressed);
                    mReduceLayout.setBackgroundResource(R.mipmap.number_reduce_pressed);
                    if (mNumberClickListener != null) {
                        mNumberClickListener.onIncrease(num);
                    }
                }
                break;
            }
        }
        setTextColor(num);
        mNumberText.setText("" + num);
    }

    /**
     * 增减接口，用于将num提供为外部使用
     */
    public interface NumberClickListener {
        void onReduce(int num);

        void onIncrease(int num);

        void onReduce();

        void onIncrease();
    }


    public int getNum() {
        return Integer.parseInt(mNumberText.getText().toString());
    }


    private void setTextColor(int currentNum) {
        if (currentNum <= 0) {
            mNumberText.setTextColor(getResources().getColor(R.color.neu_999999));
        } else {
            mNumberText.setTextColor(getResources().getColor(R.color.neu_333333));
        }
    }

}
