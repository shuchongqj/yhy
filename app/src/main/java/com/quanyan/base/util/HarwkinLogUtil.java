package com.quanyan.base.util;

import android.util.Log;

import com.yhy.common.types.AppDebug;


public class HarwkinLogUtil {
	private final static String TAG = "Harwkin";
	private final static String TIME = "time = " + System.currentTimeMillis() + "----";
	public static void info(String msg){
		if(msg == null){
			return ;
		}
		if(AppDebug.NET_DEBUG){
			Log.i(TAG,  TIME + msg);
		}
	}
	
	public static void error(String msg){
		if(msg == null){
			return ;
		}
		if(AppDebug.NET_DEBUG){
			Log.e(TAG, TIME + msg);
		}
	}
	
	public static void warning(String msg){
		if(msg == null){
			return ;
		}
		if(AppDebug.NET_DEBUG){
			Log.w(TAG, TIME + msg);
		}
	}
	
	public static void info(String tag,String msg){
		if(msg == null){
			return ;
		}
		if(AppDebug.NET_DEBUG){
			Log.i(tag, TIME + msg);
		}
	}
	
	public static void error(String tag,String msg){
		if(msg == null){
			return ;
		}
		if(AppDebug.NET_DEBUG){
			Log.e(tag, TIME + msg);
		}
	}
	
	public static void warning(String tag,String msg){
		if(msg == null){
			return ;
		}
		if(AppDebug.NET_DEBUG){
			Log.w(tag, TIME + msg);
		}
	}
}
