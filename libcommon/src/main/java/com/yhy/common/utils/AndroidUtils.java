package com.yhy.common.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.yhy.common.DirConstants;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

public class AndroidUtils {

    /** 网络不可用 */
    public static final int NONETWORK = 0;
    /** 是wifi连接 */
    public static final int WIFI = 1;
    /** 不是wifi连接 */
    public static final int NOWIFI = 2;


    public static int getVerCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception var2) {
            Log.e("AndroidUtil", "Cannot find package and its version info.");
            return -1;
        }
    }

    public static String getVerName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception var2) {
            Log.e("AndroidUtil", "Cannot find package and its version info.");
            return "no version name";
        }
    }

    public static void hideIME(Activity context, boolean bHide) {
        if (bHide) {
            try {
                ((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 2);
            } catch (NullPointerException var4) {
                var4.printStackTrace();
            }
        } else {
            try {
                ((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(context.getCurrentFocus(), 1);
            } catch (NullPointerException var3) {
                var3.printStackTrace();
            }
        }

    }

    public static void showIMEonDialog(AlertDialog dialog) {
        try {
            Window window = dialog.getWindow();
            window.setSoftInputMode(5);
        } catch (Exception var2) {
            Log.e("AndroidUtil", var2.toString());
        }

    }

    public static boolean isPkgInstalled(Context ctx, String packageName) {
        PackageManager pm = ctx.getPackageManager();

        try {
            pm.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException var4) {
            return false;
        }
    }

    public static final int[] getResourceDeclareStyleableIntArray(Context context, String name) {
        try {
            Field[] fields2 = Class.forName(context.getPackageName() + ".R$styleable").getFields();
            Field[] arr = fields2;
            int len = fields2.length;

            for(int i = 0; i < len; ++i) {
                Field f = arr[i];
                if (f.getName().equals(name)) {
                    int[] ret = (int[]) f.get(null);
                    return ret;
                }
            }
        } catch (Throwable var8) {
            ;
        }

        return null;
    }

    public static int getStyleableResourceInt(Context context, String name) {
        if (context == null) {
            return 0;
        } else {
            try {
                Field[] fields2 = Class.forName(context.getPackageName() + ".R$styleable").getFields();
                Field[] arr = fields2;
                int len = fields2.length;

                for(int i = 0; i < len; ++i) {
                    Field f = arr[i];
                    if (f.getName().equals(name)) {
                        int ret = (Integer)f.get(null);
                        return ret;
                    }
                }
            } catch (Throwable var8) {
                ;
            }

            return 0;
        }
    }

    public static void showLongToast(Context context, String str){
        try {
            Toast.makeText(context, str, Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void showShortToast(Context context, String str){
        try {
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void showToast(Context context,int msgResId){
        if(msgResId == -1){
            return ;
        }
        Toast.makeText(context, msgResId, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context,String msg){
        if(msg == null){
            return ;
        }
        if(context == null){
            return ;
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showToastCenter(Context context,String msg){
        if(msg == null){
            return ;
        }
        if(context == null){
            return ;
        }
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
    //获取版本号(内部识别号)
    public static int getVersionCode(Context context) {
        try {
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 判断sdcard是否存在
     * @return
     */
    public static boolean isSDCardMounted(){
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    //获取版本号
    public static String getVersion(Context context) {
        try {
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "1.0.0.0";
        }
    }


    /**
     * 检测sd卡是否可用
     * @return
     */
    public static boolean checkSDCard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    /**
     * 获取sd卡路径
     * @return
     */
    public static String getPath() {
        if (checkSDCard()) {
            return Environment.getExternalStorageDirectory().getPath();
        }
        return "";
    }

    /**
     * 获取sd卡默认缓存文件路径
     * @param ctx
     * @return eg:/storage/emulated/0/Android/data/项目包名/files
     */
    public static String getDefaultCachePath(Context ctx){
        if (checkSDCard()) {
//            File file =  ctx.getExternalFilesDir(null);
//            if (null ==  file) {
//                return Environment.getExternalStorageDirectory() + DIR_WORK;
//            }
//            return file.getAbsolutePath();
            return DirConstants.DIR_CACHE;
        }
        return "";
    }

    public static int getScreenWidth(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context){
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5F);
    }

    public static int dip2px(Context context, int dip){
        return dp2px(context, Integer.valueOf(dip).floatValue());
    }

    public static int px2dip(Context context, int px){
        float density = getDensity(context);
        return (int)((px - 0.5) / density);
    }

    private static float getDensity(Context ctx){
        return ctx.getResources().getDisplayMetrics().density;
    }

    /**
     * ５40 的分辨率上是85 （
     * @return
     */
    public static int getScal(Context context){
        return (int)(getScreenWidth(context) * 100 / 480);
    }

    /**
     * 宽全屏, 根据当前分辨率　动态获取高度
     * height 在８００*４８０情况下　的高度
     * @return
     */
    public static int get480Height(Context context, int height480){
        int width = getScreenWidth(context);
        return (height480 * width / 480);
    }

    /**
     * 获取状态栏高度
     * @return
     */
    public static int getStatusBarHeight(Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    @SuppressLint("MissingPermission")
    public static int getNetWorkType(Context context) {
        if (!isNetWorkAvalible(context)) {
            return NONETWORK;
        }
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting())
            return WIFI;
        else
            return NOWIFI;
    }

    public static boolean isNetWorkAvalible(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        @SuppressLint("MissingPermission") NetworkInfo ni = cm.getActiveNetworkInfo();
        return !(ni == null || !ni.isAvailable());
    }
}
