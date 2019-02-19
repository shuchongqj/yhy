package com.quanyan.yhy.util;

import java.security.SecureRandom;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created with Android Studio.
 * Title:SSLContextUtil
 * Description:
 * Copyright:Copyright (c) 2016
 * Company:quanyan
 * Author:徐学东
 * Date:16/8/11
 * Time:下午7:14
 * Version 1.1.0
 */
public class SSLContextUtil {
    /**
     * 获取ssl
     * @return
     */
    public static SSLContext getSLLContext () {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {

                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }


            }}, new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslContext;
    }
}
