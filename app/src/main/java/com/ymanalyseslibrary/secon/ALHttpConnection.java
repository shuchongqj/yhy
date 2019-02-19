package com.ymanalyseslibrary.secon;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ymanalyseslibrary.alinterface.ALHttpResponseInterface;
import com.ymanalyseslibrary.data.AnalyticsDataWrapper;
import com.ymanalyseslibrary.log.LogUtil;
import com.ymanalyseslibrary.tool.DataEncapUtils;
import com.ymanalyseslibrary.tool.StreamUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created with Android Studio.
 * Title:AboutAndFeedController
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:zhaoxiaopo
 * Date:16/4/22
 * Time:09:38
 * Version 1.0
 */
public class ALHttpConnection {
    private Context mContext;
    private String cmwap_proxy = "10.0.0.172";
    private int wap_port = 80;

    private JSONObject mSendJsonObject;
    private ALHttpResponseInterface mResponseInterface;

    public ALHttpConnection(Context context, String data){
        mContext = context;
        mSendJsonObject = wrapContent(data);
    }

    private JSONObject wrapContent(String data) {
        AnalyticsDataWrapper analyticsDataWrapper = new AnalyticsDataWrapper();
        DataEncapUtils.jointJsonHeader(mContext, analyticsDataWrapper);
        analyticsDataWrapper.setData(data);
        return analyticsDataWrapper.getJsonData();
    }

    private boolean checkNetPermission() {
        PackageManager packageManager = mContext.getPackageManager();
        if(packageManager.checkPermission("android.permission.ACCESS_NETWORK_STATE", mContext.getPackageName()) != 0) {
            return false;
        } else {
            try {
                ConnectivityManager connectivityManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if(networkInfo != null && networkInfo.getType() != 1) {
                    String var4 = networkInfo.getExtraInfo();
                    if(var4 != null && (var4.equals("cmwap") || var4.equals("3gwap") || var4.equals("uniwap"))) {
                        return true;
                    }
                }
            } catch (Exception var5) {
                var5.printStackTrace();
            }

            return false;
        }
    }

    private byte[] sendALData(byte[] data, String url){
        byte[] result = new byte[0];
        URL postUrl = null;
        HttpURLConnection connection = null;
        try {
            postUrl = new URL(url);

            connection = (HttpURLConnection) postUrl.openConnection();
            connection.setDoInput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("content-type", "text/html");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(false);
            connection.setConnectTimeout(20000);
            connection.connect();

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data.toString().getBytes());
            outputStream.flush();
            outputStream.close();

            InputStream inputStream = connection.getInputStream();
            result = StreamUtil.getInputStreamData(inputStream);
            inputStream.close();

            int responseCode = connection.getResponseCode();
            LogUtil.d("send al", "Http response : " + responseCode);
            if (HttpURLConnection.HTTP_OK == responseCode) {
            } else {
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }
//    private byte[] sendALData(byte[] data){
//        HttpURLConnection httpURLConnection = null;
//
//        byte[] result;
//        try {
//            if(mResponseInterface != null) {
//                mResponseInterface.onFailed();
//            }
//
//            if(checkNetPermission()) {
//                Proxy var4 = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(cmwap_proxy, wap_port));
//                httpURLConnection = (HttpURLConnection)(new URL(AnalyticsConfig.URL)).openConnection(var4);
//            } else {
//                httpURLConnection = (HttpURLConnection)(new URL(AnalyticsConfig.URL)).openConnection();
//            }
//
//            httpURLConnection.setRequestProperty("YM-UTC", String.valueOf(System.currentTimeMillis()));
//            httpURLConnection.setRequestProperty("YM-Sdk", mSendJsonObject.toString());
//            httpURLConnection.setRequestProperty("Msg-Type", "envelope");
//            httpURLConnection.setConnectTimeout(10000);
//            httpURLConnection.setReadTimeout(30000);
//            httpURLConnection.setRequestMethod("POST");
//            httpURLConnection.setDoOutput(true);
//            httpURLConnection.setDoInput(true);
//            httpURLConnection.setUseCaches(false);
//            httpURLConnection.setChunkedStreamingMode(0);
//            if(Integer.parseInt(Build.VERSION.SDK) < 8) {
//                System.setProperty("http.keepAlive", "false");
//            }
//
//            OutputStream var21 = httpURLConnection.getOutputStream();
//            var21.write(data);
//            var21.flush();
//            var21.close();
//            if(mResponseInterface != null) {
//                mResponseInterface.onPostStreamSucess();
//            }
//
//            int responseCode = httpURLConnection.getResponseCode();
//            String respnonseString;
//            if(responseCode != 200) {
//                LogUtil.e("status code: ", responseCode+"");
//                respnonseString = null;
//                return respnonseString.getBytes();
//            }
//
//            respnonseString = httpURLConnection.getHeaderField("Content-Type");
//            boolean flag = false;
//            if(!TextUtils.isEmpty(respnonseString) && respnonseString.equalsIgnoreCase("application/thrift")) {
//                flag = true;
//            }
//
//            LogUtil.d("status code: ", " "+ responseCode);
//            if(!flag) {
//                return null;
//            }
//
//            LogUtil.d("Send message to ", AnalyticsConfig.URL);
//            InputStream inputStream = httpURLConnection.getInputStream();
//
//            try {
//                result = StreamUtil.getInputStreamData(inputStream);
//            } finally {
//                StreamUtil.closeInputStream(inputStream);
//            }
//        } catch (Exception ex) {
//            LogUtil.e("IOException,Failed to send message.", "send exception", ex);
//            Object o = null;
//            return (byte[])o;
//        } finally {
//            if(httpURLConnection != null) {
//                httpURLConnection.disconnect();
//            }
//
//        }
//
//        return result;
//    }

}
