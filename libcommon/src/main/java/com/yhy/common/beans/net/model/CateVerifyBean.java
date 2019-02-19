package com.yhy.common.beans.net.model;


import android.graphics.drawable.Drawable;

/**
 * Created with Android Studio.
 * Title:
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:吴建淼
 * Date: 2015-10-20
 * Time: 11:01
 * Version 1.0
 */
public class CateVerifyBean {
    public boolean isShow() {
        return isShow;
    }

    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
    }

    private boolean isShow;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public Drawable getBtnDrawable() {
        return btnDrawable;
    }

    public void setBtnDrawable(Drawable btnDrawable) {
        this.btnDrawable = btnDrawable;
    }

    private Drawable btnDrawable;


}
