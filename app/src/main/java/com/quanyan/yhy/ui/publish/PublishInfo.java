package com.quanyan.yhy.ui.publish;

import com.quanyan.yhy.R;

/**
 * Created by shenwenjie on 2018/1/23.
 */

public class PublishInfo {

    private int resId;
    private String title;

    public PublishInfo() {
        this.resId = R.mipmap.app_icon;
        this.title = "标题";
    }

    public PublishInfo(int resId, String title) {
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
