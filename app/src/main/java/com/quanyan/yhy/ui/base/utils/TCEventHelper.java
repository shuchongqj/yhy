package com.quanyan.yhy.ui.base.utils;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

import java.util.Map;

public class TCEventHelper {
    /**
     * 时间打点
     *
     * @param context
     * @param eventID
     * @param eventName
     */
    public static void onEvent(Context context, String eventID, String eventName) {
        MobclickAgent.onEvent(context, eventID, eventName);
//        YmAnalyticsEvent.onEvent(context, eventID, eventName);
    }

    /**
     * 带参数事件打点
     *
     * @param context
     * @param eventID
     * @param mapValue
     */
    public static void onEvent(Context context, String eventID, Map<String, String> mapValue) {
        MobclickAgent.onEvent(context, eventID, mapValue);
//        YmAnalyticsEvent.onEvent(context, eventID, mapValue);
    }

    /**
     * 统计一个数值类型的连续变量（该变量必须为整数），用户每次触发的数值的分布情况，如事件持续时间、每次付款金额等
     *
     * @param context
     * @param eventID
     * @param mapValue
     * @param value
     */
    public static void onEvent(Context context, String eventID, Map<String, String> mapValue, int value) {
        MobclickAgent.onEventValue(context, eventID, mapValue, value);
    }

    public static void onEvent(Context context, String eventID) {
        MobclickAgent.onEvent(context, eventID);
//        YmAnalyticsEvent.onEvent(context, eventID);
    }

    /**
     * 实时打点
     *
     * @param context
     * @param eventID
     */
    public static void onCurrentEvent(Context context, String eventID) {
//        YmAnalyticsEvent.onCurrentEvent(context, eventID);
    }

    /**
     * 实时打点
     *
     * @param context
     * @param eventID
     * @param eventName
     */
    public static void onCurrentEvent(Context context, String eventID, String eventName) {
//        YmAnalyticsEvent.onCurrentEvent(context, eventID, eventName);
    }

    /**
     * 实时打点
     *
     * @param context
     * @param eventID
     * @param mapValue
     */
    public static void onCurrentEvent(Context context, String eventID, Map<String, String> mapValue) {
//        YmAnalyticsEvent.onCurrentEvent(context, eventID, mapValue);
    }
}
