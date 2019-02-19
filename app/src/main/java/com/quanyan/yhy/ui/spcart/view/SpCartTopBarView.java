package com.quanyan.yhy.ui.spcart.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.newyhy.activity.HomeMainTabActivity;
import com.quanyan.yhy.R;

/**
 * Created with Android Studio.
 * Title:SpCartTopBarView
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-10-19
 * Time:14:18
 * Version 1.0
 * Description:
 */
public class SpCartTopBarView extends LinearLayout {

    private LinearLayout mLBack;
    private TextView mTitleTv;
    private LinearLayout mEditBtn;
    private RelativeLayout mNoticeBtn;
    private TextView mEditTv;
    private TextView tvNewMsgCount;

    private BackClickListener mBackClickListener;
    private EditClickListener mEditClickListener;
    private NoticeClickListener mNoticeClickListener;

    public SpCartTopBarView(Context context) {
        super(context);
        initView(context);
    }

    public SpCartTopBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SpCartTopBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SpCartTopBarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.spcart_top_view, this);
        mLBack = (LinearLayout) this.findViewById(R.id.ll_back);
        mTitleTv = (TextView) this.findViewById(R.id.tv_title);
        mEditBtn = (LinearLayout) this.findViewById(R.id.rl_right1);
        mNoticeBtn = (RelativeLayout) this.findViewById(R.id.rl_right2);
        mEditTv = (TextView) this.findViewById(R.id.tv_edit);

        // 消息数量和按钮
        tvNewMsgCount = (TextView) this.findViewById(R.id.personal_message_num);
        updateIMMessageCount(HomeMainTabActivity.imUnreadCount);

        initClick();
    }

    private void initClick() {
        mLBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBackClickListener != null) {
                    mBackClickListener.back();
                }
            }
        });

        mEditBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditClickListener != null) {
                    mEditClickListener.edit();
                }
            }
        });

        mNoticeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNoticeClickListener != null) {
                    mNoticeClickListener.notice();
                }
            }
        });
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            return;
        }
        mTitleTv.setText(title);
    }

    /**
     * 设置编辑字
     *
     * @param str
     */
    public void setEditTv(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        mEditTv.setText(str);
    }

    /**
     * 设置编辑按钮隐藏
     */
    public void setEditBtnGone() {
        if (mEditBtn.getVisibility() == View.VISIBLE) {
            mEditBtn.setVisibility(View.GONE);
        }
    }

    /**
     * 设置编辑按钮显示
     */
    public void setEditBtnVisible() {
        if (mEditBtn.getVisibility() == View.GONE) {
            mEditBtn.setVisibility(View.VISIBLE);
        }
    }

    public void setmNoticeBtnGone() {
        if (mNoticeBtn.getVisibility() == View.VISIBLE) {
            mNoticeBtn.setVisibility(View.GONE);
        }
    }

    /**
     * 设置编辑按钮显示
     */
    public void setmNoticeBtnVisible() {
        if (mNoticeBtn.getVisibility() == View.GONE) {
            mNoticeBtn.setVisibility(View.VISIBLE);
        }
    }

    public void setBackClickListener(BackClickListener mBackClickListener) {
        this.mBackClickListener = mBackClickListener;
    }

    public void setEditClickListener(EditClickListener mEditClickListener) {
        this.mEditClickListener = mEditClickListener;
    }

    public void setNoticeClickListener(NoticeClickListener mNoticeClickListener) {
        this.mNoticeClickListener = mNoticeClickListener;
    }


    public interface BackClickListener {
        void back();
    }


    public interface EditClickListener {
        void edit();
    }

    public interface NoticeClickListener {
        void notice();
    }


    /**
     * 刷新消息数
     *
     * @param messageNum
     */
    public void updateIMMessageCount(int messageNum) {
        if (messageNum == 0) {
            tvNewMsgCount.setVisibility(View.INVISIBLE);
        } else {
            tvNewMsgCount.setVisibility(View.VISIBLE);
            tvNewMsgCount.setText("" + messageNum);
        }
    }
}
