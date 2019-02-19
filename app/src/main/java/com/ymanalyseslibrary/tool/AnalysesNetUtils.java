package com.ymanalyseslibrary.tool;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.ymanalyseslibrary.log.LogUtil;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;


/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Mac: 1A:59:36:70:62:6A  Mac.length: 19
 * 192.168.200.85
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:1/28/16
 * Time:11:05
 * Version 1.0
 */
public class AnalysesNetUtils {

    /**
     * 得到当前的手机网络类型
     * 使用NetworkInfo的getType()方法可以判断是WiFi还是手机网络。手机网络的情况下，
     * 使用NetworkInfo的 getSubtype()方法去和TelephonyManager的网络类型常量值去比较，
     * 判断是何种具体网络。
     * TelephonyManager的网络类型常量值如下（API 17）：

     1、NETWORK_TYPE_1xRTT：         常量值：7        网络类型：1xRTT

     2、NETWORK_TYPE_CDMA ：         常量值：4       网络类型： CDMA （电信2g）

     3、NETWORK_TYPE_EDGE：          常量值：2       网络类型：EDGE（移动2g）

     4、NETWORK_TYPE_EHRPD：      常量值：14    网络类型：eHRPD

     5、NETWORK_TYPE_EVDO_0：      常量值：5     网络类型：EVDO  版本0.（电信3g）

     6、NETWORK_TYPE_EVDO_A：      常量值：6     网络类型：EVDO   版本A （电信3g）

     7、NETWORK_TYPE_EVDO_B：      常量值：12   网络类型：EVDO  版本B（电信3g）

     8、NETWORK_TYPE_GPRS：          常量值：1         网络类型：GPRS （联通2g）

     9、NETWORK_TYPE_HSDPA：        常量值：8      网络类型：HSDPA（联通3g）

     10、NETWORK_TYPE_HSPA：         常量值：10     网络类型：HSPA

     11、NETWORK_TYPE_HSPAP：       常量值：15   网络类型：HSPA+

     12、NETWORK_TYPE_HSUPA：       常量值：9     网络类型：HSUPA

     13、NETWORK_TYPE_IDEN：          常量值：11      网络类型：iDen

     14、NETWORK_TYPE_LTE：             常量值：13       网络类型：LTE(3g到4g的一个过渡，称为准4g)

     15、NETWORK_TYPE_UMTS：          常量值：3    网络类型：UMTS（联通3g）

     16、NETWORK_TYPE_UNKNOWN：常量值：0  网络类型：未知
     * @param context
     * @return
     */
    public static String getCurrentNetType(Context context) {
        String type = "";
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            type = "NULL";
        } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            type = "WIFI";
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            type = "MOBILE";
//            int subType = info.getSubtype();
//            if (subType == TelephonyManager.NETWORK_TYPE_CDMA || subType == TelephonyManager.NETWORK_TYPE_GPRS
//                    || subType == TelephonyManager.NETWORK_TYPE_EDGE) {
//                type = "2g";
//            } else if (subType == TelephonyManager.NETWORK_TYPE_UMTS || subType == TelephonyManager.NETWORK_TYPE_HSDPA
//                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_A || subType == TelephonyManager.NETWORK_TYPE_EVDO_0
//                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_B) {
//                type = "3g";
//            } else if (subType == TelephonyManager.NETWORK_TYPE_LTE) {// LTE是3g到4g的过渡，是3.9G的全球标准
//                type = "4g";
//            }
        }
        return type;
    }

    public static String getIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                //网络接口是否开启（有的手机有虚拟网卡和网络共享ip），再次过滤
                if(!intf.isVirtual() && intf.isUp() && !intf.isLoopback()) {
//                Log.i("ip address", "" + intf.getDisplayName());
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()
                                && !inetAddress.isLinkLocalAddress()
                                && inetAddress.isSiteLocalAddress()
                                && inetAddress instanceof Inet4Address) {
//                        Log.i("ip address", "" + inetAddress.getHostAddress());
                            return inetAddress.getHostAddress().toString();
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("IpAddress", ex.toString());
        }
        return null;
    }

    /**
     * 通过本地ip获取mac地址
     *
     * @return
     */
    @SuppressWarnings("finally")
    public static String getMacAddressFromIp() {
        String mac_s = "";
        try {
            byte[] mac;
            List<NetworkInterface> nilist = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface ni : nilist) {
                List<InetAddress> ialist = Collections.list(ni.getInetAddresses());
                for (InetAddress address : ialist) {
                    if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                        NetworkInterface ne = NetworkInterface.getByInetAddress(address);
                        mac = ne.getHardwareAddress();
                        if (mac.length > 0) {
                            mac_s = byte2mac(mac);
                        }
                    }
                }

            }

        } catch (SocketException ex) {
            LogUtil.e("ip", ex.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return mac_s;
        }
    }

    private static String byte2mac(byte[] b) {
        StringBuffer hs = new StringBuffer(b.length);
        String stmp = "";
        int len = b.length;
        for (int n = 0; n < len; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1) {
                hs = hs.append("0").append(stmp);
            } else {
                hs = hs.append(stmp);
            }
        }
        StringBuffer str = new StringBuffer(hs);
        for (int i = 0; i < str.length(); i++) {
            if (i % 3 == 0) {
                str.insert(i, ':');
            }
        }
        return str.toString().substring(1);
    }

//    /**
//     * 获取Ethernet的MAC地址
//     *
//     * @return
//     */
//    public static String getMacAddress() {
//        try {
//            return loadFileAsString("/sys/class/net/eth0/address").toUpperCase(Locale.ENGLISH).substring(0, 17);
//        } catch (IOException e) {
//            return "no found";
//        }
//    }
//
//    private static String loadFileAsString(String filePath) throws java.io.IOException {
//        StringBuffer fileData = new StringBuffer(1000);
//        BufferedReader reader = new BufferedReader(new FileReader(filePath));
//        char[] buf = new char[1024];
//        int numRead = 0;
//        while ((numRead = reader.read(buf)) != -1) {
//            String readData = String.valueOf(buf, 0, numRead);
//            fileData.append(readData);
//        }
//        reader.close();
//        return fileData.toString();
//    }

    /**
     * 获取wifi mac
     *
     * @return
     */
    public static String getWifiMac(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
//            wifiManager.setWifiEnabled(true);
        }
        WifiInfo info = wifiManager.getConnectionInfo();
        return info.getMacAddress() == null ? "" : info.getMacAddress();
    }

    /**
     * 判断网络可用性
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return !(networkInfo == null || !networkInfo.isAvailable());
    }
}
