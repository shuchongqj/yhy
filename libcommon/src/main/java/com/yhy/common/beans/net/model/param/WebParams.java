package com.yhy.common.beans.net.model.param;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:WebParams
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/7/9
 * Time:上午11:32
 * Version 1.1.0
 */
public class WebParams implements Serializable {
    private static final long serialVersionUID = 5980249007032278364L;
    //加载的页面
    private String mUrl = null;
    //默认的标题
    private String mTitle = null;
    //是否强制设置标题
    private boolean isSetTitle = false;
    //要种cookie的域
    private String mDomain = null;
    //是否要种cookie
    private boolean mIsSyncCookie = true;
    //是否显示关闭按钮
    private boolean mIsShowCloseButton = false;
    //是否显示分享按钮
    private boolean mIsShowShareButton = false;
    //退出是否清除cookie
    private boolean mIsCleanCookie = true;
    //是否显示TitleBar
    private boolean isShowTitle = true;
    //是否需要返回
    private boolean isNeedSetResult = false;
    //是否是文章
    private boolean isArticle = false;
    //文章id
    private long id = 0;

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public boolean isSetTitle() {
        return isSetTitle;
    }

    public void setIsSetTitle(boolean setTitle) {
        isSetTitle = setTitle;
    }

    public String getDomain() {
        return mDomain;
    }

    public void setDomain(String mDomain) {
        this.mDomain = mDomain;
    }

    public boolean isSyncCookie() {
        return mIsSyncCookie;
    }

    public void setIsSyncCookie(boolean mIsSyncCookie) {
        this.mIsSyncCookie = mIsSyncCookie;
    }

    public boolean isShowCloseButton() {
        return mIsShowCloseButton;
    }

    public void setIsShowCloseButton(boolean mIsShowCloseButton) {
        this.mIsShowCloseButton = mIsShowCloseButton;
    }

    public boolean isShowShareButton() {
        return mIsShowShareButton;
    }

    public void setIsShowShareButton(boolean mIsShowShareButton) {
        this.mIsShowShareButton = mIsShowShareButton;
    }

    public boolean isCleanCookie() {
        return mIsCleanCookie;
    }

    public void setIsCleanCookie(boolean mIsCleanCookie) {
        this.mIsCleanCookie = mIsCleanCookie;
    }

    public boolean isShowTitle() {
        return isShowTitle;
    }

    public void setShowTitle(boolean showTitle) {
        isShowTitle = showTitle;
    }

    public boolean isNeedSetResult() {
        return isNeedSetResult;
    }

    public void setIsNeedSetResult(boolean isNeedSetResult) {
        this.isNeedSetResult = isNeedSetResult;
    }

    public void setArticle(boolean article) {
        isArticle = article;
    }

    public boolean isArticle() {
        return isArticle;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
