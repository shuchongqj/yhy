package com.quanyan.yhy.view;

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
 * 商品数量选择器
 * 用户可根据需要进行数量选择，使用该类，需实现本类中的接口{@link NumberClickListener}来控制数量的显示<br/>
 * <br/> {@link #setNumberChooseListener(NumberClickListener)}设置选择器
 * Created by zhaoxp on 2015-10-27.
 */
public class NumberChoose extends LinearLayout implements View.OnClickListener {

    private LinearLayout mReduceLayout;//递减数量
    private LinearLayout mIncreaseLayout;//递增数量
    private TextView mNumberText;//数量显示
    private int num = 1;
    private NumberClickListener mNumberClickListener;

    private int mMaxNum = 0;
    private int mMinNum = 0;
    private int mCurrentNum = 0;

    public NumberChoose(Context context) {
        super(context);
        init(context);
    }

    public NumberChoose(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NumberChoose(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NumberChoose(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

        mNumberText.setText(mCurrentNum + "");
        if (num == mMaxNum && num != mMinNum) {
            mReduceLayout.setBackgroundResource(R.drawable.number_reduce_bg);
            mIncreaseLayout.setBackgroundResource(R.drawable.number_reduce_bg_disable);
        } else if (num == mMinNum && num != mMaxNum) {
            mReduceLayout.setBackgroundResource(R.drawable.number_reduce_bg_disable);
            mIncreaseLayout.setBackgroundResource(R.drawable.number_reduce_bg);
        } else if (num == mMinNum && num == mMaxNum) {
            mReduceLayout.setBackgroundResource(R.drawable.number_reduce_bg_disable);
            mIncreaseLayout.setBackgroundResource(R.drawable.number_reduce_bg_disable);
        } else {
            mReduceLayout.setBackgroundResource(R.drawable.number_reduce_bg);
            mIncreaseLayout.setBackgroundResource(R.drawable.number_reduce_bg);
        }

    }

    private void init(Context context) {
        setOrientation(LinearLayout.HORIZONTAL);
        LayoutInflater.from(context).inflate(R.layout.number_choose, this);

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
            case R.id.number_choose_reduce: {
                if (num > 0) {
                    num--;
                }
                if (num <= mMinNum) {
                    num = mMinNum;
                    mReduceLayout.setBackgroundResource(R.drawable.number_reduce_bg_disable);
                    if (num < mMaxNum) {
                        mIncreaseLayout.setBackgroundResource(R.drawable.number_reduce_bg);
                    } else {
                        mIncreaseLayout.setBackgroundResource(R.drawable.number_reduce_bg_disable);
                    }
                } else {
                    mReduceLayout.setBackgroundResource(R.drawable.number_reduce_bg);
                    mIncreaseLayout.setBackgroundResource(R.drawable.number_reduce_bg);
                }
                if (mNumberClickListener != null) {
                    mNumberClickListener.onReduce(num);
                }
                break;
            }
            case R.id.number_choose_increase: {
                num++;
                if (num >= mMaxNum) {
                    num = mMaxNum;
                    mIncreaseLayout.setBackgroundResource(R.drawable.number_reduce_bg_disable);
                    if (num > mMinNum) {
                        mReduceLayout.setBackgroundResource(R.drawable.number_reduce_bg);
                    } else {
                        mReduceLayout.setBackgroundResource(R.drawable.number_reduce_bg_disable);
                    }
                } else {
                    mIncreaseLayout.setBackgroundResource(R.drawable.number_reduce_bg);
                    mReduceLayout.setBackgroundResource(R.drawable.number_reduce_bg);
                }

                if (mNumberClickListener != null) {
                    mNumberClickListener.onIncrease(num);
                }
                break;
            }
        }
        mNumberText.setText("" + num);
    }

    /**
     * 增减接口，用于将num提供为外部使用
     */
    public interface NumberClickListener {
        void onReduce(int num);

        void onIncrease(int num);
    }


    public int getNum() {
        return Integer.parseInt(mNumberText.getText().toString());
    }

}
