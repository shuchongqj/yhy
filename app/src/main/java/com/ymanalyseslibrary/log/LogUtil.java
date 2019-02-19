package com.ymanalyseslibrary.log;


import android.util.Log;

import com.quanyan.base.util.StringUtil;
import com.yhy.common.types.AppDebug;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:1/28/16
 * Time:11:10
 * Version 1.0
 */
public class LogUtil {
    public static boolean YM_ANALYSES_DEBUG = AppDebug.DEBUG_LOG;

    public static void i(String tag, String msg) {
        if (StringUtil.isEmpty(msg)) {
            return;
        }
        if (YM_ANALYSES_DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (StringUtil.isEmpty(msg)) {
            return;
        }
        if (YM_ANALYSES_DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable throwable) {
        if (StringUtil.isEmpty(msg)) {
            return;
        }
        if (YM_ANALYSES_DEBUG) {
            Log.d(tag, msg, throwable);
        }
    }

    public static void e(String tag, String msg) {
        if (StringUtil.isEmpty(msg)) {
            return;
        }
        if (YM_ANALYSES_DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable throwable) {
        if (StringUtil.isEmpty(msg)) {
            return;
        }
        if (YM_ANALYSES_DEBUG) {
            Log.e(tag, msg, throwable);
        }
    }

    public static void v(String tag, String msg) {
        if (StringUtil.isEmpty(msg)) {
            return;
        }
        if (YM_ANALYSES_DEBUG) {
            Log.v(tag, msg);
        }
    }
}
