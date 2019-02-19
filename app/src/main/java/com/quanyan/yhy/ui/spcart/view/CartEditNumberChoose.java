package com.quanyan.yhy.ui.spcart.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.common.city.utils.ToastUtil;
import com.quanyan.yhy.ui.wallet.InputTools;

/**
 * Created with Android Studio.
 * Title:CartEditNumberChoose
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-12-20
 * Time:14:51
 * Version 1.0
 * Description:
 */
public class CartEditNumberChoose extends LinearLayout implements View.OnClickListener {

    private LinearLayout mReduceLayout;//递减数量
    private LinearLayout mIncreaseLayout;//递增数量
    private NoMenuEditText mNumberText;//数量显示
    private int num = 1;
    private NumberClickListener mNumberClickListener;

    private int mMaxNum = 0;
    private int mMinNum = 0;
    private int mCurrentNum = 0;

    private Context mContext;

    public CartEditNumberChoose(Context context) {
        super(context);
        init(context);
    }

    public CartEditNumberChoose(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CartEditNumberChoose(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CartEditNumberChoose(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        if (mMaxNum <= 0) {
            mMaxNum = 0;
        } else if (mMaxNum >= 99) {
            mMaxNum = 99;
        }
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

        Editable b = mNumberText.getText();
        mNumberText.setSelection(b.length());
        InputTools.KeyBoard(mNumberText, "open");
    }

    private void init(Context context) {
        mContext = context;
        setOrientation(LinearLayout.HORIZONTAL);
        LayoutInflater.from(context).inflate(R.layout.number_edit_choose_view, this);
        mReduceLayout = (LinearLayout) findViewById(R.id.number_choose_reduce);
        mIncreaseLayout = (LinearLayout) findViewById(R.id.number_choose_increase);
        mNumberText = (NoMenuEditText) findViewById(R.id.number_choose_num_text);

//        mNumberText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
//            @Override
//            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//                return false;
//            }
//
//            @Override
//            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//                return false;
//            }
//
//            @Override
//            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//                return false;
//            }
//
//            @Override
//            public void onDestroyActionMode(ActionMode mode) {
//
//            }
//        });

        mNumberText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    Integer a = Integer.parseInt(s.toString());
                    if (a >= mMaxNum) {
                        num = mMaxNum;
                        if (num == mMinNum) {
                            mReduceLayout.setBackgroundResource(R.mipmap.number_reduce_normal);
                            mIncreaseLayout.setBackgroundResource(R.mipmap.number_add_normal);
                        } else {
                            mReduceLayout.setBackgroundResource(R.mipmap.number_reduce_pressed);
                            mIncreaseLayout.setBackgroundResource(R.mipmap.number_add_normal);
                        }
                        if (a > mMaxNum) {
                            ToastUtil.showToast(mContext, "库存紧张,最多只能买" + mMaxNum + "件哦~");
                            mHandler.sendEmptyMessageDelayed(0x10, 50);
                        }
                    } else if (a <= mMinNum) {
                        num = mMinNum;
                        if (num == mMaxNum) {
                            mReduceLayout.setBackgroundResource(R.mipmap.number_reduce_normal);
                            mIncreaseLayout.setBackgroundResource(R.mipmap.number_add_normal);
                        } else {
                            mIncreaseLayout.setBackgroundResource(R.mipmap.number_add_pressed);
                            mReduceLayout.setBackgroundResource(R.mipmap.number_reduce_normal);
                        }
                        if(a <mMinNum) {
                            mHandler.sendEmptyMessageDelayed(0x10, 500);
                        }
                    } else {
                        num = a;
                        mIncreaseLayout.setBackgroundResource(R.mipmap.number_add_pressed);
                        mReduceLayout.setBackgroundResource(R.mipmap.number_reduce_pressed);
                    }
                } else {
//                    num = 1;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
    }


    public int getNum() {
        if (!TextUtils.isEmpty(mNumberText.getText().toString())) {
            return Integer.parseInt(mNumberText.getText().toString());
        } else {
            return -1;
        }
    }


    private void setTextColor(int currentNum) {
        if (currentNum <= 0) {
            mNumberText.setTextColor(getResources().getColor(R.color.neu_999999));
        } else {
            mNumberText.setTextColor(getResources().getColor(R.color.neu_333333));
        }
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x10:
                    mNumberText.setText(num + "");
                    Editable b = mNumberText.getText();
                    mNumberText.setSelection(b.length());
                    break;
            }
        }
    };
}
