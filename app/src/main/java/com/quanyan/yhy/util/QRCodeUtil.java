package com.quanyan.yhy.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.quanyan.base.util.StringUtil;
import com.quanyan.yhy.R;
import com.quanyan.yhy.common.CommonUrl;
import com.quanyan.yhy.common.SysConfigType;
import com.yhy.common.beans.net.model.NativeBean;
import com.yhy.common.beans.net.model.user.NativeDataBean;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created with Android Studio.
 * Title:QRCodeUtil
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/7/6
 * Time:上午11:46
 * Version 1.1.0
 */
public class QRCodeUtil {
    private static final int QR_WIDTH = 300;
    private static final int QR_HEIGHT = 300;

    public static final String URL_BEFORE_HEADER = "http://www.yingheying.com/d/";
    public static final String URL_CHANNAL_TYPE = "quanyan";
    public static final String URL_CHANNAL_AFTER = "?appUri=";

//    public static final String URL_CHANNAL_TYPE = "quanyan?";
//    public static final String URL_CHANNAL_AFTER = "appUri=";
    //    public static final String URL_HEADER = "quanyan://app?content=";
    public static final String URL_HEADER = "yhyapp://app?content=";
    public static final String URL_DATA_TYPE = "QUANYAN";
    public static final String URL_INDEX_NAME = "appUri";

    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    /**
     * 生成300X300大小的二维码
     *
     * @param content
     * @return
     */
    public static Bitmap GenorateImage(String content) {
        try {
            // 需要引入core包
            QRCodeWriter writer = new QRCodeWriter();
            if (content == null || "".equals(content) || content.length() < 1) {
                return null;
            }
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.MARGIN, 8);
            //容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            BitMatrix bitMatrix = writer.encode(content,
                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成带logo的二维码图
     */
    public static Bitmap GenorateImage(Context context, String content) {
        Bitmap logoBm = BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_icon_qr);
        try {
            // 需要引入core包
            QRCodeWriter writer = new QRCodeWriter();
            if (content == null || "".equals(content) || content.length() < 1) {
                return null;
            }
            // 把输入的文本转为二维码
            BitMatrix martix = writer.encode(content, BarcodeFormat.QR_CODE,
                    QR_WIDTH, QR_HEIGHT);
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 8);
            BitMatrix bitMatrix = new QRCodeWriter().encode(content,
                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                    Bitmap.Config.ARGB_8888);

            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
//            if (logoBm != null) {
//                bitmap = addLogo(bitmap, logoBm);
//            }
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成固定大小的二维码
     *
     * @param content
     * @param qrWidth
     * @param qrHeight
     * @return
     */
    public static Bitmap GenorateImage(String content, int qrWidth, int qrHeight) {
        try {
            // 需要引入core包
            QRCodeWriter writer = new QRCodeWriter();
            if (content == null || "".equals(content) || content.length() < 1) {
                return null;
            }
            // 把输入的文本转为二维码
            BitMatrix martix = writer.encode(content, BarcodeFormat.QR_CODE,
                    qrWidth, qrHeight);
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 8);
            BitMatrix bitMatrix = new QRCodeWriter().encode(content,
                    BarcodeFormat.QR_CODE, qrWidth, qrHeight, hints);
            int[] pixels = new int[qrWidth * qrHeight];
            for (int y = 0; y < qrHeight; y++) {
                for (int x = 0; x < qrWidth; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * qrWidth + x] = 0xff000000;
                    } else {
                        pixels[y * qrWidth + x] = 0xffffffff;
                    }
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(qrWidth, qrHeight,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, qrWidth, 0, 0, qrWidth, qrHeight);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 在二维码中间添加Logo图案
     */
    public static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }

        if (logo == null) {
            return src;
        }

        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }

        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }

        //logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 6 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }

        return bitmap;
    }

    /**
     * 生成二维码Bitmap
     *
     * @param content  内容
     * @param filePath 用于存储二维码图片的文件路径
     * @return 生成二维码及保存文件是否成功
     */
    public static boolean createQRImage(Context context, String content, String filePath) {
        Bitmap logoBm = BitmapFactory.decodeResource(context.getResources(), R.mipmap.app_icon_qr);
        try {
            if (content == null || "".equals(content)) {
                return false;
            }

            //配置参数
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错级别
            /*switch (android.os.Build.MODEL){
                case
            }*/
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 8);
            //设置空白边距的宽度
//            hints.put(EncodeHintType.MARGIN, 2); //default is 4

            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }

            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);

            if (logoBm != null) {
                bitmap = addLogo(bitmap, logoBm);
            }

            //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
            return bitmap != null && bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath));
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    //保存图片
    public static boolean saveImg(Context context, Bitmap bitmap, String filePath) {
        try {
            return bitmap != null && bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 生成带图标的二维码
     *
     * @param context
     * @param content
     * @return
     */
    public static Bitmap createQrAddImg(Context context, String content) {
        Bitmap logoBm = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_default_avatar);
        try {
            if (content == null || "".equals(content)) {
                return null;
            }

            //配置参数
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 8);
            //设置空白边距的宽度
//            hints.put(EncodeHintType.MARGIN, 2); //default is 4

            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }

            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);

            if (logoBm != null) {
                bitmap = addLogo(bitmap, logoBm);
                return bitmap;
            }

        } catch (WriterException e) {
            e.printStackTrace();
        }

        return null;
    }


    //开启选择相册的界面
    public static void selectOnePicture(Activity context, int reqCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        context.startActivityForResult(intent, reqCode);
    }

    //判断是否是图片
    public static boolean isPicture(String pic) {
        String[] suffix = {"png", "jpg", "jpeg"};
        int index = pic.lastIndexOf(".");
        if (index != -1) {
            String endString = pic.substring(index + 1);
            for (int i = 0; i < suffix.length; i++) {
                if (endString.equalsIgnoreCase(suffix[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    //步步吸金生成链接
    public static String getUserPageUrl(Context context, String operation, NativeDataBean nativeDataBean) {
        NativeBean nativeBean = new NativeBean();
        nativeBean.setTYPE(URL_DATA_TYPE);
        nativeBean.setOPERATION(operation);
        if (nativeDataBean != null) {
            nativeBean.setData(nativeDataBean);
        }
        String data = "";
        try {
            data = nativeBean.serialize().toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String shareUrlSuffix = CommonUrl.getShareUrlSuffix(context, SysConfigType.URL_ADD_QR_HEAD);
        if (!StringUtil.isEmpty(shareUrlSuffix)) {
            String sharedata =  URL_HEADER + data;
            try {
                sharedata = java.net.URLEncoder.encode(sharedata, "utf-8");

            } catch (Exception e) {
                e.printStackTrace();
            }
            String shareUrl = shareUrlSuffix + URL_CHANNAL_TYPE + URL_CHANNAL_AFTER +sharedata;
            return shareUrl;
        }
        return null;
    }

    //商品详情生成链接
    public static String getProductPageUrl(Context context, NativeDataBean nativeDataBean) {
        NativeBean nativeBean = new NativeBean();
        nativeBean.setTYPE(URL_DATA_TYPE);
        nativeBean.setOPERATION("YHY_BUY_DETAIL");
        if (nativeDataBean != null) {
            nativeBean.setData(nativeDataBean);
        }
        String data = "";
        try {
            data = nativeBean.serialize().toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String shareUrlSuffix = CommonUrl.getShareUrlSuffix(context, SysConfigType.URL_ADD_QR_HEAD);
        if (!StringUtil.isEmpty(shareUrlSuffix)) {
            String sharedata =  URL_HEADER + data;
            try {
                sharedata = java.net.URLEncoder.encode(sharedata, "utf-8");

            } catch (Exception e) {
                e.printStackTrace();
            }
            String shareUrl = shareUrlSuffix + URL_CHANNAL_TYPE + URL_CHANNAL_AFTER +sharedata;
            return shareUrl;
        }
        return null;
    }

    //扫码判断
    public static boolean parseUrl(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        String shareUrlSuffix = CommonUrl.getShareUrlSuffix(context, SysConfigType.URL_ADD_QR_HEAD);
        if (!StringUtil.isEmpty(shareUrlSuffix)) {
            return url.startsWith(shareUrlSuffix + URL_CHANNAL_TYPE + URL_CHANNAL_AFTER);
        }
        return false;
    }

    //判断是否是url
    public static boolean parseHttp(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        try {
            URL url1 = new URL(url);
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //获取url 指定name的value
    public static String getValueByName(String url, String name) {
        String result = "";
        int index = url.indexOf("?");
        String temp = url.substring(index + 1);
        String[] keyValue = temp.split("\\&");
        for (String str : keyValue) {
            if (str.contains(name)) {
                result = str.replace(name + "=", "");
            }
        }
        return result;
    }

}
