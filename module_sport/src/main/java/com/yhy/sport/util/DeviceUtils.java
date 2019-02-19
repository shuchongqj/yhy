package com.yhy.sport.util;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.regex.Pattern;

public class DeviceUtils {
    private static final String RegEx_HUAWEI = "huawei|honor|mate|nova";
    private static final String RegEx_XIAOMI = "xiaomi";
    private static final String RegEx_MEIZU = "meizu";
    private static final String RegEx_SAMSUNG = "samsung";
    private static final String RegEx_VIVO = "vivo";
    private static final String RegEx_OPPO = "oppo";

    public static void openSystemProcessProtectPage(Context context) {
        String brand = Build.BRAND;
        Pattern pattern_xiaomi = Pattern.compile(RegEx_XIAOMI, Pattern.CASE_INSENSITIVE);
        Pattern pattern_huawei = Pattern.compile(RegEx_HUAWEI, Pattern.CASE_INSENSITIVE);
        Pattern pattern_meizu = Pattern.compile(RegEx_MEIZU, Pattern.CASE_INSENSITIVE);
        Pattern pattern_sumsung = Pattern.compile(RegEx_SAMSUNG, Pattern.CASE_INSENSITIVE);
        Pattern pattern_vivo = Pattern.compile(RegEx_VIVO, Pattern.CASE_INSENSITIVE);
        Pattern pattern_oppo = Pattern.compile(RegEx_OPPO, Pattern.CASE_INSENSITIVE);
        Intent intent = new Intent();
        ComponentName componentName = null;
        if (pattern_xiaomi.matcher(brand).find()) {
            //小米
            componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity");
        } else if (pattern_huawei.matcher(brand).find()) {
            //华为
            componentName = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity");
        } else if (pattern_meizu.matcher(brand).find()) {
            //魅族
            componentName = new ComponentName("com.meizu.safe", "com.meizu.safe.permission.SmartBGActivity");
        } else if (pattern_sumsung.matcher(brand).find()) {
            //三星
        } else if (pattern_oppo.matcher(brand).find()) {
            //oppo
        } else if (pattern_vivo.matcher(brand).find()) {
            //vivo
        }
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK&Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setComponent(componentName);
        try {
            context.startActivity(intent);
        } catch (Exception e) {//抛出异常就直接打开设置页面
            intent = new Intent(Settings.ACTION_SETTINGS);
            context.startActivity(intent);
        }
    }

    public static void openSystemBatteryPage(Context context) {
        String brand = Build.BRAND;
        Pattern pattern_xiaomi = Pattern.compile(RegEx_XIAOMI, Pattern.CASE_INSENSITIVE);
        Pattern pattern_huawei = Pattern.compile(RegEx_HUAWEI, Pattern.CASE_INSENSITIVE);
        Pattern pattern_meizu = Pattern.compile(RegEx_MEIZU, Pattern.CASE_INSENSITIVE);
        Pattern pattern_sumsung = Pattern.compile(RegEx_SAMSUNG, Pattern.CASE_INSENSITIVE);
        Pattern pattern_vivo = Pattern.compile(RegEx_VIVO, Pattern.CASE_INSENSITIVE);
        Pattern pattern_oppo = Pattern.compile(RegEx_OPPO, Pattern.CASE_INSENSITIVE);
        Intent intent = new Intent();
        ComponentName componentName = null;
        if (pattern_xiaomi.matcher(brand).find()) {
            //小米
            componentName = new ComponentName("com.miui.powerkeeper", "com.miui.powerkeeper.ui.HiddenAppsContainerManagementActivity");
        } else if (pattern_huawei.matcher(brand).find()) {
            //华为
            componentName = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");
        } else if (pattern_meizu.matcher(brand).find()) {
            //魅族
            componentName = new ComponentName("com.meizu.safe", "com.meizu.safe.SecurityMainActivity");
        } else if (pattern_sumsung.matcher(brand).find()) {
            //三星
        } else if (pattern_oppo.matcher(brand).find()) {
            //oppo
        } else if (pattern_vivo.matcher(brand).find()) {
            //vivo
        }
        intent.setComponent(componentName);
        try {
            context.startActivity(intent);
        } catch (Exception e) {//抛出异常就直接打开设置页面
            intent = new Intent(Settings.ACTION_SETTINGS);
            context.startActivity(intent);
        }
    }

    public static String obtainIdentification(Activity context) {
        String id;
        String deviceId = obtainDeviceId(context);

        if (deviceId == null || TextUtils.isEmpty(deviceId)) {
            id = obtainAndroidId(context);
        } else {
            id = deviceId;
        }

        return id;
    }

    /**
     * 动态申请获取DeviceId（在具体的Activity中获取）,而且需要在具体的Activity中监听onRequestPermissionsResult回调
     *
     * @param context
     * @return
     */
    public static String obtainDeviceId(Activity context) {
        if (context == null)
            return null;

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            // switch (requestCode) {
            //     case REQUEST_READ_PHONE_STATE:
            //     if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            //     }
            //     break;
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_PHONE_STATE}, Const.REQUEST_READ_PHONE_STATE);
        }
        return telephonyManager.getDeviceId();
    }

    public static String obtainAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

}
