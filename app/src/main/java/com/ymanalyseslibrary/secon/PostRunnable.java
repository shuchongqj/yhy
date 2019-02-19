package com.ymanalyseslibrary.secon;

import android.content.Context;

import com.quanyan.yhy.common.DirConstants;
import com.yhy.common.config.ContextHelper;
import com.yhy.common.utils.SPUtils;
import com.ymanalyseslibrary.AnalysesConstants;
import com.ymanalyseslibrary.alinterface.PostInterface;
import com.ymanalyseslibrary.log.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPOutputStream;


/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:1/28/16
 * Time:09:24
 * Version 1.0
 */
public class PostRunnable implements Runnable {
    private static final String TAG = "post runnable";

    private Context mContext;
    private JSONObject mPostBody;
    private PostInterface mPostInterface;
    private int mIndex;
    public PostRunnable(Context context, PostInterface postInterface, int position, JSONObject postBody) {
        this.mContext = context;
        this.mPostInterface = postInterface;
        this.mIndex = position;
        this.mPostBody = postBody;
    }

    @Override
    public void run() {
        String logUrl = ContextHelper.getLogUrl();
        LogUtil.d(TAG, "Http URL : " + logUrl);
        postCacheFile(mContext, logUrl, mPostBody);

        //ceshiWrite(mContext, mPostBody);
        //FileUtil.deleteFile(new File(DirConstants.DIR_LOGS + "ceShiAna.txt"));
    }

    //测试写入
    private void ceshiWrite(Context mContext, JSONObject mPostBody) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(DirConstants.DIR_LOGS + "ceShiAna.txt"), true);
            fos.write(mPostBody.toString().getBytes());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //FileUtil.writeContent(DirConstants.DIR_LOGS + "ceShiAna.txt", mPostBody.toString());
        /*File cacheFile = new File(mContext.getCacheDir().getPath(), "ceShiAna.txt");
        try {
            if (!cacheFile.exists()) {
                cacheFile.createNewFile();
            }
            AtomicFile atomicFile = new AtomicFile(cacheFile);
            FileOutputStream fileOutputStream = atomicFile.startWrite();
            fileOutputStream.write(mPostBody.toString().getBytes("utf-8"));
            atomicFile.finishWrite(fileOutputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * 网络操作，上传信息
     *
     * @param context
     * @param url
     * @param postContent
     */
    private void postCacheFile(Context context, String url, JSONObject postContent) {
        /*Log.d(TAG, postContent.toString());
        // TODO: 1/27/16  测试代码
        try {
            Thread.sleep(2000);

            mPostInterface.onSuccess(mIndex, postContent.getString(AnalysesConstants.BODY_KEY_DATA));
//            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // TODO: 1/27/16 上传完成后将index发送通知，删除对应内容，同步操作
        if(mPostBody == null || mPostBody.isNull(AnalysesConstants.BODY_KEY_DATA)){
            return;
        }*/
        LogUtil.i(TAG, "max dot   " + SPUtils.getDCMaxLength(context) + "");
        LogUtil.d(TAG, postContent.toString());
        URL postUrl = null;
        HttpURLConnection connection = null;
        try {
            postUrl = new URL(url);

            connection = (HttpURLConnection) postUrl.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("content-type", "text/html");
            connection.setRequestProperty("Content-Encoding", "gzip");
            connection.setRequestProperty("isGzip", "true");
//            connection.setRequestProperty("charset", "utf-8");
//            connection.setRequestProperty("Accept-Encoding", "gzip");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(false);
            connection.setConnectTimeout(20000);
            connection.connect();

            /**-----固定压缩顺序，不要更改-----**/
            OutputStream outputStream = connection.getOutputStream();
            byte[] bytes = mPostBody.toString().getBytes("UTF-8");
            LogUtil.d(TAG, "length before  : " + bytes.length);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            gzip.write(bytes);
            gzip.close();

            LogUtil.d(TAG, "length end : " + out.toByteArray().length);
            outputStream.write(out.toByteArray());
            outputStream.flush();
            outputStream.close();
            /**-----固定压缩顺序，不要更改 ends-----**/

            InputStream inputStream = connection.getInputStream();

            inputStream.close();

            int responseCode = connection.getResponseCode();
            LogUtil.d(TAG, "Http response : " + responseCode);
            if (HttpURLConnection.HTTP_OK == responseCode) {
                LogUtil.d(TAG,  postContent.getString(AnalysesConstants.BODY_KEY_DATA));
                mPostInterface.onSuccess(mIndex, postContent.getString(AnalysesConstants.BODY_KEY_DATA));
            } else {
                mPostInterface.onFailed(mIndex, postContent.getString(AnalysesConstants.BODY_KEY_DATA));
            }
            /*connection.disconnect();
            connection = null;*/
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            LogUtil.e(TAG, e.toString());
            LogUtil.e(TAG, e.getMessage());
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
    }

}
