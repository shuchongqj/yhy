package com.yhy.common.config;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.yhy.common.utils.SPUtils;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ContextHelper {
    //默认值
    public static final String DEFAULT_API_URL = "";
    public static final String DEFAULT_HTTPS_API_URL = "";
    private static final String DEFAULT_SERVER_IMAGE_SUFFIX = "";
    private static final String DEFAULT_IMAGE_UPLOAD_URL = "";
    private static final String DEFAULT_LOG_URL = "";
    private static final String DEFAULT_APP_ID = "";
    private static final int DEFAULT_DOMAIN_ID = 0;
    private static final String DOMAIN_DEFAULT = "";
    private static final String DEFAULT_LIVE_SHARE_URL = "";
    private static final String DEFAULT_VOD_URL = "";
    //变量名
    private static String mEnvUrl = DEFAULT_API_URL;
    private static String mHttpsApiUrl = DEFAULT_HTTPS_API_URL;
    private static String mAppId = DEFAULT_APP_ID;
    private static int mDomainId = DEFAULT_DOMAIN_ID;
    private static String mUplodImageUrl = DEFAULT_IMAGE_UPLOAD_URL;
    private static String mImageUrl = DEFAULT_SERVER_IMAGE_SUFFIX;
    private static String mLogUrl = DEFAULT_LOG_URL;
    private static String mLiveShareUrl = DEFAULT_LIVE_SHARE_URL;
    private static String mVodUrl = DEFAULT_VOD_URL;
    //默认的域
    private static String mDefaultDomain = DOMAIN_DEFAULT;

    private static Context mContext;


    public static void setLiveShareUrl(String liveShareUrl) {
        mLiveShareUrl = liveShareUrl;
    }

    public static String getLiveShareUrl() {
        return mLiveShareUrl;
    }

    public Context getContext() {
        return mContext;
    }

    public static void setEnvUrl(String url) {
        mEnvUrl = url;
    }

    public static String getEnvUrl() {
        return mEnvUrl;
    }

    public static String getHttpsApiUrl() {
        return mHttpsApiUrl;
    }

    public static void setHttpsApiUrl(String mHttpsApiUrl) {
        ContextHelper.mHttpsApiUrl = mHttpsApiUrl;
    }

    public static void setAppId(String appId) {
        mAppId = appId;
    }

    public static String getAppId() {
        return mAppId;
    }

    public static int getDomainId() {
        return mDomainId;
    }

    public static void setDomainId(int domainId) {
        mDomainId = domainId;
    }

    public static void setImageUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            mImageUrl = DEFAULT_SERVER_IMAGE_SUFFIX;
            return;
        }
        mImageUrl = url;
    }

    public static String getImageUrl() {
        if (mImageUrl == null) {
            return DEFAULT_SERVER_IMAGE_SUFFIX;
        }
        return mImageUrl;
    }

    public static void setLogUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            mLogUrl = DEFAULT_LOG_URL;
            return;
        }
        mLogUrl = url;
    }

    public static String getLogUrl() {
        if (mLogUrl == null) {
            return DEFAULT_LOG_URL;
        }
        return mLogUrl;
    }

    public static void setVodUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            mVodUrl = DEFAULT_VOD_URL;
            return;
        }
        mVodUrl = url;
    }

    public static String getVodUrl() {
        if (mVodUrl == null) {
            return DEFAULT_VOD_URL;
        }
        return mVodUrl;
    }


    public static void setUploadImageUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            mUplodImageUrl = DEFAULT_IMAGE_UPLOAD_URL;
            return;
        }
        mUplodImageUrl = url;
    }

    public static String getUploadImageUrl() {
        if (mUplodImageUrl == null) {
            return DEFAULT_IMAGE_UPLOAD_URL;
        }
        return mUplodImageUrl;
    }

    public static String getDefaultDomain() {
        return mDefaultDomain;
    }

    public static void setDefaultDomain(String mDefaultDomain) {
        ContextHelper.mDefaultDomain = mDefaultDomain;
    }

    /**
     * 获取图片的全路径
     *
     * @param imageName
     * @return
     */
    public static String getImageFullUrl(String imageName) {
        if (imageName == null) {
            return null;
        }

        if (!TextUtils.isEmpty(imageName) && imageName.startsWith("http:")) {
            return imageName;
        }

        if (!TextUtils.isEmpty(imageName) && imageName.startsWith("https:")) {
            return imageName;
        }

        if (!TextUtils.isEmpty(imageName) && imageName.startsWith("file:")) {
            return imageName;
        }

        String str = imageName.toLowerCase();
        if(str.endsWith(".gif")){
            return getThumbnailFullPath(imageName, null);
        }
        return ContextHelper.getImageUrl() + imageName;

    }

    /**
     * 拼出缩略图全地址*
     * @param
     * @param sizeStr 大小 eg. “300x300”
     * @return url full path
     */
    public static String getThumbnailFullPath(String imageName, String sizeStr) {
        if(TextUtils.isEmpty(imageName)){
            return null;
        }
        if (imageName.startsWith("http:")) {
            return imageName;
        }
        String format = "";
        int index = imageName.lastIndexOf(".");
        if (index > 0) {
            format = imageName.substring(index, imageName.length());
            imageName = imageName.substring(0,index);
        }
//        if(!StringUtil.isEmpty(sizeStr)) {
//            return ContextHelper.getImageUrl() + imageName + "_" + sizeStr + format;
//        }else {
//            return ContextHelper.getImageUrl() + imageName + format;
//        }
        return ContextHelper.getImageUrl() + imageName + format;
    }
}
