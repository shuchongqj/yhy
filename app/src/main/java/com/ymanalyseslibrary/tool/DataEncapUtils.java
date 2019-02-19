package com.ymanalyseslibrary.tool;

import android.content.Context;
import android.os.Build;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.quanyan.base.util.LocalUtils;
import com.quanyan.yhy.R;
import com.quanyan.yhy.ui.discovery.viewhelper.LiveItemHelper;
import com.yhy.common.config.ContextHelper;
import com.yhy.common.utils.JSONUtils;
import com.yhy.common.utils.SPUtils;
import com.yhy.router.YhyRouter;
import com.yhy.service.IUserService;
import com.ymanalyseslibrary.AnalysesConstants;
import com.ymanalyseslibrary.data.AnalyticsDataWrapper;

import java.util.HashMap;
import java.util.Map;


/**
 * Created with Android Studio.
 * Title:DataEncapUtils
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:baojie
 * Date:2016-4-28
 * Time:14:31
 * Version 1.0
 */

public class DataEncapUtils {


    private static DataEncapUtils instance = new DataEncapUtils();
    @Autowired
    IUserService userService;

    private DataEncapUtils() {
        YhyRouter.getInstance().inject(this);
    }

    public static DataEncapUtils getInstance(){
        return instance;
    }
    /**
     * 封装头部数据
     *
     * @param context
     * @param analyticsDataWrapper
     */
    public static void jointJsonHeader(Context context, AnalyticsDataWrapper analyticsDataWrapper) {
        analyticsDataWrapper.setSoftwareName(context.getString(R.string.quanyan_app_name));//软件名称
        analyticsDataWrapper.setSoftwareId(ContextHelper.getAppId());//appid
        analyticsDataWrapper.setSoftwareVersion(AnalyticUtils.getVersionName(context));//软件版本号
        analyticsDataWrapper.setMac(AnalyticUtils.getMacAddress(context));//MAC地址
        analyticsDataWrapper.setIP(AnalysesNetUtils.getIpAddress());//IP地址
        analyticsDataWrapper.setNetworkingMde(AnalyticUtils.getNetworkType(context));//网络类型
        analyticsDataWrapper.setCityName(SPUtils.getExtraCurrentCityName(context) == null ? "" : SPUtils.getExtraCurrentCityName(context));//定位城市名称
        analyticsDataWrapper.setTermType(AnalysesConstants.TERM_TYPE);//终端类型
        analyticsDataWrapper.setOs(AnalysesConstants.OS_TYPE);//终端操作系统
        analyticsDataWrapper.setOsVersion(Build.VERSION.RELEASE);//终端操作系统版本号
        analyticsDataWrapper.setChannelNumber(LocalUtils.getChannel(context));//渠道编号
        analyticsDataWrapper.setChannelName(LocalUtils.getChannel(context));//渠道名称
        analyticsDataWrapper.setResolution(AnalyticUtils.getScreenPix(context));//终端使用分辨率
        analyticsDataWrapper.setTerminalBrand(Build.MANUFACTURER);//终端品牌
        analyticsDataWrapper.setTerminalType(Build.MODEL);//终端型号
        analyticsDataWrapper.setDeviceId(AnalyticUtils.getDeviceIMEI(context));//设备imei
        analyticsDataWrapper.setDomainId(String.valueOf(ContextHelper.getDomainId()));//domain id
        /*analyticsDataWrapper.setUserId(context.getSharedPreferences(AnalysesConstants.PREFERENCE_NAME, Context.MODE_PRIVATE)
                .getString(AnalysesConstants.KEY_USER_ACCOUNT, ""));//用户id*/
        //analyticsDataWrapper.setUserId(String.valueOf(SPUtils.getUid(context)));

    }

    /**
     * body数据解析
     * @param eventID
     * @param eventStr
     * @param eventValue
     * @return
     */
    public static String eventValueEnacp(Context context, String eventID, String eventStr, Map<String, String> eventValue) {
        Map<String, Object> event = new HashMap<>();
        event.put(AnalysesConstants.BODY_KEY_EVENT_ID, eventID);
        encapNormalValue(context, event);
        if(eventStr != null && eventStr.length() > 0){
            HashMap<String, String> events = new HashMap<>();
            events.put(AnalysesConstants.BODY_KEY_EVENT_KEY_1, eventStr);
            event.put(AnalysesConstants.BODY_KEY_EVENT_VALUES, events);
        }
        if(eventValue != null){
            HashMap<String, String> events = new HashMap<>(eventValue);
            event.put(AnalysesConstants.BODY_KEY_EVENT_VALUES, events);
        }

        return JSONUtils.toJson(event);
    }

    /**
     * 打点时间
     * @return
     */
    public static String currentTimeString(){
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * 常量封装
     * @param context
     * @param event
     */
    public static void encapNormalValue(Context context, Map<String, Object> event) {
        event.put(AnalysesConstants.BODY_KEY_EVENT_LABEL, "");
        event.put(AnalysesConstants.BODY_KEY_EVENT_TIME, currentTimeString());
        //event.put(AnalysesConstants.BODY_KEY_URL, "");
        event.put(AnalysesConstants.BODY_KEY_USERID, getUid(context));
        //event.put(AnalysesConstants.BODY_KEY_APP_ID, AnalysesConstants.BODY_VALUE_APP_ID);
        //event.put(AnalysesConstants.BODY_KEY_DOMAIN_ID, AnalysesConstants.BODY_VALUE_DOMAIN_ID);
        //event.put(AnalysesConstants.BODY_KEY_UV, "");
        //event.put(AnalysesConstants.BODY_KEY_ACTIVITY_INFO, "");
    }

    //字符串uid
    public static String getUid(Context context){
        String uid = instance.userService.getLoginUserId() + "";
        if("0".equals(uid)){
            return null;
        }
        return uid;
    }
}
