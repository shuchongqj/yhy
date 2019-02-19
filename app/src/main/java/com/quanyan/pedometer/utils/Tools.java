package com.quanyan.pedometer.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.lidroid.xutils.util.LogUtils;
import com.quanyan.yhy.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sky on 14-3-28.
 */
public class Tools {
    public static final String PEDO_SHORTCUT = "pedo_shortcut";
    public static final String PEDO_SHORTCUT_FIRST = "pedo_shortcut_first";
    public static int getAppVersionCode(Context context) {
        int version = 0;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            version = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            version = 1;
        }
        return version;
    }

    public static int dip2px(Context context, int dip) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) (dip * dm.density + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static float heightDy(Context context) {
        int height = ScreenSize.getScreenHeight(context);
        return height * 1.0f / 1100;
    }

    public static boolean isPhoneNumber(String text) {
        if (text == null || text.length() == 0 || !text.matches("\\d{11}")) {
            return false;
        }
        return true;
    }



    public static void sendMessage(String content) {
        SmsManager smsManager = SmsManager.getDefault();
        List<String> divideContents = smsManager.divideMessage(content);
        for (String text : divideContents) {
            smsManager.sendTextMessage("150xxxxxxxx", "13512345678", text,
                    null, null);
        }
    }

    public static boolean hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {

            inputMethodManager.hideSoftInputFromWindow(activity
                    .getCurrentFocus().getWindowToken(), 0);

            View a = activity.getCurrentFocus();
            a.setFocusableInTouchMode(false);
            a.clearFocus();
            a.setFocusableInTouchMode(true);
            return true;
        } else {
            return false;
        }
    }

    public static <Element> List<Element> reverse(List<Element> list) {
        List<Element> newlist = new ArrayList<Element>();
        for (int i = list.size() - 1; i >= 0; i--) {
            newlist.add(list.get(i));
        }
        return newlist;
    }
    
    public static void createShortcut(Activity ctx) {  
//        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
//        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, ctx.getString(R.string.pedo_app_name));
//        shortcut.putExtra("duplicate", false);//设置是否重复创建
//        Intent intent = new Intent(SPUtils.ACTION_PEDOMETER_CHECK);
//        intent.addCategory(Intent.CATEGORY_DEFAULT);
//        intent.putExtra(PEDO_SHORTCUT, true);
////        intent.setClass(ctx, CheckActivity.class);//设置第一个页面
//        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
//        ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(ctx, R.mipmap.pedo_launcher);
//        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
//        ctx.sendBroadcast(shortcut);
//        SPUtils.save(ctx, PEDO_SHORTCUT, true);
    }

    public static void delShortcut(Activity ctx){ 
//        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
//        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, ctx.getString(R.string.pedo_app_name));
//        String appClass = ctx.getPackageName() + "." + ctx.getLocalClassName();
//        ComponentName comp = new ComponentName(ctx.getPackageName(), appClass);
//        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(SPUtils.ACTION_PEDOMETER_CHECK).addCategory(Intent.CATEGORY_DEFAULT));
//
//        ctx.sendBroadcast(shortcut);
//        SPUtils.save(ctx, PEDO_SHORTCUT, false);
    }

    public static boolean hasShortcut(Activity ctx) {
            boolean isInstallShortcut = false;
            final ContentResolver cr = ctx.getContentResolver();
            String AUTHORITY ="com.android.launcher2.settings";
            Uri CONTENT_URI = Uri.parse("content://" +AUTHORITY + "/favorites");
            Cursor c = null;
            try {
	            c = cr.query(CONTENT_URI,new String[] {"title","iconResource" },"title=?",
	            new String[] {ctx.getString(R.string.quanyan_app_name).trim()}, null);
	            if(c!=null && c.getCount()>0){
	                isInstallShortcut = true ;
	            }
	            LogUtils.d("Has shortcut 2 is=" +isInstallShortcut);
            } catch (Exception e) {
            	LogUtils.e(e.toString(), e);
            } finally {
            	if(c != null) {
            		c.close();
            	}
            }

            AUTHORITY ="com.android.launcher.settings";
            CONTENT_URI = Uri.parse("content://" +AUTHORITY + "/favorites");
            try {
	            c = cr.query(CONTENT_URI,new String[] {"title","iconResource" },"title=?",
	            new String[] {ctx.getString(R.string.quanyan_app_name).trim()}, null);
	            if(c!=null && c.getCount()>0){
	                isInstallShortcut = true ;
	            }
	            LogUtils.d("Has shortcut is =" +isInstallShortcut);
            } catch (Exception e) {
            	LogUtils.e(e.toString(), e);
            } finally {
            	if(c != null) {
            		c.close();
            	}
            }
            
            AUTHORITY ="com.htc.launcher.settings";
            CONTENT_URI = Uri.parse("content://" +AUTHORITY + "/favorites");
            try {
	            c = cr.query(CONTENT_URI,new String[] {"title","iconResource" },"title=?",
	            new String[] {ctx.getString(R.string.quanyan_app_name).trim()}, null);
	            if(c!=null && c.getCount()>0){
	                isInstallShortcut = true ;
	            }
	            LogUtils.d("Has shortcut HTC is =" +isInstallShortcut);
            } catch (Exception e) {
            	LogUtils.e(e.toString(), e);
            } finally {
            	if(c != null) {
            		c.close();
            	}
            }
            
            AUTHORITY ="com.miui.mihome.launcher2.settings";
            CONTENT_URI = Uri.parse("content://" +AUTHORITY + "/favorites");
            try {
	            c = cr.query(CONTENT_URI,new String[] {"title","iconResource" },"title=?",
	            new String[] {ctx.getString(R.string.quanyan_app_name).trim()}, null);
	            if(c!=null && c.getCount()>0){
	                isInstallShortcut = true ;
	            }
	            LogUtils.d("Has shortcut xiaomi is =" +isInstallShortcut);
            } catch (Exception e) {
            	LogUtils.e(e.toString(), e);
            } finally {
            	if(c != null) {
            		c.close();
            	}
            }
            return isInstallShortcut ;
        }
}
