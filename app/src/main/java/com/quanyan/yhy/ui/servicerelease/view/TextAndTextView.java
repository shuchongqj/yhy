package com.quanyan.yhy.ui.servicerelease.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;

/**
 * Created with Android Studio.
 * Title:TextAndTextView
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-6-29
 * Time:10:42
 * Version 1.1.0
 */

public class TextAndTextView extends LinearLayout {

    private TextView mTVIndex;
    private TextView mTVContent;

    public TextAndTextView(Context context) {
        super(context);
        init(context);
    }

    public TextAndTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextAndTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TextAndTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View view = View.inflate(context, R.layout.text_and_text_item, null);
        mTVIndex = (TextView)view.findViewById(R.id.tv_index);
        mTVContent = (TextView) view.findViewById(R.id.tv_content);
        addView(view);
    }

    public void setTitle(String title){
        if(!StringUtil.isEmpty(title)){
            mTVIndex.setText(title);
        }
    }

    public void setContent(String content){
        if(!StringUtil.isEmpty(content)){
            mTVContent.setText(content);
        }
    }

    public String getContent(){
        return mTVContent.getText().toString().trim();
    }
}
