package com.ymanalyseslibrary;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:1/13/16
 * Time:16:48
 * Version 1.0
 */
public class AnalysesConstants {

    public static final String PREFERENCE_NAME = "ym_preferences_analyses";
    public static final String ANALYSES_CACHE_FILE_NAME = "YM_analyses.txt";
    /**
     * 程序第一次启动
     */
    public static final String ANALYSES_FIRST_START = "analyses_first_start";
    /**
     * 用户登录信息
     */
    public static final String KEY_USER_ACCOUNT = "userAccount";
    public static final String KEY_USER_LOGIN_TIME = "userLoginTime";
    public static final String KEY_USER_EXIT_TIME = "userExitTime";


    /**
     * 软件名称（＊必填）
     */
    public static final String BODY_KEY_SOFTWARENAME = "1001";
    /**
     * 软件softwareID（＊必填）21
     */
    public static final String BODY_KEY_SOFTWAREID = "1002";
    /**
     * 软件版本号（＊必填）
     */
    public static final String BODY_KEY_SOFTWAREVERSION = "1003";
    /**
     * Mac地址（＊必填）
     */
    public static final String BODY_KEY_MAC_ADDRESS = "1004";
    /**
     * IP地址（＊必填）
     */
    public static final String BODY_KEY_IP_ADDRESS = "1005";
    /**
     * 联网方式（＊必填）（1、WIFI 2、MOBILENET）
     */
    public static final String BODY_KEY_NETWORKINGMDE = "1006";
    /**
     * 定位城市名称cityName
     */
    public static final String BODY_KEY_LOCATION_NAME = "1007";

    /**
     * 终端类型（＊必填）（MOBILE, PC, PAD）
     */
    public static final String BODY_KEY_TERMTYPE = "1008";
    /**
     * 终端操作系统（＊必填）（1：android 2：ios）
     */
    public static final String BODY_KEY_OS = "1009";
    /**
     * 终端操作系统版本号
     */
    public static final String BODY_KEY_OSVERSION = "1010";
    /**
     * 渠道编号
     */
    public static final String BODY_KEY_CHANNEL_NUMBER = "1011";
    /**
     * 渠道名称
     */
    public static final String BODY_KEY_CHANNEL_NAME = "1012";
    /**
     * 终端率
     */
    public static final String BODY_KEY_RESOLUTION = "1013";
    /**
     * 终端品牌
     */
    public static final String BODY_KEY_TERMINALBRAND = "1014";
    /**
     * 终端型号
     */
    public static final String BODY_KEY_TERMINALTYPE = "1015";
    /**
     * imei
     */
    public static final String BODY_KEY_DEVICEID = "1016";
    /**
     * 协议内容（＊必填）
     */
    public static final String BODY_KEY_DATA = "1017";
    /**
     * domain_id
     */
    public static final String BODY_KEY_DOMAIN_ID = "1018";
    /**
     * 动作编码
     */
    public static final String BODY_KEY_EVENT_ID = "2001";
    /**
     *
     */
    public static final String BODY_KEY_EVENT_LABEL = "2002";
    /**
     * 动作发生时间
     */
    public static final String BODY_KEY_EVENT_TIME = "2003";
    /**
     * 动作相关属性信息
     */
    public static final String BODY_KEY_EVENT_VALUES = "2004";

    /**
     * 用户id
     */
    public static final String BODY_KEY_USERID = "2006";
    /**
     * 动作相关属性key值1
     */
    public static final String BODY_KEY_EVENT_KEY_1 = "value";

    /**
     * 终端类型
     */
    public static final String TERM_TYPE = "MOBILE";
    /**
     * 终端操作系统
     */
    public static final String OS_TYPE = "Android";

    /**
     * 界面显示的EventId
     */
    public static final String ACTIVITY_ONRESUME = "Activity_OnResume";

    /**
     * 界面隐藏的EventId
     */
    public static final String ACTIVITY_ONPAUSE = "Activity_OnPause";

}
