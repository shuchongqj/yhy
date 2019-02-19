package com.mogujie.tt.imservice.network;

import android.content.Context;

import com.quanyan.yhy.R;

import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created with Android Studio.
 * Title:SslContextFactory
 * Description:
 * Copyright:Copyright (c) 2015
 * Company:quanyan
 * Author:邓明佳
 * Date:2016/3/17
 * Time:17:59
 * Version 1.0
 */
public class SslContextFactory {
    private String PROTOCOL = "TLS";
    private SSLContext CLIENT_CONTEXT;
    private static final String TRUST_PWD = "jiuxiu";
    private static SslContextFactory instance = new SslContextFactory();
    private Context context;

    public static SslContextFactory getInstance() {
        return instance;
    }

    public void init(Context context) {
        if (CLIENT_CONTEXT != null) return;
        SSLContext clientContext;
        try {
            // keystore
//            KeyStore ks = KeyStore.getInstance("JKS");
//            ks.load(SslContextFactory.class.getClassLoader().getResourceAsStream("cert\\client.keystore"), keyStorePassword.toCharArray());

            // Set up key manager factory to use our key store
//            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//            kmf.init(ks, keyStorePassword.toCharArray());

            // truststore
            KeyStore ts = KeyStore.getInstance("BKS");
//            ts.load(context.getResources().openRawResource(R.raw.quanyan), TRUST_PWD.toCharArray());
            ts.load(context.getResources().openRawResource(R.raw.jiuxiu), TRUST_PWD.toCharArray());

            // set up trust manager factory to use our trust store
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ts);
            clientContext = SSLContext.getInstance(PROTOCOL);
            clientContext.init(null, tmf.getTrustManagers(), null);


        } catch (Exception e) {
            throw new Error("Failed to initialize the client-side SSLContext", e);
        }
        CLIENT_CONTEXT = clientContext;
    }

    public SSLContext getClientContext() {
        return CLIENT_CONTEXT;
    }

}