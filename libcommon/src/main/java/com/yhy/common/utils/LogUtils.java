package com.yhy.common.utils;

public class LogUtils {

    static final boolean SHOW_LOG = true;

    public static void i(String tag, String string) {
        android.util.Log.i(tag, string);
    }

    public static void e(String tag, String string) {
        android.util.Log.e(tag, string);
    }

    public static void e(String tag, String string, Throwable tr) {
        android.util.Log.e(tag, string, tr);
    }

    public static void d(String tag, String string) {
        android.util.Log.d(tag, string);
    }

    public static void w(String tag, String string) {
        android.util.Log.w(tag, string);
    }

    public static void w(String tag, String string, Throwable tr) {
        android.util.Log.w(tag, string, tr);
    }

    public static void v(String tag, String string) {
        android.util.Log.v(tag, string);
    }

    public static void v(String tag, String string, Throwable tr) {
        android.util.Log.v(tag, string, tr);
    }
}
