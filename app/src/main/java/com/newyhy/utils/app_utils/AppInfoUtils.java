package com.newyhy.utils.app_utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newyhy.utils.PreferencesUtils;
import com.newyhy.views.MapPopupWindow;

import com.quanyan.yhy.R;
import com.yhy.common.base.YHYBaseApplication;
import com.yhy.common.utils.AndroidUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jiervs on 2018/5/9.
 */

public class AppInfoUtils {

    public static final String Current_Version = "current_version";
    public static final int UPDATE_LARGE_VERSION = 2;
    public static final int UPDATE_SMALL_VERSION = 1;
    public static final int UNCHANGE_VERSION = 0;
    public static final int OLD_VERSION = -1;

    public static String[] mapPaks = new String[]{
            "com.baidu.BaiduMap",   //百度
            "com.autonavi.minimap",//高德
            "com.tencent.map"};     //腾讯

    /**
     * 通过包名获取应用信息
     *
     * @param context
     * @param packageName
     * @return
     */
    public static AppInfo getAppInfoByPak(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        for (PackageInfo packageInfo : packageInfos) {
            if (packageName.equals(packageInfo.packageName)) {
                AppInfo tmpInfo = new AppInfo();
                tmpInfo.setAppName(packageInfo.applicationInfo
                        .loadLabel(packageManager).toString());
                tmpInfo.setPackageName(packageInfo.packageName);
                tmpInfo.setVersionName(packageInfo.versionName);
                tmpInfo.setVersionCode(packageInfo.versionCode);
                tmpInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(packageManager));
                return tmpInfo;
            }
        }
        return null;
    }

    /**
     * 返回当前设备上的地图应用集合
     *
     * @param context
     * @return
     */
    public static List<AppInfo> getMapApps(Context context) {
        LinkedList<AppInfo> apps = new LinkedList<AppInfo>();
        for (String pak : mapPaks) {
            AppInfo appinfo = getAppInfoByPak(context, pak);
            if (appinfo != null) {
                apps.add(appinfo);
            }
        }
        return apps;
    }

    /**
     * 弹出一个对应App选择框
     */
    public static void showMapAppListDialog(final Context context, List<AppInfo> apps, final HashMap<String, String> map) {
        MapPopupWindow mapPopupWindow = new MapPopupWindow((Activity) context, apps, map);
        mapPopupWindow.showOrDismiss(null);
    }

    /**
     * 版本号比较
     *
     * @param version1
     * @param version2
     * @return 0表示相等，2表示大版本更新，1表示小版本更新，-1表示旧版本
     */
    public static int compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return UNCHANGE_VERSION;
        }

        if (TextUtils.isEmpty(version2)) {
            PreferencesUtils.putString(YHYBaseApplication.getInstance(), Current_Version, version1);
            return UPDATE_LARGE_VERSION;
        }

        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        int index = 0;
        if (Integer.parseInt(version1Array[index]) > Integer.parseInt(version2Array[index])) {
            PreferencesUtils.putString(YHYBaseApplication.getInstance(), Current_Version, version1);
            return UPDATE_LARGE_VERSION;
        }
        // 获取最小长度值
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        // 循环判断每位的大小
        while (index < minLen && (diff = Integer.parseInt(version1Array[index])
                - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            // 如果位数不一致，比较多余位数
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    PreferencesUtils.putString(YHYBaseApplication.getInstance(), Current_Version, version1);
                    return UPDATE_SMALL_VERSION;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return OLD_VERSION;
                }
            }
            return UNCHANGE_VERSION;
        } else {
            PreferencesUtils.putString(YHYBaseApplication.getInstance(), Current_Version, version1);
            return diff > 0 ? UPDATE_SMALL_VERSION : OLD_VERSION;
        }
    }
}
