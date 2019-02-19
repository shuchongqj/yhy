package com.quanyan.yhy.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanyan.yhy.R;

/**
 * Created with Android Studio.
 * Title:MasterTitleTopLineView
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-3-7
 * Time:11:07
 * Version 1.0
 * Description:
 */
public class MasterTitleTopLineView extends LinearLayout {

    private TextView tv_topview;

    public MasterTitleTopLineView(Context context) {
        super(context);
        init(context);
    }

    public MasterTitleTopLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MasterTitleTopLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MasterTitleTopLineView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        View view = mLayoutInflater.inflate(R.layout.mastertitletoplineview, null);

        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        rlp.bottomMargin = 0;
        rlp.topMargin = 0;
        rlp.leftMargin = 0;
        rlp.rightMargin = 0;

        tv_topview = (TextView) view.findViewById(R.id.tv_topview);
        addView(view, rlp);
    }

    public void setTopTextView(String text) {
        if (!TextUtils.isEmpty(text)) {
            tv_topview.setText(text);
        }
    }

}
