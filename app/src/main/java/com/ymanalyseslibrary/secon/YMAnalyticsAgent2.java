package com.ymanalyseslibrary.secon;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.util.AtomicFile;
import android.text.TextUtils;
import android.util.SparseArray;

import com.quanyan.yhy.common.DirConstants;
import com.quanyan.yhy.ui.base.utils.PageNameUtils;
import com.ymanalyseslibrary.AnalysesConstants;
import com.ymanalyseslibrary.AnalysesExceptionHandler;
import com.ymanalyseslibrary.AnalysesExceptionInterface;
import com.ymanalyseslibrary.AnalyticsConfig;
import com.ymanalyseslibrary.actinfo.ActSesstionUtil;
import com.ymanalyseslibrary.actinfo.ActivityStatistic;
import com.ymanalyseslibrary.data.EventDataEncap;
import com.ymanalyseslibrary.log.LogUtil;
import com.ymanalyseslibrary.tool.DataEncapUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:使用SparseArray存储
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:1/28/16
 * Time:09:10
 * Version 1.0
 */
public class YMAnalyticsAgent2 implements AnalysesExceptionInterface {

    private static final String TAG = "YMAnalysesAgent";
    private Context mContext;
    //    private Handler mHandler;
    private boolean isInitialed = false;
    private AnalysesExceptionHandler mAnalysesExceptionHandler = new AnalysesExceptionHandler();
    private YMAnalyticsDataWrapper mYMAnalyticsDataWrapper;

    private ActivityStatistic mActivityStatistic = new ActivityStatistic();
    private ActSesstionUtil mActSesstionUtil = new ActSesstionUtil();


    YMAnalyticsAgent2() {
        mAnalysesExceptionHandler.setAnalysesExceptionInterface(this);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void initAgent(Context context) {
        if (!isInitialed) {
            mContext = context.getApplicationContext();
            if (mYMAnalyticsDataWrapper == null) {
                mYMAnalyticsDataWrapper = new YMAnalyticsDataWrapper(context);
            }
            //从文件中读取存储的内容
            if(readCacheContent(mContext) != null){
                String fileStr = readCacheContent(mContext).toString();
                if(fileStr != null && fileStr.length() > 0){
                    //mYMAnalyticsDataWrapper.setContent(fileStr);
                    mYMAnalyticsDataWrapper.dataNetWork(fileStr, false);
                }
//            HandlerThread localHandlerThread = new HandlerThread("");
//            localHandlerThread.start();
//            mHandler = new Handler(localHandlerThread.getLooper());
                isInitialed = true;
            }

        }

    }

    /**
     * app启动的时候调用
     *
     * @param context
     */
    public void uploadFirstBegin(Context context) {

        initAgent(context);

        /*mContext = context.getApplicationContext();
        if (mYMAnalyticsDataWrapper == null) {
            mYMAnalyticsDataWrapper = new YMAnalyticsDataWrapper(context);
        }
        if(readCacheContent(context) != null){
            String fileStr = readCacheContent(mContext).toString();
            if(fileStr != null && fileStr.length() > 0){
                mYMAnalyticsDataWrapper.dataNetWork(fileStr, false);
            }

            isInitialed = true;
        }*/

        //第一次启动创建文件夹
        /*if (isAppFirstStart(context)) {
            setAppStartFirst(context);
            creatAnalyFile(context);
        }else {
            String fileStr = readCacheContent(mContext).toString();
            if(fileStr != null && fileStr.length() > 0){
                //context.deleteFile(AnalysesConstants.ANALYSES_CACHE_FILE_NAME);
                //mYMAnalyticsDataWrapper.setContent(fileStr);
                mYMAnalyticsDataWrapper.dataNetWork(fileStr, false);
            }
        }*/
        //mYMAnalyticsDataWrapper.setCurrentContent(mCache);
    }

    /**
     * 创建文件
     */
    private void creatAnalyFile(Context context) {
        //String firePathName = context.getFilesDir().toString();
        File file = new File(DirConstants.DIR_LOGS, AnalysesConstants.ANALYSES_CACHE_FILE_NAME);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            LogUtil.e(TAG, "create AnalyFile fail");
            e.printStackTrace();
        }

    }

    /**
     * 设置第一次启动标志位
     */
    private void setAppStartFirst(Context context) {
        context.getSharedPreferences(AnalysesConstants.PREFERENCE_NAME, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(AnalysesConstants.ANALYSES_FIRST_START, false)
                .commit();
    }

    /**
     * 判断是否第一次启动
     *
     * @param context
     * @return
     */
    private boolean isAppFirstStart(Context context) {
        Boolean value = false;
        SharedPreferences preferences = context.getSharedPreferences(AnalysesConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        value = preferences.getBoolean(AnalysesConstants.ANALYSES_FIRST_START, true);
        return value;
    }

    /**
     * 登录
     *
     * @param context
     * @param userID
     */
    public void onUserLogin(Context context, String userID) {
        context.getSharedPreferences(AnalysesConstants.PREFERENCE_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(AnalysesConstants.KEY_USER_ACCOUNT, userID)
                .putLong(AnalysesConstants.KEY_USER_LOGIN_TIME, System.currentTimeMillis())
                .putLong(AnalysesConstants.KEY_USER_EXIT_TIME, -1L)
                .commit();
    }

    /**
     * 退出
     *
     * @param context
     */
    public void onUserLoginOut(Context context) {
        context.getSharedPreferences(AnalysesConstants.PREFERENCE_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(AnalysesConstants.KEY_USER_ACCOUNT, "")
                .putLong(AnalysesConstants.KEY_USER_LOGIN_TIME, -1L)
                .putLong(AnalysesConstants.KEY_USER_EXIT_TIME, System.currentTimeMillis())
                .commit();
    }

//    /**
//     * 系统启动
//     * @param context
//     */
//    public void onStart(Context context) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(AnalysesConstants.PREFERENCE_NAME,
//                Context.MODE_PRIVATE);
//        SharedPreferences.Editor edit = sharedPreferences.edit();
//        edit.putLong(AnalysesConstants.KEY_START_TIME, System.currentTimeMillis());
//        edit.commit();
//    }
//
//    /**
//     * 系统退出
//     * @param context
//     */
//    public void onExit(Context context) {
//        if(!isInitialed){
//            initAgent(context);
//        }
//
//        SharedPreferences sharedPreferences = context.getSharedPreferences(AnalysesConstants.PREFERENCE_NAME,
//                Context.MODE_PRIVATE);
//        HashMap<String, Object> stringLongMap = new HashMap<>();
//        stringLongMap.put("appStart", sharedPreferences.getLong(AnalysesConstants.KEY_START_TIME, -1L));
//        stringLongMap.put("appExit", System.currentTimeMillis());
//
//        mYMAnalyticsDataWrapper.appendContent(new MapValueWrapper("appStart", stringLongMap));
//    }

    public void onResume(Context context) {
        if (context == null) {
            LogUtil.e(TAG, "the context is null");
        } else {

            if (!isInitialed) {
                initAgent(context);
            }
            if(context instanceof Activity){
                String pageName = PageNameUtils.getChineseName((Activity) context);
                if (AnalyticsConfig.ACTIVITY_DURATION_OPEN) {
                    //mActivityStatistic.onResume(pageName);
                    Map<String, String> map = new HashMap<>();
                    map.put(AnalysesConstants.BODY_KEY_EVENT_KEY_1, pageName);
                    onEvent(context, AnalysesConstants.ACTIVITY_ONRESUME, map);
                }
            }


            //mActSesstionUtil.onResume(context);
        }
    }

    public void onPause(Context context) {
        if (context == null) {
            LogUtil.e(TAG, "the context is null");
        } else {
            if (!isInitialed) {
                initAgent(context);
            }
            if(context instanceof Activity){
                String pageName = PageNameUtils.getChineseName((Activity) context);
                if (AnalyticsConfig.ACTIVITY_DURATION_OPEN) {
                    //mActivityStatistic.onPause(pageName);
                    Map<String, String> map = new HashMap<>();
                    map.put(AnalysesConstants.BODY_KEY_EVENT_KEY_1, pageName);
                    onEvent(context, AnalysesConstants.ACTIVITY_ONPAUSE, map);
                }
            }


            //mActSesstionUtil.onPause(context);
            //mActivityStatistic.storeTheActititiesSession(context);
        }
    }

    /**
     * 保存文件
     *
     * @param context
     */
    public void onDestroy(Context context) {
        if (context == null) {
            LogUtil.e(TAG, "the context is null");
        } else {
            mYMAnalyticsDataWrapper.saveFile(context);
        }
    }

    /*public void onEvent(Context context, String eventID){
        if(!isInitialed){
            initAgent(context);
        }

        if(!TextUtils.isEmpty(eventID)) {
            HashMap<String, Object> event = new HashMap<>();
            event.put(eventID, "");
            event.put("time", System.currentTimeMillis());
            mYMAnalyticsDataWrapper.appendContent(new MapValueWrapper(eventID, event));
        }else{
            LogUtil.d(TAG, "The eventID is NULL");
        }
    }*/

    /**
     * 传eventID打点
     *
     * @param context
     * @param eventID
     */
    public void onEvent(Context context, String eventID) {
        if(context == null){
            return ;
        }
        if (!isInitialed) {
            initAgent(context);
        }
        if (!TextUtils.isEmpty(eventID)) {
            HashMap<String, Object> event = new HashMap<>();
            event.put(AnalysesConstants.BODY_KEY_EVENT_ID, eventID);
            DataEncapUtils.encapNormalValue(context, event);
            mYMAnalyticsDataWrapper.appendContent(context, new EventDataEncap(event).toString());
        } else {
            LogUtil.d(TAG, "The eventID is NULL");
        }
    }

    /**
     * 打点事件
     *
     * @param context
     * @param eventID
     * @param eventValue
     * @param eventMap
     */
    public void onEvent(Context context, String eventID, String eventValue, Map<String, String> eventMap) {
        if (!isInitialed) {
            initAgent(context);
        }
        if (!TextUtils.isEmpty(eventID)) {
            String content = DataEncapUtils.eventValueEnacp(context, eventID, eventValue, eventMap);
            mYMAnalyticsDataWrapper.appendContent(context, content);
        } else {
            LogUtil.d(TAG, "The eventID is NULL");
        }
    }

    /**
     * 打点事件
     *
     * @param context
     * @param eventID
     * @param eventStr
     */
    public void onEvent(Context context, String eventID, String eventStr) {
        onEvent(context, eventID, eventStr, null);
    }

    /**
     * 打点事件
     *
     * @param context
     * @param eventID
     * @param eventMap
     */
    public void onEvent(Context context, String eventID, Map<String, String> eventMap) {
        onEvent(context, eventID, null, eventMap);
    }

    /**
     * 实时传递数据
     *
     * @param context
     * @param eventID
     */
    public void onCurrentEvent(Context context, String eventID) {
        if (!isInitialed) {
            initAgent(context);
        }
        if (!TextUtils.isEmpty(eventID)) {
            HashMap<String, Object> event = new HashMap<>();
            event.put(AnalysesConstants.BODY_KEY_EVENT_ID, eventID);
            DataEncapUtils.encapNormalValue(context, event);
            mYMAnalyticsDataWrapper.netAccess(new EventDataEncap(event).toString());
        } else {
            LogUtil.d(TAG, "The eventID is NULL");
        }
    }

    /**
     * 实时传递数据
     *
     * @param context
     * @param eventID
     * @param eventValue
     * @param eventMap
     */
    public void onCurrentEvent(Context context, String eventID, String eventValue, Map<String, String> eventMap) {
        if (!isInitialed) {
            initAgent(context);
        }
        if (!TextUtils.isEmpty(eventID)) {
            String content = DataEncapUtils.eventValueEnacp(context, eventID, eventValue, eventMap);
            mYMAnalyticsDataWrapper.netAccess(content);
        } else {
            LogUtil.d(TAG, "The eventID is NULL");
        }
    }

    /**
     * 实时传递数据
     *
     * @param context
     * @param eventID
     * @param eventStr
     */
    public void onCurrentEvent(Context context, String eventID, String eventStr) {
        onCurrentEvent(context, eventID, eventStr, null);
    }

    /**
     * 实时传递数据
     *
     * @param context
     * @param eventID
     * @param eventMap
     */
    public void onCurrentEvent(Context context, String eventID, Map<String, String> eventMap) {
        onCurrentEvent(context, eventID, null, eventMap);
    }

    /**
     * 从文件中读取内容到{@link SparseArray}和缓存中
     *
     * @param context
     * @return
     */
    public StringBuilder readCacheContent(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        File cacheFile = new File(DirConstants.DIR_LOGS, AnalysesConstants.ANALYSES_CACHE_FILE_NAME);
        try {
            if (!cacheFile.exists()) {
                cacheFile.createNewFile();
            }
            AtomicFile atomicFile = new AtomicFile(cacheFile);
            byte[] content = atomicFile.readFully();
            stringBuilder.append(new String(content));

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return stringBuilder;
    }

    /**
     * 程序退出时，保存采集信息
     *
     * @param context
     * @param content
     */
    public synchronized void writeCacheContent(Context context, String content) {

        File cacheFile = new File(DirConstants.DIR_LOGS, AnalysesConstants.ANALYSES_CACHE_FILE_NAME);
        try {
            if (!cacheFile.exists()) {
                cacheFile.createNewFile();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(cacheFile, true);
            byte[] contentByte = content.getBytes("utf-8");
            fileOutputStream.write(contentByte, 0, contentByte.length);
            //atomicFile.finishWrite(fileOutputStream);
            /*AtomicFile atomicFile = new AtomicFile(cacheFile);
            FileOutputStream fileOutputStream = atomicFile.startWrite();
            fileOutputStream.write(content.getBytes("utf-8"));
            atomicFile.finishWrite(fileOutputStream);*/

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件
     * @param file
     */
    public void deleteFile(File file){
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    this.deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        } else {
            LogUtil.i(TAG, "fire not found");
        }
    }

    public void deleteFile(){
        File cacheFile = new File(DirConstants.DIR_LOGS, AnalysesConstants.ANALYSES_CACHE_FILE_NAME);
        if (cacheFile.exists()) { // 判断文件是否存在
            if (cacheFile.isFile()) { // 判断是否是文件
                cacheFile.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (cacheFile.isDirectory()) { // 否则如果它是一个目录
                File files[] = cacheFile.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    this.deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            cacheFile.delete();
        } else {
            LogUtil.i(TAG, "fire not found");
        }
    }

    @Override
    public void catchException(Throwable throwable) {
        // TODO: 1/27/16 系统异常时，保存数据
        if (mContext == null) {
            return;
        }
//        int size = mStringSparseArray.size();
//        for(int i = 0; i < size; i ++){
//            if(mStatusArray.get(mStatusArray.keyAt(i)) != AnalysesStatus.success) {
//                writeCacheContent(mContext, mStringSparseArray.get(mStringSparseArray.keyAt(i)));
//            }
//        }
    }

    /**
     * 异常上传操作
     *
     * @param context
     * @param error
     */
    public void reportError(Context context, String error) {
        if (!TextUtils.isEmpty(error)) {
            if (context == null) {
                LogUtil.e(TAG, "unexpected null context in reportError");
            } else {
                //传递错误日志

            }
        }
    }
}
