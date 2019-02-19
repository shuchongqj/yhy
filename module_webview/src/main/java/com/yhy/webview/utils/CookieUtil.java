package com.yhy.webview.utils;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

public class CookieUtil {

	public static void cleanCookie(Context ctx) {
		CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(ctx);
		cookieSyncManager.sync();

		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
		CookieSyncManager.getInstance().sync();
	}
}
