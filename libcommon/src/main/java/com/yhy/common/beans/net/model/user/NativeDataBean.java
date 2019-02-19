package com.yhy.common.beans.net.model.user;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:NativeDataBean
 * Copyright:Copyright (c) 2015
 * Company:北京quanyan
 * Author:J-King
 * Date:2016-3-9
 * Time:19:27
 * Version 1.0
 * Description:
 */
public class NativeDataBean implements Serializable {
    private static final long serialVersionUID = 3344004159207701923L;
    private String id;
    private String title;
    private String tagId;
    private String tagName;
    private String cityName;
    private String cityCode;
    private String uid;
    private String name;
    private String phone;
    //分享标题
    private String shareTitle;
    //分享内容
    private String shareContent;
    //分享图片链接
    private String shareImageUrl;
    //分享的网页地址
    private String shareWebPage;
    //分享方式
    private int shareWay;
    //打开的链接
    private String link;
    //话题名称
    private String topicName;
    //领取积分的状态
    private String status;
    //用户角色
    private String option;
    //是否清除cookie
    private boolean isCleanCookie;
    //花费积分
    private long point;
    //时间
    private long time;
    private long ugcid;

    public void setUgcId(long ugcId) {
        this.ugcid = ugcId;
    }

    public long getUgcId() {
        return ugcid;
    }

    //时间
    private boolean isShowTitle = true;
    private boolean isSetTitle = true;
    private long orderId;
    private String orderType;
    private int liveScreenType;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public boolean isSetTitle() {
        return isSetTitle;
    }

    public void setSetTitle(boolean setTitle) {
        isSetTitle = setTitle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public String getShareWebPage() {
        return shareWebPage;
    }

    public void setShareWebPage(String shareWebPage) {
        this.shareWebPage = shareWebPage;
    }

    public String getShareImageUrl() {
        return shareImageUrl;
    }

    public void setShareImageUrl(String shareImageUrl) {
        this.shareImageUrl = shareImageUrl;
    }

    public int getShareWay() {
        return shareWay;
    }

    public void setShareWay(int shareWay) {
        this.shareWay = shareWay;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public boolean isCleanCookie() {
        return isCleanCookie;
    }

    public void setCleanCookie(boolean cleanCookie) {
        isCleanCookie = cleanCookie;
    }

    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isShowTitle() {
        return isShowTitle;
    }

    public void setShowTitle(boolean showTitle) {
        isShowTitle = showTitle;
    }

    public void setLiveScreenType(int liveScreenType) {
        this.liveScreenType = liveScreenType;
    }

    public int getLiveScreenType() {
        return liveScreenType;
    }

    @Override
    public String toString() {
        return "NativeDataBean{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", tagId='" + tagId + '\'' +
                ", tagName='" + tagName + '\'' +
                ", cityName='" + cityName + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", shareTitle='" + shareTitle + '\'' +
                ", shareContent='" + shareContent + '\'' +
                ", shareImageUrl='" + shareImageUrl + '\'' +
                ", shareWebPage='" + shareWebPage + '\'' +
                ", shareWay=" + shareWay +
                ", link='" + link + '\'' +
                ", topicName='" + topicName + '\'' +
                ", status='" + status + '\'' +
                ", option='" + option + '\'' +
                ", isCleanCookie=" + isCleanCookie +
                ", point=" + point +
                ", time=" + time +
                ", isShowTitle=" + isShowTitle +
                ", liveScreenType=" + liveScreenType +
                '}';
    }

    public JSONObject serialize() throws JSONException {
        JSONObject json = new JSONObject();
        if (this.id != null) {
            json.put("id", this.id);
        }
        if (this.title != null) {
            json.put("title", this.title);
        }
        if (this.tagId != null) {
            json.put("tagId", this.tagId);
        }
        if (this.tagName != null) {
            json.put("tagName", this.tagName);
        }
        if (this.cityName != null) {
            json.put("cityName", this.cityName);
        }
        if (this.cityCode != null) {
            json.put("cityCode", this.cityCode);
        }
        if (this.uid != null) {
            json.put("uid", this.uid);
        }
        if (this.name != null) {
            json.put("name", this.name);
        }
        if (this.phone != null) {
            json.put("phone", this.phone);
        }
        if (this.shareTitle != null) {
            json.put("shareTitle", this.shareTitle);
        }
        if (this.shareContent != null) {
            json.put("shareContent", this.shareContent);
        }
        if (this.shareImageUrl != null) {
            json.put("shareImageUrl", this.shareImageUrl);
        }
        if (this.shareWebPage != null) {
            json.put("shareWebPage", this.shareWebPage);
        }
        if(this.shareWay != 0){
            json.put("shareWay", this.shareWay);
        }

        if (this.link != null) {
            json.put("link", this.link);
        }
        if (this.topicName != null) {
            json.put("topicName", this.topicName);
        }
        if (this.status != null) {
            json.put("status", this.status);
        }
        if (this.option != null) {
            json.put("option", this.option);
        }

        if(this.isCleanCookie){
            json.put("isCleanCookie", this.isCleanCookie);
        }

        if(this.point != 0){
            json.put("point", this.point);
        }

        if(this.time != 0){
            json.put("time", this.time);
        }

        if(!this.isShowTitle){
            json.put("isShowTitle", this.isShowTitle);
        }

        if(!this.isShowTitle){
            json.put("liveScreenType", this.liveScreenType);
        }

        return json;
    }
}