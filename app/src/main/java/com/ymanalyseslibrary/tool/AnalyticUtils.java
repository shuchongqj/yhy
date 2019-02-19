package com.ymanalyseslibrary.tool;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.quanyan.base.util.StringUtil;
import com.ymanalyseslibrary.log.LogUtil;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Locale;
import java.util.UUID;

import javax.microedition.khronos.opengles.GL10;


/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:2/17/16
 * Time:17:12
 * Version 1.0
 */
public class AnalyticUtils {
    protected static final String TAG = AnalyticUtils.class.getName();
    public static final String b = "";
    public static final String NETTYPE = "2G/3G";
    public static final String WIFI = "Wi-Fi";
    public static final int e = 8;
    //电量值
    private static int sBatteryLevel = 0;

    public AnalyticUtils() {
    }

    /**
     * 获取MAC地址
     * Get MAC address;
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context){
        String macAddress = null;
        if(!checkPermission(context, Manifest.permission.ACCESS_WIFI_STATE)){
            return macAddress;
        }

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if(null == wifiManager){
            return macAddress;
        }

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if(null == wifiInfo){
            return macAddress;
        }

        macAddress = wifiInfo.getMacAddress();
        return null == macAddress? null: macAddress.toUpperCase();
    }

    /**
     * 获取IMEI信息
     * Get IMEI
     *
     * @param context
     * @return
     */
    public static String getDeviceIMEI(Context context){
        String imei = null;
        if(!checkPermission(context, Manifest.permission.READ_PHONE_STATE)){
            return imei;
        }

        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        if(null == telephonyManager){
            return imei;
        }

        imei = telephonyManager.getDeviceId();
        if(StringUtil.isEmpty(imei)){
            return "";
        }
        return imei.toUpperCase();
    }

    /**
     * 获取IMSI
     * Get IMSI
     *
     * @param context
     * @return
     */
    public static String getDeviceIMSI(Context context){
        String imsi = null;
        if (!checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            return imsi;
        }

        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        if(null == telephonyManager){
            return imsi;
        }

        imsi = telephonyManager.getSubscriberId();
        return null == imsi ? null : imsi.toUpperCase();
    }

    /**
     * 获取Device model
     * @return
     */
    public static String getDeviceModel(){
        return Build.MODEL;
    }

    /**
     * 获取生产商
     * @return
     */
    public static String getManufacturer(){
        return Build.MANUFACTURER;
    }

    /**
     * 获取屏幕高度(pixels)
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context){
        DisplayMetrics displayMetrics =context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    /**
     * 获取屏幕宽度(pixels)
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context){
        DisplayMetrics displayMetrics =context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    /**
     * 获取屏幕分辨率
     * @param context
     * @return
     */
    public static String getScreenPix(Context context){
        return getScreenWidth(context) + "*" + getScreenHeight(context);
    }

    /**
     * 获取APP的版本名
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context){
        String versionName = "";
        PackageManager packageManager = context.getPackageManager();
        try{
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        }catch (PackageManager.NameNotFoundException e){
            Log.e(TAG, "Cannot get app version name", e);
        }
        return versionName;
    }

    /**
     * 获取APP的版本号
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context){
        int versionCode = -1;
        PackageManager packageManager = context.getPackageManager();
        try{
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        }catch (PackageManager.NameNotFoundException e){
            Log.e(TAG, "Cannot get app version name", e);
        }
        return versionCode;
    }

    /**
     * 获取Queen的ID,此处ID为Queen特有,并储存在该设备中,用于识别特定设备
     * Queen's ID, store in the device to recognize single device.
     *
     * @param context
     * @return
     */
    /*public static String getDeviceId(Context context){
        String deviceId = null;

        deviceId = loadDeviceId(context);
        if(null == deviceId){
            deviceId = generateDeviceId();
        }

        saveDeviceId(deviceId, context);
        return deviceId;
    }*/

    /**
     * 获取联网状态
     * @param context
     * @return
     */
    public static String getNetworkType(Context context){

        if(!isNetworkConnected(context)){
            return "NONE";
        }

        ConnectivityManager connectivityManager = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if(null == connectivityManager){
            return null;
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(ConnectivityManager.TYPE_WIFI == networkInfo.getType()){
            return "WIFI";
        }

        if(!checkPermission(context, Manifest.permission.READ_PHONE_STATE)){
            return null;
        }

        TelephonyManager telephonyManager = (TelephonyManager)context
                .getSystemService(Context.TELEPHONY_SERVICE);

        if(null == telephonyManager){
            return null;
        }

        switch (telephonyManager.getNetworkType()){
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "MOBILE-2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "MOBILE-3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "MOBILE-4G";
            default:
                return "MOBILE-UNKNOWN";
        }
    }

    /**
     * 获取本地IP
     * @param context
     * @return
     */
    public static String getLocalIpAddress(Context context){
        if(!checkPermission(context, Manifest.permission.INTERNET)){
            return null;
        }

        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            if (en == null) {
                return "";
            }

            while (en.hasMoreElements()) {
                NetworkInterface intf = en.nextElement();
                Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();

                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = enumIpAddr.nextElement();

                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            Log.w(TAG, "Cannot get local ipaddress", e);
        }

        return null;
    }

    /**
     * 获取系统版本号
     * @return
     */
    public static String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取电量
     * @return
     */
    public static int getBatteryLevel(){
        return sBatteryLevel;
    }

    /**
     * 获取包名
     */
    public static String getAppPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 获取平台类型: Android
     * @return
     */
    public static String getPlatform() {
        return "Android";
    }

    /**
     * 检查网络是否连接
     * @param context
     * @return
     */
    private static boolean isNetworkConnected(Context context){
        boolean isNetworkConnected = false;

        if(!checkPermission(context, Manifest.permission.ACCESS_NETWORK_STATE)){
            return false;
        }

        ConnectivityManager connectivityManager = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if(null == connectivityManager){
            return false;
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (null != networkInfo && networkInfo.isConnected());
    }

    /**
     * 获取Android的版本号
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {

        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId == null ? null : androidId.toUpperCase();
    }

    /**
     * 获取电话号码
     * @param context
     * @return
     */
    public static String getPhoneNo(Context context) {

        if (!checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            return "";
        }

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }

        String phoneNo = tm.getLine1Number();
        return phoneNo == null ? "" : phoneNo;
    }

    /**
     * 判断是否为模拟器(普通方式)
     * It is hard to recognize all the emulator in the market. This is a normal way.
     *
     * @param context
     * @return
     */
    public static boolean isEmulator(Context context) {
        if (!checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            return false;
        }

        if (Build.MODEL.equals("sdk") || Build.MODEL.equals("google_sdk")) {
            return true;
        }

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return false;
        }
        String imei = tm.getDeviceId();
        if ((imei != null && imei.equals("000000000000000"))) {
            return true;
        }

        return false;
    }

    /**
     * 获取基站定位数据
     * @param context
     * @return
     */
    public static String getCellLocation(Context context) {

        if (!checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            return "";
        }

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return "";
        }

        CellLocation cellLocation = tm.getCellLocation();
        if (cellLocation == null) {
            return "";
        }
        return cellLocation.toString();
    }

    /**
     * 生成Queen ID.
     * @return
     */
    private static String generateDeviceId(){
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }


    /**
     * 注册电池监听广播
     * register to obtain the battery info;
     */
    public static void registerBatteryReceiver(Context context) {

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);

        BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                sBatteryLevel = (int) (100f * intent
                        .getIntExtra(BatteryManager.EXTRA_LEVEL, 0) / intent.getIntExtra
                        (BatteryManager.EXTRA_SCALE, 100));
            }
        };
        context.registerReceiver(batteryReceiver, filter);
    }


    public static boolean activityIsExist(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        boolean isExist = false;

        try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            isExist = true;
        } catch (PackageManager.NameNotFoundException e1) {
            isExist = false;
        }
        return isExist;
    }

    public static boolean isChina(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        return locale.toString().equals(Locale.CHINA.toString());
    }

    public static boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static String getVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            int versionCode = packageInfo.versionCode;
            return String.valueOf(versionCode);
        } catch (PackageManager.NameNotFoundException e1) {
            return "";
        }
    }

    public static String getVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e1) {
            return "";
        }
    }

    /**
     * 检查是否具有获取某些设备信息的权限
     * Check ther permission required;
     *
     * @param context
     * @param permissionName 权限名; Permission name;
     * @return
     */
    public static boolean checkPermission(Context context, String permissionName) {
        boolean permission = false;
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (context.checkSelfPermission(permissionName) == PackageManager.PERMISSION_GRANTED) {
//                permission = true;
//            }
//        } else {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager.checkPermission(permissionName, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                permission = true;
            }
//        }
        return permission;
    }

    public static String[] getGPUInfo(GL10 var0) {
        try {
            String[] var1 = new String[2];
            String var2 = var0.glGetString(7936);
            String var3 = var0.glGetString(7937);
            var1[0] = var2;
            var1[1] = var3;
            return var1;
        } catch (Exception var4) {
            return new String[0];
        }

    }

    public static String getCPUInfo() {
        String var0 = null;
        FileReader var1 = null;
        BufferedReader var2 = null;

        try {
            var1 = new FileReader("/proc/cpuinfo");
            if (var1 != null) {
                try {
                    var2 = new BufferedReader(var1, 1024);
                    var0 = var2.readLine();
                    var2.close();
                    var1.close();
                } catch (IOException var4) {
                    LogUtil.e(TAG, "Could not read from file /proc/cpuinfo", var4);
                }
            }
        } catch (FileNotFoundException var5) {
            LogUtil.e(TAG, "Could not open file /proc/cpuinfo", var5);
        }

        if (var0 != null) {
            int var3 = var0.indexOf(58) + 1;
            var0 = var0.substring(var3);
            return var0.trim();
        } else {
            return "";
        }
    }

    public static String getPhoneNetOperatorName(Context var0) {
        try {
            TelephonyManager var1 = (TelephonyManager) var0.getSystemService(Context.TELEPHONY_SERVICE);
            return !checkPermission(var0, "android.permission.READ_PHONE_STATE") ? "" : (var1 == null ? "" : var1.getNetworkOperatorName());
        } catch (Exception var2) {
            var2.printStackTrace();
            return "";
        }
    }

    public static String getPhoneSubId(Context var0) {
        TelephonyManager var1 = (TelephonyManager) var0.getSystemService(Context.TELEPHONY_SERVICE);
        String var2 = null;
        if (checkPermission(var0, "android.permission.READ_PHONE_STATE")) {
            var2 = var1.getSubscriberId();
        }

        return var2;
    }

    public static boolean isNetAvailable(Context var0) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) var0.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
            return networkInfo != null ? networkInfo.isConnectedOrConnecting() : false;
        } catch (Exception var3) {
            return true;
        }
    }

    public static boolean isWIFIConnected(Context var0) {
        return "Wi-Fi".equals(getNetWorkType(var0)[0]);
    }

    public static String[] getNetWorkType(Context var0) {
        String[] var1 = new String[]{"", ""};

        try {
            if (!checkPermission(var0, "android.permission.ACCESS_NETWORK_STATE")) {
                var1[0] = "";
                return var1;
            }

            ConnectivityManager var2 = (ConnectivityManager) var0.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (var2 == null) {
                var1[0] = "";
                return var1;
            }

            NetworkInfo var3 = var2.getNetworkInfo(1);
            if (var3.getState() == NetworkInfo.State.CONNECTED) {
                var1[0] = "Wi-Fi";
                return var1;
            }

            NetworkInfo var4 = var2.getNetworkInfo(0);
            if (var4.getState() == NetworkInfo.State.CONNECTED) {
                var1[0] = "2G/3G";
                var1[1] = var4.getSubtypeName();
                return var1;
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return var1;
    }


    private static String getMacFromWIFI(Context var0) {
        try {
            WifiManager var1 = (WifiManager) var0.getSystemService(Context.WIFI_SERVICE);
            if (checkPermission(var0, "android.permission.ACCESS_WIFI_STATE")) {
                WifiInfo var2 = var1.getConnectionInfo();
                return var2.getMacAddress();
            } else {
                LogUtil.e(TAG, "Could not get mac address.[no permission android.permission.ACCESS_WIFI_STATE");
                return "";
            }
        } catch (Exception var3) {
            LogUtil.e(TAG, "Could not get mac address." + var3.toString());
            return "";
        }
    }

    private static String openAddressFile() {
        String[] var0 = new String[]{"/sys/class/net/wlan0/address", "/sys/class/net/eth0/address", "/sys/devices/virtual/net/wlan0/address"};

        for (int var2 = 0; var2 < var0.length; ++var2) {
            try {
                String var1 = readAddressFile(var0[var2]);
                if (var1 != null) {
                    return var1;
                }
            } catch (Exception var4) {
                LogUtil.e(TAG, "open file  Failed", var4);
            }
        }

        return null;
    }

    private static String readAddressFile(String var0) throws FileNotFoundException {
        String var1 = null;
        FileReader var2 = new FileReader(var0);
        BufferedReader var3 = null;
        if (var2 != null) {
            try {
                var3 = new BufferedReader(var2, 1024);
                var1 = var3.readLine();
            } catch (IOException var17) {
                LogUtil.e(TAG, "Could not read from file " + var0, var17);
            } finally {
                if (var2 != null) {
                    try {
                        var2.close();
                    } catch (IOException var16) {
                        var16.printStackTrace();
                    }
                }

                if (var3 != null) {
                    try {
                        var3.close();
                    } catch (IOException var15) {
                        var15.printStackTrace();
                    }
                }

            }
        }

        return var1;
    }


    public static String getDeviceLabel(Context context) {
        String var1 = "";
        TelephonyManager var2 = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        if(Build.VERSION.SDK_INT >= 23) {
            try {
                if(checkPermission(context, "android.permission.READ_PHONE_STATE")) {
                    var1 = var2.getDeviceId();
                }
            } catch (Exception var5) {
            }

            if(TextUtils.isEmpty(var1)) {
                var1 = openAddressFile();
                if(TextUtils.isEmpty(var1)) {
                    var1 = Settings.Secure.getString(context.getContentResolver(), "android_id");
                    if(TextUtils.isEmpty(var1)) {
                        if(Build.VERSION.SDK_INT >= 9) {
                            var1 = Build.SERIAL;
                        }
                    }
                }
            }
        } else {
            if(var2 == null) {
            }

            try {
                if(checkPermission(context, "android.permission.READ_PHONE_STATE")) {
                    var1 = var2.getDeviceId();
                }
            } catch (Exception var4) {
            }

            if(TextUtils.isEmpty(var1)) {
                var1 = getMacAddress(context);
                if(TextUtils.isEmpty(var1)) {
                    var1 = Settings.Secure.getString(context.getContentResolver(), "android_id");
                }
            }
        }

        return var1;
    }
}
