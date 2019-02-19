package com.quanyan.base.view.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Spannable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created with Android Studio.
 * Title:ClickPreventableTextView
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:赵晓坡
 * Date:16/7/25
 * Time:11:26
 * Version 1.1.0
 */
public class ClickPreventableTextView extends TextView implements View.OnClickListener {

    private boolean preventClick;
    private OnClickListener clickListener;
    private boolean ignoreSpannableClick;

    public ClickPreventableTextView(Context context) {
        super(context);
        initView(context);
    }

    public ClickPreventableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ClickPreventableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ClickPreventableTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getMovementMethod() != null) {
            getMovementMethod().onTouchEvent(this, (Spannable) getText(), event);
        }
        ignoreSpannableClick = true;
        boolean ret = super.onTouchEvent(event);
        ignoreSpannableClick = false;
        return ret;
    }

    @Override
    public void setOnClickListener(OnClickListener listener) {
        this.clickListener = listener;
        super.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (preventClick) {
            preventClick = false;
        } else if (clickListener != null)
            clickListener.onClick(v);
    }

    /**
     * Returns true if click event for a clickable span should be ignored
     * @return true if click event should be ignored
     */
    public boolean ignoreSpannableClick() {
        return ignoreSpannableClick;
    }

    /**
     * Call after handling click event for clickable span
     */
    public void preventNextClick() {
        preventClick = true;
    }


    private void initView(Context context) {

    }
}
