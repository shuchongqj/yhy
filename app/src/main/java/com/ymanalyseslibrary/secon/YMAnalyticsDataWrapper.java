package com.ymanalyseslibrary.secon;

import android.content.Context;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.quanyan.base.util.HarwkinLogUtil;
import com.quanyan.yhy.net.NetThreadManager;
import com.yhy.common.types.AppDebug;
import com.yhy.common.utils.SPUtils;
import com.ymanalyseslibrary.AnalysesStatus;
import com.ymanalyseslibrary.AnalyticsConfig;
import com.ymanalyseslibrary.alinterface.PostInterface;
import com.ymanalyseslibrary.data.AnalyticsDataWrapper;
import com.ymanalyseslibrary.log.LogUtil;
import com.ymanalyseslibrary.tool.AnalysesNetUtils;
import com.ymanalyseslibrary.tool.DataEncapUtils;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:数据读写处理
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:2/17/16
 * Time:10:28
 * Version 1.0
 */
public class YMAnalyticsDataWrapper implements PostInterface {

    private static final String TAG = "YMAnalyticsDataWrapper";
    private Context mContext;

    private static YMAnalyticsAgent2 mAgent = new YMAnalyticsAgent2();
    private StringBuilder mCacheContentBuilder = new StringBuilder();
    private StringBuilder mWriteFileBuilder = new StringBuilder();
    private SparseArray<String> mContent = new SparseArray<>();
    private SparseIntArray mContentStatus = new SparseIntArray();

    private static int mSparseKey = 1;

    public YMAnalyticsDataWrapper(Context context) {
        mContext = context.getApplicationContext();
    }

    /**
     * 添加内容
     *
     * @param content
     */
    public synchronized void setContent(String content) {
        HarwkinLogUtil.info(TAG, "setContent" + String.valueOf(content.length()));
        mCacheContentBuilder.append(content).append(";");
        /*if (mCacheContentBuilder.length() >= AnalyticsConfig.MAXLENTH) {
            dataNetWork(mCacheContentBuilder.toString(), true);
            mCacheContentBuilder.delete(0, mCacheContentBuilder.length());
        }*/
    }

    /**
     * 添加内容
     *
     * @param value
     */
    public synchronized void appendContent(final Context context, final String value) {
        HarwkinLogUtil.info(TAG, "appendContent" + String.valueOf(value.length()));
        mCacheContentBuilder.append(value).append(";");
        if(SPUtils.getDCMaxLength(context) > AnalyticsConfig.SAVE_LIMIT){
            mWriteFileBuilder.append(value).append(";");
            LogUtil.d(TAG, mWriteFileBuilder.length() + "");
            if(mWriteFileBuilder.length() >= AnalyticsConfig.SAVE_LIMIT){
                //开启线程写入
                saveFileThread(context);
            }
        }

        if (mCacheContentBuilder.length() >= SPUtils.getDCMaxLength(context)) {
            StringBuilder stringBuilder = mAgent.readCacheContent(context);
            if(stringBuilder == null || stringBuilder.length() <= 0){
                stringBuilder = new StringBuilder();
                stringBuilder.append(mCacheContentBuilder.toString());
            }
            dataNetWork(stringBuilder.toString(), true);
            //dataNetWork(mCacheContentBuilder.toString(), true);
            mCacheContentBuilder.delete(0, mCacheContentBuilder.length());
        }
    }

    private void saveFileThread(final Context context) {
        Runnable fileWrite = new Runnable() {
            @Override
            public void run() {
                saveFile(context);
            }
        };

        NetThreadManager.getInstance().execute(fileWrite);
    }

    /**
     * 直接访问网络
     *
     * @param value
     */
    public synchronized void netAccess(String value) {
        dataNetWork(value, true);
    }

    /**
     * 数据处理网络操作
     */
    public synchronized void dataNetWork(String value, Boolean flag) {
        HarwkinLogUtil.info(TAG, "dataNetWork" + String.valueOf(value.length()));
        if (value != null && value.length() > 0) {
            if (flag) {
                mContent.put(mSparseKey, value);
                mContentStatus.put(mSparseKey, AnalysesStatus.ready);
                attemptSubmit(mSparseKey, mContent.get(mSparseKey));
                mSparseKey++;
            } else {
                mContent.put(0, value);
                mContentStatus.put(0, AnalysesStatus.ready);
                attemptSubmit(0, value);
            }
        }
    }

    /**
     * 保存数据到文件中
     */
    public synchronized void saveFile(Context context) {
        StringBuilder saveStr = new StringBuilder();
        if(mWriteFileBuilder != null && mWriteFileBuilder.length() > 0){
            saveStr.append(mWriteFileBuilder.toString());
            LogUtil.d(TAG, "saveFile: " + saveStr.toString());
            mAgent.writeCacheContent(context, saveStr.toString());
            mWriteFileBuilder.delete(0, mWriteFileBuilder.length());
        }

    }

    /**
     * 网络处理
     *
     * @param position
     * @param content
     */
    public void attemptSubmit(int position, String content) {
        if(!AppDebug.DC_SUBMIT){
            return ;
        }
        HarwkinLogUtil.info(TAG, "attemptSubmit :" + content);
        StringBuilder stringBuilder = new StringBuilder();
//        if(content.endsWith(",")){
        stringBuilder.append(content);
//        }
        if (AnalysesNetUtils.isNetworkAvailable(mContext)) {
            AnalyticsDataWrapper analyticsDataWrapper = new AnalyticsDataWrapper();
            //设置头部信息
            DataEncapUtils.jointJsonHeader(mContext, analyticsDataWrapper);
            //设置数据
            analyticsDataWrapper.setData(stringBuilder.toString());
            try {
                //启动线程范围网络
                AnalyticExecutorService.execute(new PostRunnable(mContext, YMAnalyticsDataWrapper.this, position, new JSONObject(analyticsDataWrapper.getStringJsonData())));
            } catch (JSONException e) {
                LogUtil.e("postError", "has a error while wrap JSONObject");
            }
        }
    }

    @Override
    public synchronized void onSuccess(int position, String postBody) {
        // TODO: 1/28/16 上传成功后删除对应的内容
        HarwkinLogUtil.info(TAG, "success :" + postBody);
        HarwkinLogUtil.info(TAG, "success :" + position);
        //删除内容
        mContent.delete(position);
        mAgent.deleteFile();
        /*if(mCacheContentBuilder != null && mCacheContentBuilder.length() > 0){
            mCacheContentBuilder.delete(0, mCacheContentBuilder.length());
        }*/
        //int index = mContent.indexOfValue(postBody + ",");
//        mContentStatus.put(mContent.keyAt(index), AnalysesStatus.success);
//        mContent.delete(mContent.keyAt(index));
    }

    @Override
    public void onFailed(int position, String postBody) {
        AnalyticsDataWrapper analyticsDataWrapper = new AnalyticsDataWrapper();
        DataEncapUtils.jointJsonHeader(mContext, analyticsDataWrapper);
        analyticsDataWrapper.setJsonData(postBody);
        LogUtil.d("1", "baojie onFailed");
        if (AnalysesNetUtils.isNetworkAvailable(mContext)) {
            try {
                AnalyticExecutorService.scheduledExecuteForTime(new PostRunnable(mContext, YMAnalyticsDataWrapper.this,
                        position, new JSONObject(analyticsDataWrapper.toString())));
            } catch (JSONException e) {
                LogUtil.e("postError", "has a error while wrap JSONObject");
            }
        }

    }


}
