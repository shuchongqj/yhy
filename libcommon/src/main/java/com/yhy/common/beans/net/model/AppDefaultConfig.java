package com.yhy.common.beans.net.model;

import com.yhy.common.beans.net.model.common.Booth;
import com.yhy.common.beans.net.model.common.BoothList;
import com.yhy.common.beans.net.model.common.ComIconList;
import com.yhy.common.beans.net.model.rc.SystemConfig;
import com.yhy.common.beans.net.model.user.DestinationList;

import java.util.HashMap;

/**
 * Created with Android Studio.
 * Title:AppDefaultConfig
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/3/28
 * Time:下午12:17
 * Version 1.0
 */
public class AppDefaultConfig {
    //系统配置项
    private SystemConfig mSystemConfig;
    //全部图标配置项
    private ComIconList mComIconList;
    //目的地城市列表
    private HashMap<String,DestinationList> mCityList;
    //闪页
    private Booth mAdBooth;
    //首页弹窗广告
    private BoothList mAdMultiBooth;
    //达人TAB标签文案
    private Booth mMasterBooth;

    public Booth getMasterBooth() {
        return mMasterBooth;
    }

    public void setMasterBooth(Booth mMasterBooth) {
        this.mMasterBooth = mMasterBooth;
    }

    public Booth getAdBooth() {
        return mAdBooth;
    }

    public void setAdBooth(Booth mAdBooth) {
        this.mAdBooth = mAdBooth;
    }

    public BoothList getmAdMultiBooth() {
        return mAdMultiBooth;
    }

    public void setmAdMultiBooth(BoothList mAdMultiBooth) {
        this.mAdMultiBooth = mAdMultiBooth;
    }

    public HashMap<String, DestinationList> getCityList() {
        return mCityList;
    }

    public void setCityList(HashMap<String, DestinationList> mCityList) {
        this.mCityList = mCityList;
    }

    public ComIconList getComIconList() {
        return mComIconList;
    }

    public void setComIconList(ComIconList mComIconList) {
        this.mComIconList = mComIconList;
    }

    public SystemConfig getSystemConfig() {
        return mSystemConfig;
    }

    public void setSystemConfig(SystemConfig mSystemConfig) {
        this.mSystemConfig = mSystemConfig;
    }
}
