package com.yhy.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class NetworkUtil {
	public static boolean isNetworkAvailable(Context context) {
		boolean res = false;
		int reTry = 3;

		// if(NetworkUtil.getIMSI(context) == null){
		// return res;
		// }

//		if (isAirplaneModeOn(context)) {
//			return res;
//		}

		for (int i = 0; i < reTry; i++) {
			try {
				ConnectivityManager nm = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = nm.getActiveNetworkInfo();
				if (null != networkInfo) {
					res = networkInfo.isConnected();
				}
				break;
			} catch (Exception e) {

			}
		}
		return res;
	}

	public static String getPhoneType(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
			return "CDMA";
		} else if (tm.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
			return "GSM";
		} else {
			return "WCDMA";
		}
	}

	public static String getNetworkType(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null) {
			return ni.getTypeName();
		}
		return null;
	}

	public static String getMacAddress(Context context){
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		return wifiInfo.getMacAddress();
	}
}
