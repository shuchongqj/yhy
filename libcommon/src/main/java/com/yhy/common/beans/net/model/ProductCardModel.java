package com.yhy.common.beans.net.model;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:ProductCardModel
 * Description:产品卡
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/3/25
 * Time:17:36
 * Version 1.0
 */
public class ProductCardModel implements Serializable {
    private static final long serialVersionUID = 2718881095249771969L;
    private int type;
    private String subType;
    private String title;
    private long id;
    private long childId;
    private String imgUrl;
    private String summary;
    private long price;
    private long max_price;

    public ProductCardModel(int type, String subType, String title, long id, String imgUrl, String summary, long price, long max_price, long childId) {
        this.type = type;
        this.subType = subType;
        this.title = title;
        this.id = id;
        this.imgUrl = imgUrl;
        this.summary = summary;
        this.price = price;
        this.childId = childId;
        this.max_price = max_price;
    }

    public ProductCardModel(int type, String subType, String title, long id, String imgUrl, String summary, long price) {
        this(type, subType, title, id, imgUrl, summary, price, price, 0);
    }

    public ProductCardModel(int type, String subType, String title, long id, String imgUrl, String summary, long price, long max_price) {
        this(type, subType, title, id, imgUrl, summary, price, max_price, 0);
    }

    public int getType() {
        return type;
    }

    public String getSubType() {
        return subType;
    }

    public String getTitle() {
        return title;
    }

    public long getId() {
        return id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getSummary() {
        return summary;
    }

    public long getPrice() {
        return price;
    }

    public long getChildId(){return childId;}

    public long getMax_price() {
        return max_price;
    }

    public void setMax_price(long max_price) {
        this.max_price = max_price;
    }
}
