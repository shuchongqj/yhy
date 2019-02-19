package com.newyhy.utils.android_system;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import java.util.List;
import java.util.Random;

/**
 * 关于系统的一些工具类
 * Created by Jiervs on 2018/5/4.
 */

public class SystemUtils {
     /** 判断当前app是否存活
     * @param context
     * @param str
     * @return
     */
    public static boolean isAppaLive(Context context ,String str) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        boolean isAppRunning = false;
        //String MY_PKG_NAME = "你的包名";
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(str)//如果想要手动输入的话可以str换成<span style="font-family: Arial, Helvetica, sans-serif;">MY_PKG_NAME，下面相同</span>
                    || info.baseActivity.getPackageName().equals(str)) {
                isAppRunning = true;
                Log.i("SystemUtils", info.topActivity.getPackageName()
                        + " info.baseActivity.getPackageName()="
                        + info.baseActivity.getPackageName());
                break;
            }
        }
        return isAppRunning;
    }
}
