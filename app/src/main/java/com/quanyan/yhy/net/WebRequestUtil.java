package com.quanyan.yhy.net;


import com.lidroid.xutils.util.LogUtils;
import com.quanyan.base.util.HarwkinLogUtil;
import com.quanyan.yhy.util.SSLContextUtil;
import com.smart.sdk.client.LocalException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.KeyStore;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class WebRequestUtil {
	private static final int SET_SOCKET_TIMEOUT = 15 * 1000;
	private static final int CONNECTION_TIMEOUT = 15 * 1000;

    private static HttpClient getHttpClient() {
    	try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new SSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();

			HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params,SET_SOCKET_TIMEOUT );

			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			HttpClient client = new DefaultHttpClient(params);
			return client;
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
    }

    public static void fillResponse(String baseUrl, String params, String cid, boolean useGzip, ResponseFiller f) {
        HttpResponse resp = null;
        InputStream is = null;
        try {
            resp = getHttpResponse(baseUrl, params, cid, useGzip);
            if(resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK){            	 
            	 HttpEntity httpEntity = resp.getEntity();
                 is = httpEntity.getContent();
                 Header contentType = httpEntity.getContentEncoding();
                 if (contentType != null) {
                     String value = contentType.getValue();
                     if (value != null && value.contains("gzip")) {
                    	 is = new GZIPInputStream(is);
                     }
                 }
                 f.fill(is);
            }else{
            	throw new LocalException(resp.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            HarwkinLogUtil.info("end request : " + e.getMessage());
            if(e instanceof SocketTimeoutException){
                HarwkinLogUtil.info("end request :  SocketTimeoutException");
                throw new LocalException(HttpStatus.SC_GATEWAY_TIMEOUT);
            }
        	
        	if(e instanceof ConnectionPoolTimeoutException){
                HarwkinLogUtil.info("end request :  ConnectionPoolTimeoutException");
                throw new LocalException(HttpStatus.SC_GATEWAY_TIMEOUT);
        	}
        	
        	if(e instanceof ConnectTimeoutException){
                HarwkinLogUtil.info("end request :  ConnectTimeoutException");
                throw new LocalException(HttpStatus.SC_GATEWAY_TIMEOUT);
        	}
        	
        	e.printStackTrace();
            throw new RuntimeException(cid + ",1 " + baseUrl + "?" + params, e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                	e.printStackTrace();
                    throw new RuntimeException(cid + ",2 " + baseUrl + "?" + params, e);
                }
            }
        }
    }

    private static HttpResponse getHttpResponse(String baseUrl, String params, String cid, boolean useGzip) throws ClientProtocolException,
            IOException {
        HttpClient client = getHttpClient();
        HttpRequestBase req = null;
        if (params == null) {
            req = new HttpGet(baseUrl);
        } else if (params.length() > 200) {
            HttpPost post = new HttpPost(baseUrl);
            StringEntity se = new StringEntity(params, "utf-8");
            se.setContentType("application/x-www-form-urlencoded;charset=UTF-8");
            post.setEntity(se);
            req = post;
        } else {
            req = new HttpGet(baseUrl + "?" + params);
        }
        if(!baseUrl.contains("user.imLogin")) {
            HarwkinLogUtil.info("com.smart.sdk.client.ApiContext", "----before request url = " + baseUrl + "?" + params);
        }
        if (useGzip) {
            req.setHeader("Accept-Encoding", "gzip");
        }
        HttpResponse resp = null;
        resp = client.execute(req);
        int statusCode = resp.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            HarwkinLogUtil.info("com.smart.sdk.client.ApiContext", "----failed----");
            throw new LocalException(statusCode);
        }
        HarwkinLogUtil.info("com.smart.sdk.client.ApiContext", "----after-----");
        return resp;
    }

    /**
     * Https链接
     * @param baseUrl
     * @param params
     * @param cid
     * @param useGzip
     * @param f
     */
    public static void fillHttpsResponse(String baseUrl, String params, String cid, boolean useGzip, ResponseFiller f) {
        if(!baseUrl.contains("user.imLogin")) {
            HarwkinLogUtil.info("com.smart.sdk.client.ApiContext", "----before request url = " + baseUrl + "?" + params);
        }
        HttpsURLConnection urlConnection = null;
        try {
            URL url;
            if (params.length() > 200){
                url = new URL(baseUrl);
            }else{
                url = new URL(baseUrl + "?" + params);
            }
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(10 * 1000);
            urlConnection.setReadTimeout(20 * 1000);
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            if (urlConnection instanceof HttpsURLConnection) { // 是Https请求
                SSLContext sslContext = SSLContextUtil.getSLLContext();
                if (sslContext != null) {
                    javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                    urlConnection.setSSLSocketFactory(sslSocketFactory);
                }
//            urlConnection.setHostnameVerifier(hostnameVerifier);
            }
            if (params.length() > 200) {
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                urlConnection.setRequestProperty("Content-Length", String.valueOf(params.length()));
                urlConnection.setRequestMethod("POST");
                urlConnection.setUseCaches(false);
                //把封装好的实体数据发送到输出流
                OutputStream outStream = urlConnection.getOutputStream();
                outStream.write(params.getBytes());
                outStream.flush();
                outStream.close();
//            return urlConnection.getInputStream();
            } else {
                urlConnection.setRequestMethod("GET");
            }

            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            LogUtils.v("the response code -->> " + responseCode);
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                HarwkinLogUtil.info("com.smart.sdk.client.ApiContext", "----failed----");
                throw new LocalException(responseCode);
            }
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            f.fill(in);
        } catch (Exception e) {
            if(e instanceof SocketTimeoutException){
                throw new LocalException(HttpStatus.SC_GATEWAY_TIMEOUT);
            }

            if(e instanceof ConnectionPoolTimeoutException){
                throw new LocalException(HttpStatus.SC_GATEWAY_TIMEOUT);
            }

            if(e instanceof ConnectTimeoutException){
                throw new LocalException(HttpStatus.SC_GATEWAY_TIMEOUT);
            }

            e.printStackTrace();
            throw new RuntimeException(cid + ",1 " + baseUrl + "?" + params, e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

//    private static HostnameVerifier hostnameVerifier = new HostnameVerifier() {
//        @Override
//        public boolean verify(String hostname, SSLSession session) {
//            HostnameVerifier hv =
//                    HttpsURLConnection.getDefaultHostnameVerifier();
//            return hv.verify("example.com", session);
//        }
//    };

    /**
     * Https链接
     * @param baseUrl
     * @param params
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    private static InputStream getHttpsResponse(String baseUrl, String params) throws Exception {
        if(!baseUrl.contains("user.imLogin")) {
            HarwkinLogUtil.info("com.smart.sdk.client.ApiContext", "----before request url = " + baseUrl + "?" + params);
        }
        URL url;
        if (params.length() > 200){
            url = new URL(baseUrl);
        }else{
            url = new URL(baseUrl + "?" + params);
        }
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(20 * 1000);
        urlConnection.setReadTimeout(20 * 1000);
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        if (urlConnection instanceof HttpsURLConnection) { // 是Https请求
            SSLContext sslContext = SSLContextUtil.getSLLContext();
            if (sslContext != null) {
                javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                urlConnection.setSSLSocketFactory(sslSocketFactory);
            }
//            urlConnection.setHostnameVerifier(hostnameVerifier);
        }
        if (params.length() > 200) {
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            urlConnection.setRequestProperty("Content-Length", String.valueOf(params.length()));
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            //把封装好的实体数据发送到输出流
            OutputStream outStream = urlConnection.getOutputStream();
            outStream.write(params.getBytes());
            outStream.flush();
            outStream.close();
//            return urlConnection.getInputStream();
        } else {
            urlConnection.setRequestMethod("GET");
        }

        urlConnection.connect();
        int responseCode = urlConnection.getResponseCode();
        LogUtils.v("the response code -->> " + responseCode);
        if (responseCode != HttpsURLConnection.HTTP_OK) {
            HarwkinLogUtil.info("com.smart.sdk.client.ApiContext", "----failed----");
            throw new LocalException(responseCode);
        }
        return urlConnection.getInputStream();
    }

    public static interface ResponseFiller {
        void fill(InputStream is);
    }
}
