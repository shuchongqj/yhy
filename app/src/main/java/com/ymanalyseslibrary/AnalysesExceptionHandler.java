package com.ymanalyseslibrary;

/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:异常捕获类
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:1/26/16
 * Time:14:54
 * Version 1.0
 */
public class AnalysesExceptionHandler implements Thread.UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";

    private Thread.UncaughtExceptionHandler mUncaughtExceptionHandler;
    private AnalysesExceptionInterface mAnalysesExceptionInterface;

    /**
     * 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
     */
    public AnalysesExceptionHandler() {
        if(Thread.getDefaultUncaughtExceptionHandler() != this) {
            this.mUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(this);
        }
    }

    public void setAnalysesExceptionInterface(AnalysesExceptionInterface var1) {
        this.mAnalysesExceptionInterface = var1;
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     * @param var1
     * @param var2
     */
    public void uncaughtException(Thread var1, Throwable var2) {
        this.a(var2);
        if(this.mUncaughtExceptionHandler != null && this.mUncaughtExceptionHandler != Thread.getDefaultUncaughtExceptionHandler()) {
            this.mUncaughtExceptionHandler.uncaughtException(var1, var2);
        }

    }

    private void a(Throwable var1) {
        if(AnalyticsConfig.CATCH_EXCEPTION) {
            this.mAnalysesExceptionInterface.catchException(var1);
        } else {
            this.mAnalysesExceptionInterface.catchException((Throwable)null);
        }

    }
}
