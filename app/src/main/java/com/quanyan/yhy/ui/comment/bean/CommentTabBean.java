package com.quanyan.yhy.ui.comment.bean;

import android.widget.TextView;

/**
 * Created with Android Studio.
 * Title:CommentTabBean
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-5-16
 * Time:9:54
 * Version 1.1.0
 */


public class CommentTabBean {
    private int id;
    public String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private boolean isChecked;
    private TextView textView;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }
}
