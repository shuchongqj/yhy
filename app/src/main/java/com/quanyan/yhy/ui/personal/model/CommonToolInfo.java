package com.quanyan.yhy.ui.personal.model;

import com.quanyan.yhy.R;

/**
 * Created by shenwenjie on 2018/1/25.
 */

public class CommonToolInfo {

    private int resId;
    private String title;
    private String url;
    private String cover;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public CommonToolInfo() {
        this.resId = R.mipmap.app_icon;
        this.title = "默认工具";
    }

    public CommonToolInfo(int resId, String title) {
        this.resId = resId;
        this.title = title;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
