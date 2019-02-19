package com.quanyan.yhy.ui.servicerelease.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;

/**
 * Created with Android Studio.
 * Title:TextAndEditView
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-6-29
 * Time:10:06
 * Version 1.1.0
 */

public class TextAndEditView extends LinearLayout {

    private TextView mTVIndex;
    private EditText mETContent;

    public TextAndEditView(Context context) {
        super(context);
        init(context);
    }

    public TextAndEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextAndEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TextAndEditView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View view = View.inflate(context, R.layout.text_and_edit_item, null);
        mTVIndex = (TextView)view.findViewById(R.id.tv_index);
        mETContent = (EditText) view.findViewById(R.id.et_content);
        addView(view);
    }

    public void setTitle(String title){
        if(!StringUtil.isEmpty(title)){
            mTVIndex.setText(title);
        }
    }

    public EditText getEditText(){
        return mETContent;
    }
}
