package com.ymanalyseslibrary.secon;

import android.app.Activity;
import android.content.Context;

import com.ymanalyseslibrary.log.LogUtil;

import java.util.Map;


/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:打点方法入口
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:1/28/16
 * Time:09:56
 * Version 1.0
 */
public class YmAnalyticsEvent {
    private static final String TAG = "YiMayAnalysis";
    private static YMAnalyticsAgent2 mAgent = new YMAnalyticsAgent2();
    private YmAnalyticsEvent(){
        throw new UnsupportedOperationException("please don't initial the private construction");
    }

    /**
     * 登录操作
     * @param context
     * @param userID
     */
    public static void onUserLogin(Context context, String userID){
        mAgent.onUserLogin(context, userID);
    }

    /**
     * 退出操作
     * @param context
     */
    public static void onUserLoginOut(Context context){
        mAgent.onUserLoginOut(context);
    }

    /**
     * app启动时调用
     * @param context
     */
    public static void init(Context context){
        mAgent.uploadFirstBegin(context);
    }

    /**
     * 打点动作
     * @param context
     * @param eventID
     */
    public static void onEvent(Context context, String eventID){
        mAgent.onEvent(context, eventID);
    }


    /**
     * 打点动作
     * @param context
     * @param eventKey
     * @param eventValue
     */
    /*public static void onEvent(Context context, String eventKey, Map<String, Object> eventValue){
        mAgent.onEvent(context, eventKey, eventValue);
    }*/

    public static void onEvent(Context context, String eventKey, Map<String, String> eventValue){
        mAgent.onEvent(context, eventKey, eventValue);
    }

//    public static void onStart(Context context){
//        mAgent.onStart(context);
//    }
//
//    public static void onExit(Context context){
//        mAgent.onExit(context);
//    }

    /**
     * 打点动作
     * @param context
     * @param eventID
     * @param eventValue
     */
    public static void onEvent(Context context, String eventID, String eventValue){
        mAgent.onEvent(context, eventID, eventValue);
    }

    /**
     * 实时打点动作
     * @param context
     * @param eventID
     */
    public static void onCurrentEvent(Context context, String eventID){
        mAgent.onCurrentEvent(context, eventID);
    }


    /**
     * 实时打点动作
     * @param context
     * @param eventKey
     * @param eventValue
     */
    public static void onCurrentEvent(Context context, String eventKey, Map<String, String> eventValue){
        mAgent.onCurrentEvent(context, eventKey, eventValue);
    }

    /**
     * 实时打点动作
     * @param context
     * @param eventID
     * @param eventValue
     */
    public static void onCurrentEvent(Context context, String eventID, String eventValue){
        mAgent.onCurrentEvent(context, eventID, eventValue);
    }

    public static void onResume(Context context){
        if(context instanceof Activity) {
            mAgent.onResume(context);
        }else{
            LogUtil.e(TAG, "The context is not an instance of Activity");
        }
    }

    public static void onPause(Context context){
        if(context instanceof Activity) {
            mAgent.onPause(context);
        }else{
            LogUtil.e(TAG, "The context is not an instance of Activity");
        }
    }

    /**
     * 退出程序
     * @param context
     */
    public static void onDestroy(Context context){
        if(context instanceof Activity) {
            mAgent.onDestroy(context);
            //Thread.sleep(300); //延误300毫秒关闭，给数据发送争取时间
        }else{
            LogUtil.e(TAG, "The context is not an instance of Activity");
        }
    }

    /**
     * 上传错误日志
     * @param context
     * @param error
     */
    public static void reportError(Context context, String error){
        mAgent.reportError(context, error);
    }
}
