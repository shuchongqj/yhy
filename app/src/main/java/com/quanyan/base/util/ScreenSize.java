package com.quanyan.base.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import java.lang.reflect.Method;

/**
 * 屏幕适配、像素转换相关的类
 */
public class ScreenSize
{
	public static float xDpi = 0;
	public static float yDpi = 0;
		
	private void loadDpi(Activity activity, Display disp){

		//clearDpiPrefs(activity);
		
		try {		
			DisplayMetrics dm = new DisplayMetrics();
			disp.getMetrics(dm);
			
			boolean isHorz = disp.getWidth() > disp.getHeight();

			float xD = dm.xdpi * dm.density;
			float yD = dm.ydpi * dm.density;
			
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
			String lds = sp.getString("long_dpi", "0");
			String sds = sp.getString("short_dpi", "0");
			float ld = TryParse(lds, 0);
			float sd = TryParse(sds, 0);
			
			if (ld != 0 && sd != 0){
				if (isHorz){
					xDpi = ld;
					yDpi = sd;
				} else{
					xDpi = sd;
					yDpi = ld;
				}
			} else{
				xDpi = xD;
				yDpi = yD;
				saveDpiToPrefs(activity);
			}
		} catch (Exception ex){
			Toast.makeText(activity, "Exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
		}	
	}
	
	private void saveDpiToPrefs(Activity activity){
		WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
		Display disp = wm.getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		disp.getMetrics(dm);

		boolean isHorz = disp.getWidth() > disp.getHeight();

		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
		
		SharedPreferences.Editor e = sp.edit();
		e.putString("long_dpi", "" + (isHorz ? xDpi : yDpi));
		e.putString("short_dpi", "" + (isHorz ? yDpi : xDpi));
		e.commit();
	}
	
	private void clearDpiPrefs(Activity activity){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
		SharedPreferences.Editor e = sp.edit();
		e.putString("long_dpi", "0");
		e.putString("short_dpi", "0");
		e.commit();	
	}
	
	public double getExpectedDiagonal(Activity activity){
		
		WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
		Display disp = wm.getDefaultDisplay();
		
		if (xDpi == 0 || yDpi == 0)
		{
			loadDpi(activity, disp);
		}
		
		int width = 0;
		int height = 0;
		try{
		
			Method mGetRawW = Display.class.getMethod("getRawWidth");
			Method mGetRawH = Display.class.getMethod("getRawHeight");
			width = (Integer)mGetRawW.invoke(disp);
			height = (Integer)mGetRawH.invoke(disp);
			
			//Toast.makeText(activity, "Raw: " + width + "x" + height, 2000).show();
		} catch(Exception e){
			width = disp.getWidth();
			height = disp.getHeight();
			
			//Toast.makeText(activity, "WH: " + width + "x" + height, 2000).show();
		}		
		
		double ws = width / xDpi;
		double hs = height / yDpi;
		
		double diag = Math.sqrt(ws*ws + hs*hs);
		
		return diag;
	}
	
	public void setDiagonal(double diagonal, Activity activity){
		double d = getExpectedDiagonal(activity);
		
		xDpi = (float)(xDpi * d / diagonal);
		yDpi = (float)(yDpi * d / diagonal);
		
		saveDpiToPrefs(activity);
	}

    public static int getScreenHeightExcludeStatusBar(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        disp.getMetrics(dm);
        return dm.heightPixels - getStatusBarHeight(context);
    }

	public static int getScreenHeightIncludeStatusBar(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display disp = wm.getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		disp.getMetrics(dm);
		return dm.heightPixels;
	}

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
		if(disp != null) {
			disp.getMetrics(dm);
		}
        return dm.widthPixels;
    }
    
    /** 转换dip为px **/
	public static int convertDIP2PX(Context context, int dip) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
	}

	/** 转换px为dip **/
	public static int convertPX2DIP(Context context, int px) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f * (px >= 0 ? 1 : -1));
	}

	public static float TryParse(String str, float defaultFloat){
		try{
			float f = Float.parseFloat(str);
			return f;
		} catch (NumberFormatException ex){
			return defaultFloat;
		}
	}

	public static int dp2px(Context context, float dpValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int)(dpValue * scale + 0.5F);
	}

	/**
	 * 获取状态栏高度
	 *
	 * @return
	 */
	public static int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		java.lang.reflect.Field field = null;
		int x = 0;
		int statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
			return statusBarHeight;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusBarHeight;
	}
}
