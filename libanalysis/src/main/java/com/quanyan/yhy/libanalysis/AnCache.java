package com.quanyan.yhy.libanalysis;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by yangboxue on 2018/7/16.
 */

public class AnCache {
    public static final String ANCACHE = "ANCACHE";
    public static final String ANCACHE_CIRCLE_LOGS = "ANCACHE_CIRCLE_LOGS";
    public static final String ANCACHE_CIRCLE_FAILED_LOGS = "ANCACHE_CIRCLE_FAILED_LOGS";

    public static void saveAnCacheCircleLogs(Context context, String json) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(ANCACHE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ANCACHE_CIRCLE_LOGS, json);
        editor.apply();
    }

    public static String getAnCacheCircleLogs(Context context) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(ANCACHE, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(ANCACHE_CIRCLE_LOGS, "");
    }

    public static void saveAnCacheCircleFailedLogs(Context context, String json) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(ANCACHE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ANCACHE_CIRCLE_FAILED_LOGS, json);
        editor.apply();
    }

    public static String getAnCacheCircleFailedLogs(Context context) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(ANCACHE, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(ANCACHE_CIRCLE_FAILED_LOGS, "");
    }
}
