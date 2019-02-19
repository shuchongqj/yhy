package com.quanyan.yhy.ui.order.entity;

/**
 * Created with Android Studio.
 * Title:***Acitivity
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:田海波
 * Date: 2016/9/22
 * Time: 17:38
 * Version 2.0
 */
public class FilterCondition {
    public void setConId(String conId) {
        this.conId = conId;
    }

    public void setConName(String conName) {
        this.conName = conName;
    }

    public String getConName() {
        return conName;
    }

    public String getConId() {
        return conId;
    }

    private   String conId;
    private  String conName;

}
