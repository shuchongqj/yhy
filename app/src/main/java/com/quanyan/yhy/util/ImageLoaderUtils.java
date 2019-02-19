package com.quanyan.yhy.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

//import com.bumptech.glide.load.resource.drawable.GlideDrawable;
//import com.bumptech.glide.request.animation.GlideAnimation;
//import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

/**
 * Created by Administrator on 2018-03-06.
 */

public class ImageLoaderUtils {

//    public static void displayScaleImage(Context context, final ImageView imageView, String url, final PhotoViewAttacher photoViewAttacher) {
//        if (imageView == null) {
//            throw new IllegalArgumentException("argument error");
//        }
//
//        Glide.with(context).
//                load(url)
//                .placeholder(R.mipmap.icon_default_215_215)
//                .error(R.mipmap.icon_default_215_215)
//                .into(new GlideDrawableImageViewTarget(imageView) {
//                    @Override
//                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
//                        super.onResourceReady(resource, animation);
//                        if (photoViewAttacher != null) {
//                            photoViewAttacher.update();
//                        }
//                    }
//                });
//    }

    public static void downLoadImage(final String url, final Context context) {
//        Glide.with(context).asBitmap().load(url).toBytes().into(new SimpleTarget<byte[]>() {
//            @Override
//            public void onResourceReady(byte[] bytes, GlideAnimation<? super byte[]> glideAnimation) {
//                try {
//                    String fileName = System.currentTimeMillis() + ".png";
//                    saveFileToSD(context, fileName, bytes);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        Glide.with(context).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                try {
                    String fileName = System.currentTimeMillis() + ".png";
                    saveFileToSD(context, fileName, resource);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //往SD卡写入文件的方法
    public static void saveFileToSD(Context context, String filename, byte[] bytes) throws Exception {
        //如果手机已插入sd卡,且app具有读写sd卡的权限
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String filePath = Environment.getExternalStorageDirectory().getCanonicalPath() + "/yhy";
            File dir1 = new File(filePath);
            if (!dir1.exists()) {
                dir1.mkdirs();
            }
            filename = filePath + "/" + filename;
            //这里就不要用openFileOutput了,那个是往手机内存中写数据的
            FileOutputStream output = new FileOutputStream(filename);
            output.write(bytes);
            //将bytes写入到输出流中
            output.close();
            //关闭输出流
//            Toast.makeText(context, "图片已成功保存到" + filePath, Toast.LENGTH_SHORT).show();
            Toast.makeText(context, "图片已成功保存至相册", Toast.LENGTH_SHORT).show();
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(new File(filename))));
        } else
            Toast.makeText(context, "SD卡不存在或者不可读写", Toast.LENGTH_SHORT).show();
    }

    public static void saveFileToSD(Context context, String filename, Bitmap bitmap) throws Exception {
        //如果手机已插入sd卡,且app具有读写sd卡的权限
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String filePath = Environment.getExternalStorageDirectory().getCanonicalPath() + "/yhy";
            File dir1 = new File(filePath);
            if (!dir1.exists()) {
                dir1.mkdirs();
            }
//            filename = filePath + "/" + filename;
            //这里就不要用openFileOutput了,那个是往手机内存中写数据的
            FileOutputStream output = new FileOutputStream(filePath + "/" + filename);
            bitmap.compress(Bitmap.CompressFormat.PNG, 70, output);
            output.flush();
            output.close();
            //关闭输出流
//            Toast.makeText(context, "图片已成功保存到" + filePath, Toast.LENGTH_SHORT).show();
            Toast.makeText(context, "图片已成功保存至相册", Toast.LENGTH_SHORT).show();
            // 其次把文件插入到系统图库
            try {
                MediaStore.Images.Media.insertImage(context.getContentResolver(),
                        filePath + "/" + filename, filename, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(new File(filename))));
        } else
            Toast.makeText(context, "SD卡不存在或者不可读写", Toast.LENGTH_SHORT).show();
    }
}