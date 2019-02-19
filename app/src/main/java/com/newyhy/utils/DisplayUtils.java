package com.newyhy.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.quanyan.base.util.ScreenSize;
import com.yhy.common.constants.ValueConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Glide图片加载工具类
 * <p>
 * Created by yangboxue on 2018/4/11.
 */

public class DisplayUtils {

    /**
     * 获得屏幕宽度，单位为px
     */
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获得屏幕高度,单位为px
     */
    public static int getScreenHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static int dp2px(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    public static int px2dp(Context context, int px) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
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

    /**
     * 获取列表图片的高度
     *
     * @return
     */
    public static int getImgHeight(Context context) {
        return (int) (ScreenSize.getScreenWidth(context) * ValueConstants.SCALE_VALUE);
    }

    /**
     * 获取截图
     *
     * @return
     */
    public static String getScreenShot(Context context, View view) {

        Bitmap bitmap = getBitmap(view);
        if (bitmap == null) {
            return null;
        }

        return saveAsBitmap(context, bitmap, "", "");
    }

    public static Bitmap getBitmap(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public static String saveAsBitmap(Context context, Bitmap bitmap, String folderName, String fileName) {
        String parentpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/yhy/";
        if (TextUtils.isEmpty(fileName)) {
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
            fileName = format.format(date) + ".jpg";
        }

        File pic = new File(parentpath, fileName);
        if (!pic.getParentFile().exists())
            pic.getParentFile().mkdirs();

        if (pic.exists())
            pic.delete();

        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(pic));
            MediaScannerConnection.scanFile(context, new String[]{pic.toString()}, null, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bitmap.recycle();
        return pic.getAbsolutePath();
    }
}
