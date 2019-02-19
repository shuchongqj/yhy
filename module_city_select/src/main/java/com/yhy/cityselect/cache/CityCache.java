package com.yhy.cityselect.cache;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by yangboxue on 2018/7/10.
 */

public class CityCache {
    public static final String OUTPLACE = "OUTPLACE";
    public static final String OUTPLACE_CITY_LIST_ORIGIN = "OUTPLACE_CITY_LIST_ORIGIN";
    public static final String OUTPLACE_CITY_LIST = "OUTPLACE_CITY_LIST";
    public static final String OUTPLACE_CITY_LIST_INDEX = "OUTPLACE_CITY_LIST_INDEX";
    public static final String OUTPLACE_CITY_LIST_HOT = "OUTPLACE_CITY_LIST_HOT";
    public static final String OUTPLACE_CITY_LIST_HISTORY = "OUTPLACE_CITY_LIST_HISTORY";

    public static void saveOutPlaceCityListOrigin(Context context, String json) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(OUTPLACE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(OUTPLACE_CITY_LIST_ORIGIN, json);
        editor.apply();
    }

    public static String getOutPlaceCityListOrigin(Context context) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(OUTPLACE, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(OUTPLACE_CITY_LIST_ORIGIN, "");
    }

    public static void saveOutPlaceCityList(Context context, String json) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(OUTPLACE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(OUTPLACE_CITY_LIST, json);
        editor.apply();
    }

    public static String getOutPlaceCityList(Context context) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(OUTPLACE, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(OUTPLACE_CITY_LIST, "");
    }

    public static void saveCityListIndex(Context context, String json) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(OUTPLACE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(OUTPLACE_CITY_LIST_INDEX, json);
        editor.apply();
    }

    public static String getCityListIndex(Context context) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(OUTPLACE, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(OUTPLACE_CITY_LIST_INDEX, "");
    }

    public static void saveHotCityList(Context context, String json) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(OUTPLACE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(OUTPLACE_CITY_LIST_HOT, json);
        editor.apply();
    }

    public static String getHotCityList(Context context) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(OUTPLACE, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(OUTPLACE_CITY_LIST_HOT, "");
    }

    public static void saveCityHistory(Context context, String json) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(OUTPLACE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(OUTPLACE_CITY_LIST_HISTORY, json);
        editor.apply();
    }

    public static String getCityHistoryt(Context context) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(OUTPLACE, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(OUTPLACE_CITY_LIST_HISTORY, "");
    }
}
