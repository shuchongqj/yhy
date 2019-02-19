package com.quanyan.yhy.ui.order.entity;

import android.widget.TextView;

import com.quanyan.yhy.ui.order.NumberChooseView;

/**
 * Created with Android Studio.
 * Title:PersonnalInformation
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-4-9
 * Time:14:43
 * Version 1.0
 * Description:
 */
public class PersonnalInformation {


    private long skuID;
    private long pid;
    private long vid;
    private String type;
    private TextView textView;
    private NumberChooseView numberChooseView;
    private String text;
    private int num;
    private long price;
    private long totalPrice;

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public long getSkuID() {
        return skuID;
    }

    public void setSkuID(long skuID) {
        this.skuID = skuID;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public long getVid() {
        return vid;
    }

    public void setVid(long vid) {
        this.vid = vid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public NumberChooseView getNumberChooseView() {
        return numberChooseView;
    }

    public void setNumberChooseView(NumberChooseView numberChooseView) {
        this.numberChooseView = numberChooseView;
    }
}
