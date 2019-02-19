package com.harwkin.nb.camera;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.text.TextUtils;

import com.lidroid.xutils.util.LogUtils;
import com.quanyan.base.util.MD5Utils;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.common.DirConstants;
import com.yhy.common.config.ContextHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageUtils {
    static int MAX_UPLOAD_FILE_SIZE = 100 * 1024;

    public static String prepareUpload(String filePath) {
        String newFilePath;
        int scaleFactor;
        if (filePath == null) {
            return null;
        }

        File oldFile = new File(filePath);
        if (!oldFile.exists()) {
            return null;
        }

        if (oldFile.length() <= MAX_UPLOAD_FILE_SIZE) {
            return filePath;
        }
        newFilePath = DirConstants.DIR_PIC_ORIGIN + "/tmp.jpg";
        File newFile = new File(newFilePath);
        if (newFile.exists()) {
            newFile.deleteOnExit();
        }
        try {
            newFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(newFile);
            scaleFactor = (int) (oldFile.length() / MAX_UPLOAD_FILE_SIZE);
            Options op = new Options();
            op.inSampleSize = scaleFactor;
            Bitmap newBitmap = BitmapFactory.decodeFile(filePath, op);
            newBitmap.compress(CompressFormat.JPEG, 80, fos);
            return newFilePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        if (bm == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(CompressFormat.JPEG, 80, baos);
        return baos.toByteArray();
    }

    // byte[]转换成Bitmap
    public static Bitmap Bytes2Bitmap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        return null;
    }

    /**
     * 获取图片的全路径
     *
     * @param imageName
     * @return
     */
    public static String getImageFullUrl(String imageName) {
        if (imageName == null) {
            return null;
        }

        if (!TextUtils.isEmpty(imageName) && imageName.startsWith("http:")) {
            return imageName;
        }

        if (!TextUtils.isEmpty(imageName) && imageName.startsWith("https:")) {
            return imageName;
        }

        if (!TextUtils.isEmpty(imageName) && imageName.startsWith("file:")) {
            return imageName;
        }

        String str = imageName.toLowerCase();
        if(str.endsWith(".gif")){
            return getThumbnailFullPath(imageName, null);
        }
        return ContextHelper.getImageUrl() + imageName;

    }

    /**
     * 获取H5链接全路径
     * @param h5Name
     * @return
     */
    public static String getH5FullUrl(String h5Name) {
        if (h5Name == null) {
            return null;
        }

        if (!TextUtils.isEmpty(h5Name) && h5Name.startsWith("http:")) {
            return h5Name;
        }
        return ContextHelper.getImageUrl() + h5Name;

    }
    /**
     * 拼出缩略图全地址*
     * @param
     * @param sizeStr 大小 eg. “300x300”
     * @return url full path
     */
    public static String getThumbnailFullPath(String imageName, String sizeStr) {
        if(StringUtil.isEmpty(imageName)){
            return null;
        }
        if (imageName.startsWith("http:")) {
            return imageName;
        }
        String format = "";
        int index = imageName.lastIndexOf(".");
        if (index > 0) {
            format = imageName.substring(index, imageName.length());
            imageName = imageName.substring(0,index);
        }
//        if(!StringUtil.isEmpty(sizeStr)) {
//            return ContextHelper.getImageUrl() + imageName + "_" + sizeStr + format;
//        }else {
//            return ContextHelper.getImageUrl() + imageName + format;
//        }
        return ContextHelper.getImageUrl() + imageName + format;
    }

    public static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    static final int ROUNDED_CORNER_COLOR = -12434878;
    public static Bitmap xform(Bitmap bitmap, int boxWidth, int boxHeight) {
        int src_w = bitmap.getWidth();
        int src_h = bitmap.getHeight();
        if(boxHeight <= 0 && boxWidth <= 0) {
            return Bitmap.createScaledBitmap(bitmap, src_w, src_h, true);
        } else {
            if(boxHeight <= 0) {
                boxHeight = (int)((float)src_h / (float)src_w * (float)boxWidth);
            } else if(boxWidth <= 0) {
                boxWidth = (int)((float)src_w / (float)src_h * (float)boxHeight);
            }

            return Bitmap.createScaledBitmap(bitmap, boxWidth, boxHeight, true);
        }
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.WHITE);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap getRoundedCircleBitmap(Bitmap bitmap, int borderWidth, int borderColor, Context context) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_4444);

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        //--CROP THE IMAGE
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2 - 1, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        //--ADD BORDER IF NEEDED
        if(borderWidth > 0){
            final Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint2.setAntiAlias(true);
            paint2.setColor(context.getResources().getColor(borderColor));
            paint2.setStrokeWidth(borderWidth);
            paint2.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, (float) (bitmap.getWidth() / 2 - Math.ceil(borderWidth / 2)), paint2);
        }
        return output;
    }

    public static Bitmap decodeSampledBitmapFromByteArray(byte[] data, int offset, int length, int reqWidth, int reqHeight) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, offset, length, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, offset, length, options);
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    private static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if(height > reqHeight || width > reqWidth) {
            if(width > height) {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            } else {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }

        return inSampleSize;
    }

    /**
     * 从缓存种获取图片
     * @param url
     * @return
     */
    public static Bitmap getBitmapFromSD(String url) {
        Bitmap bitmap = null;
        if (!TextUtils.isEmpty(url)) {
            if (isFileExist(url)) {
                LogUtils.d("download from sd");
                try {
                    final File cacheDir = new File(DirConstants.DIR_PIC_ORIGIN);
                    File newFile = new File(cacheDir, MD5Utils.toMD5(url));
                    String localUrl = newFile.getAbsolutePath();
                    bitmap = BitmapFactory.decodeFile(localUrl);
                    LogUtils.d("bitmap: " + bitmap);
                } catch (Exception e) {
                    LogUtils.e(e.toString());
                }
            }
        }
        return bitmap;
    }

    /**
     * 判断图片文件是否存在
     * @param urlString
     * @return
     */
    public static boolean isFileExist(String urlString) {
        final File cacheDir = new File(DirConstants.DIR_PIC_ORIGIN);
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }
        File newFile = new File(cacheDir, MD5Utils.toMD5(urlString));
        LogUtils.e("new file: " + newFile.getAbsolutePath() + " isFileExist()=" + newFile.exists());
        return newFile.exists();
    }

    /**
     * 保存到相册
     */
    public static void saveToGallary(Context context,File path,String fileName){
        // 其次把文件插入到系统图库
//        try {
//            MediaStore.Images.Media.insertImage(context.getContentResolver(),
//                    path.getAbsolutePath(), fileName, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        //TODO 这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你保存的图片了！，记得要传你更新的file哦
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(path);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }
}
