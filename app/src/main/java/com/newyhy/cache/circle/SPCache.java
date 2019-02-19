package com.newyhy.cache.circle;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * SPCache 存 Json
 * Created by Jiervs on 2018/6/30.
 */

public class SPCache {

    public static final String CIRCLE = "CIRCLE";
    public static final String CIRCLE_TABS = "CIRCLE_TABS";

    public static void saveCircleTabCache(Context context, String json) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(CIRCLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(CIRCLE_TABS, json);
        editor.apply();
    }

    public static String getCircleTabCache(Context context) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(CIRCLE, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(CIRCLE_TABS, "");
    }
}
