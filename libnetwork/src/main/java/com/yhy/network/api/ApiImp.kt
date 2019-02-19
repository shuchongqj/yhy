package com.yhy.network.api

import com.parkingwang.okhttp3.LogInterceptor.LogInterceptor
import com.yhy.common.base.YHYBaseApplication
import com.yhy.network.YhyCallback
import com.yhy.network.req.BaseRequest
import com.yhy.network.YhyCaller
import com.yhy.network.req.BaseLogRequest
import com.yhy.network.req.device.RegisterReq
import com.yhy.network.req.log.LogUploadReq
import com.yhy.network.resp.Response
import com.yhy.network.resp.device.RegisterResp
import com.yhy.network.utils.*
import okhttp3.*
import okhttp3.internal.http.BridgeInterceptor
import okhttp3.internal.http.HttpMethod
import java.lang.ref.SoftReference
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

class ApiImp : Api {


    companion object {
        val client : OkHttpClient = OkHttpClient.Builder()
                .retryOnConnectionFailure(true)//默认重试一次，若需要重试N次，则要实现拦截器。
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(BridgeInterceptor(CookieJar.NO_COOKIES)).apply {
                    if (!YHYBaseApplication.getInstance().yhyEnvironment.isOnline){
                        addNetworkInterceptor(LogInterceptor())
                    }
                }
                .build()

        val logClient : OkHttpClient = OkHttpClient.Builder()
                .retryOnConnectionFailure(true)//默认重试一次，若需要重试N次，则要实现拦截器。
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(BridgeInterceptor(CookieJar.NO_COOKIES))
                .addInterceptor{
                    val body = it.request().body() as YhyLogFormBody

                    it.proceed(it.request().newBuilder().apply {
                        get()
                        url("${it.request().url().url()}?${body.makeParam()}")
                    }.build())
                }
                .apply {
                    if (!YHYBaseApplication.getInstance().yhyEnvironment.isOnline){
                        addNetworkInterceptor(LogInterceptor())
                    }
                }.dispatcher(Dispatcher(Executors.newSingleThreadExecutor()).apply {
                    maxRequests = 1
                    maxRequestsPerHost = 1
                })
                .build()

    }


    override fun register(req : RegisterReq, callback : YhyCallback<Response<RegisterResp>>?) : YhyCaller<Response<RegisterResp>, RegisterResp> {
        return makeRequest(req, callback, RegisterResp::class.java)
    }

    override fun <T> makeLogRequest(request: LogUploadReq, callback : YhyCallback<Response<T>>?, respClass : Class<T>): YhyCaller<Response<T>, T> {
        //创建OkHttpClient对象

        val r = Request.Builder()
                .url(apiLogUrl) //请求接口。如果需要传参拼接到接口后面。
                .post(YhyLogFormBody(request))
                .build()//创建Request 对象

        val call = logClient.newCall(r)
        val caller = YhyCaller<Response<T>, T>(call, respClass, request)
        callback?.apply {
            caller.callback = this
        }

        return caller
    }

    override fun <T> makeRequest(request: BaseRequest, callback : YhyCallback<Response<T>>?, respClass : Class<T>): YhyCaller<Response<T>, T> {
        //创建OkHttpClient对象
        val r = Request.Builder()
                .url(apiUrl)//请求接口。如果需要传参拼接到接口后面。
                .post(YhyFormBody(request))
                .build()//创建Request 对象

        val call = client.newCall(r)
        val caller = YhyCaller<Response<T>, T>(call, respClass, request)
        callback?.apply {
            caller.callback = this
        }

        return caller
    }

}
