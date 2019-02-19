package com.newyhy.utils;

import android.content.Context;

import com.yhy.common.base.YHYBaseApplication;

public class SharedPreferenceUtil {

    private static final String FILE_NAME = "app_user_preference";//保存数据的文件名称

    private static final String INTEREST_SKIP = "INTEREST_SKIP";//用户是否跳过兴趣引导页

    public static void saveInterestSkip(boolean hasSkip) {
        YHYBaseApplication.getInstance().getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).edit().putBoolean(INTEREST_SKIP, hasSkip).commit();
    }

    public static boolean getInterestSkip() {
        return YHYBaseApplication.getInstance().getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE).getBoolean(INTEREST_SKIP, false);
    }
}
