package com.quanyan.yhy.net.cache;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.harwkin.nb.camera.FileUtil;
import com.quanyan.base.util.MD5Utils;
import com.quanyan.yhy.ui.base.utils.SDCardUtil;
import com.yhy.common.utils.JSONUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LocalJsonCache<Result extends Object> {
    public static String cachePath;
    private ReadWriteLock mLock = new ReentrantReadWriteLock();

    private Callback<Result> mCallBack;

    public static final String CACHE_DIR = "caches";

    public LocalJsonCache(Context context) {
        init(context);
    }

    private void init(Context context) {
        cachePath = getCacheLocalPath(context);
    }

    /**
     * 异步保存json类
     * 
     * @param key
     * @param t
     */
    public void save(String key, Result t) {

        String fileName = generateKey(key);
        if (!TextUtils.isEmpty(fileName)) {
            new Thread(new SaveThread(fileName, t)).start();
        }
    }

    /**
     * 同步保存json类
     * 
     * @param key
     * @param t
     */
    public void syncSave(String key, Result t) {
        String fileName = generateKey(key);
        if (!TextUtils.isEmpty(fileName)) {
            doSave(fileName, t);
        }
    }

    /**
     * 异步解析文件方法
     * 
     * @param key
     * @param t
     */
    public void load(String key, Class<Result> t) {
        String fileName = generateKey(key);

        if (!TextUtils.isEmpty(fileName)) {
            new Thread(new ParseThread(fileName, t)).start();
        }
    }

    /**
     * 同步解析文件方法
     * 
     * @param key
     * @param t
     * @return
     */
    public Result syncLoad(String key, Class<Result> t) {
        String fileName = generateKey(key);
        if (!TextUtils.isEmpty(fileName)) {
            return doParse(fileName, t);
        }
        return null;
    }

    /**
     * 删除文件
     * 
     * @param key
     */
    public void remove(String key) {
        String fileName = generateKey(key);
        if (!TextUtils.isEmpty(fileName)) {
            File file = new File(cachePath, fileName);
            try {
                mLock.readLock().lock();
                file.delete();
            } finally {
                mLock.readLock().unlock();
            }
        }
    }

    /**
     * 生成md5的key值
     * 
     * @param key
     * @return
     */
    private String generateKey(String key) {
        if (!TextUtils.isEmpty(key)) {
            return MD5Utils.toMD5(key);
        }
        return null;
    }

    public void setCallback(Callback<Result> callback) {
        mCallBack = callback;
    }

    /**
     * 回调类
     * 
     * @param <Result>
     */
    public interface Callback<Result> {
        public void onSave(String path);

        public void onParse(String path, Result t);
    }

    /**
     * 保存文件线程
     */
    private class SaveThread implements Runnable {
        String fileName;
        Result pojo;

        public SaveThread(String fileName, Result obj) {
            this.fileName = fileName;
            this.pojo = obj;
        }

        @Override
        public void run() {
            doSave(fileName, pojo);
        }
    }

    /**
     * 解析文件线程
     */
    private class ParseThread implements Runnable {
        String fileName;
        Class<Result> pojo;

        public ParseThread(String fileName, Class<Result> t) {
            this.fileName = fileName;
            pojo = t;
        }

        @Override
        public void run() {
            doParse(fileName, pojo);
        }

    }

    /**
     * 
     * 将实体类解析成json 并存入本地文件中
     * 
     * @param fileName
     * @param pojo
     */
    private void doSave(String fileName, Result pojo) {
        BufferedOutputStream bos = null;
        try {
            String jsonRes = JSONUtils.toJson(pojo);
            File file = new File(cachePath, fileName + ".tmp");
            FileUtil.createFile(file.getAbsolutePath());
            mLock.writeLock().lock();
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(jsonRes.getBytes("utf-8"));
            bos.flush();

            file.renameTo(new File(cachePath, fileName));

            if (mCallBack != null) {
                mCallBack.onSave(file.getAbsolutePath());
            }
        } catch (Exception e) {
        } finally {
            try {
                mLock.writeLock().unlock();

                if (bos != null) {
                    bos.close();
                }
            } catch (Exception e) {

            }
        }

    }

    /**
     * 读取本地json文件并解析成实体类
     * 
     * @param fileName
     * @param pojo
     * @return
     */
    private Result doParse(String fileName, Class<Result> pojo) {
        long start = System.currentTimeMillis();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FileInputStream fis = null;
        File file = new File(cachePath, fileName);
        try {
            mLock.readLock().lock();
            fis = new FileInputStream(file);
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }

            String resJson = bos.toString("utf-8");
            Result obj = JSONUtils.convertToObject(resJson, pojo);
            if (mCallBack != null) {
                mCallBack.onParse(file.getAbsolutePath(), obj);
            }
            return obj;
        } catch (Exception e) {
            if (mCallBack != null) {
                mCallBack.onParse(file.getAbsolutePath(), null);
            }
        } finally {
            try {
                mLock.readLock().unlock();
                if (fis != null) {
                    fis.close();
                }
                bos.close();
                long time =  (System.currentTimeMillis() - start);
            } catch (Exception e) {
            }
        }
        return null;
    }
    /**
     * 移除缓存
     */
    public void removeAll(){
        File[] files = new File(cachePath).listFiles();
        if (files == null) {
            return;
        }
        try {
            mLock.readLock().lock();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        } finally {
            mLock.readLock().unlock();
        }
    }

    /**
     *
     * 文件缓存地址
     *
     * @param ctx
     * @return
     */
    public static final String getCacheLocalPath(Context ctx) {
        String path = "";
        if (SDCardUtil.checkSDCard()) {
            path = SDCardUtil.getDefaultCachePath(ctx);
        } else {
            path = ctx.getFilesDir().getAbsolutePath();
            Log.d("zc", "path" + path);

        }
        if (TextUtils.isEmpty(path)) {
            return "";
        }

        path += File.separator + CACHE_DIR + File.separator;

        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }
}
