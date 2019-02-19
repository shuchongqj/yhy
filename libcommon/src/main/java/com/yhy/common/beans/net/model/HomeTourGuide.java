package com.yhy.common.beans.net.model;

import com.yhy.common.beans.net.model.common.Booth;
import com.yhy.common.beans.net.model.guide.GuideScenicInfoList;

import java.io.Serializable;

/**
 * Created with Android Studio.
 * Title:HomeTourGuide
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/8/23
 * Time:下午3:50
 * Version 1.1.0
 */
public class HomeTourGuide implements Serializable{
    private static final long serialVersionUID = -1144330471216080427L;
    //广告栏
    public Booth banners;
    //单个广告位
    public Booth singleBanner;
    //推荐景区
    public GuideScenicInfoList recommandGuideScenicInfoList;
    //附近景区
    public GuideScenicInfoList nearByGuideScenicInfoList;
}
