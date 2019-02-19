package com.quanyan.yhy.util;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

public class CookieUtil {
	private Context mContext;
	private String mDomain;

	public CookieUtil(Context ctx, String domain) {
		mContext = ctx;
		mDomain = domain;
	}

//	public void syncCookie(String url) {
//		try {
//
//			CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(mContext);
//			cookieSyncManager.sync();
//
//			CookieManager cookieManager = CookieManager.getInstance();
//			cookieManager.removeAllCookie();
//
//			if (mDomain != null && !TextUtils.isEmpty(mDomain)) {
//				setCookieFromDomain(url, cookieManager,mDomain);
//			} else {
//				setCookieFromDomain(url, cookieManager, DomainUtils.getDomain());
//				String dom = DomainUtils.getDomain();
//				setCookieFromDomain(url, cookieManager, dom);
//			}
//			CookieSyncManager.getInstance().sync();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	private void setCookieFromDomain(String url, CookieManager cookieManager, String domain) {
//		cookieManager.setCookie(url,String.format("_wtk=%s;path=/;domain=%s",
//				SPUtils.getWebUserToken(mContext),
//				domain));
//
//		cookieManager.setCookie(url,String.format("_uid=%s;path=/;domain=%s",
//				SPUtils.getUid(mContext),
//				domain));
//
//	}

	public static void cleanCookie(Context ctx) {
		CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(ctx);
		cookieSyncManager.sync();

		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
		CookieSyncManager.getInstance().sync();
	}
}
