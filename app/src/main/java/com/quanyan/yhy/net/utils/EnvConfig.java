package com.quanyan.yhy.net.utils;

import com.quanyan.yhy.BuildConfig;

/**
 * Created with Android Studio.
 * Title:EnvConfig
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:鲍杰
 * Date:2016-9-2
 * Time:19:10
 * Version 1.1.0
 */

public class EnvConfig {
    //开发环境
    public static final String ENV_DEVELOP = "env_develop.xml";
    //测试环境
    public static final String ENV_TEST = "env_test.xml";
    //线上环境
    public static final String ENV_ONLINE = "env_online.xml";
    //预发环境
    public static final String ENV_PRE_ONLINE = "env_pre.xml";
    //默认的环境
    public static String DEFAULT_ENV = ENV_ONLINE;

    static {
        switch (BuildConfig.ENV_TYPE) {
            case EnvType.DEVELOP:
                DEFAULT_ENV = ENV_DEVELOP;
                break;
            case EnvType.TEST:
                DEFAULT_ENV = ENV_TEST;
                break;
            case EnvType.PRE:
                DEFAULT_ENV = ENV_PRE_ONLINE;
                break;
            case EnvType.RELEASE:
                DEFAULT_ENV = ENV_ONLINE;
                break;
        }
    }


    /**
     * API
     */
    private String apiUrl;
    /**
     * HttpsAPI
     */
    private String httpsApiUrl;
    /**
     * 图片地址
     */
    private String imageUrl;
    /**
     * AppId
     */
    private String appId;

    /**
     * DomainId
     */
    private String domainId;

    /**
     * 密码公钥
     */
    private String publicKey;
    /**
     * 图片上传地址
     */
    private String uploadUrl;

    /**
     * 网宿 URL
     */
    private String vodUrl;

    /**
     * 打点的url
     */
    private String logUrl;

    /**
     * 默认的域名
     */
    private String defaultDomain;
    /**
     * 直播分享地址
     */
    private String liveShareUrl;

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getHttpsApiUrl() {
        return httpsApiUrl;
    }

    public void setHttpsApiUrl(String httpsApiUrl) {
        this.httpsApiUrl = httpsApiUrl;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }


    public String getVodUrl() {
        return vodUrl;
    }

    public void setVodUrl(String vodUrl) {
        this.vodUrl = vodUrl;
    }

    public String getLogUrl() {
        return logUrl;
    }

    public void setLogUrl(String logUrl) {
        this.logUrl = logUrl;
    }

    public String getDefaultDomain() {
        return defaultDomain;
    }

    public void setDefaultDomain(String defaultDomain) {
        this.defaultDomain = defaultDomain;
    }

    public void setLiveShareUrl(String liveShareUrl) {
        this.liveShareUrl = liveShareUrl;
    }

    public String getLiveShareUrl() {
        return liveShareUrl;
    }
}
