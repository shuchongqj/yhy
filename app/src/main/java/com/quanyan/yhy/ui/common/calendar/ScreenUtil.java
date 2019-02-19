package com.quanyan.yhy.ui.common.calendar;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class ScreenUtil {
	/**
	 * 获取屏幕dpi
	 * 
	 * @param context
	 * @return
	 */
	public static float getDensity(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getApplicationContext().getResources().getDisplayMetrics();
		float density = dm.density;
		return density;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 获取当前屏幕宽度
	 * @param ctx
	 * @return
	 */
	public static int getScreenWidth(Context ctx) {
		if (null == ctx) {
			return 0;
		}
		WindowManager windowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		if (Build.VERSION.SDK_INT < 13) {
			return display.getWidth();
		}
		Point point = new Point();
		display.getSize(point);
		return point.x;
	}

	/**
	 * 获取当前屏幕高度
	 * @param ctx
	 * @return
	 */
	public static int getScreenHeight(Context ctx) {
		if (null == ctx) {
			return 0;
		}
		WindowManager windowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		if (Build.VERSION.SDK_INT < 13) {
			return display.getHeight();
		}
		Point point = new Point();
		display.getSize(point);
		return point.y;
	}
}
