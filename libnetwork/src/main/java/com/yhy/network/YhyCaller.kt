package com.yhy.network

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import com.yhy.common.base.YHYBaseApplication
import com.yhy.common.beans.net.model.CreditNotification
import com.yhy.common.constants.ValueConstants
import com.yhy.common.utils.JSONUtils
import com.yhy.common.utils.SPUtils
import com.yhy.network.exceptions.HttpResponseCodeException
import com.yhy.network.manager.DeviceManager
import com.yhy.network.req.BaseRequest
import com.yhy.network.resp.Response
import com.yhy.network.resp.log.LogUploadResp
import com.yhy.network.utils.*
import okhttp3.Call
import okhttp3.Callback
import java.io.IOException
import java.lang.ref.SoftReference
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class YhyCaller<T, F>(private var call: Call, private var respClass : Class<F>, private var req : BaseRequest) {

    @Volatile
    private var isCancel = false
    var callback: YhyCallback<T>? = null

    /** Cancels the request, if possible. Requests that are already complete cannot be canceled. */
    fun cancel() {
        synchronized(this) {
            if (isCancel) {
                return
            }
            isCancel = true
            pendingRegisterDeviceRequestList.remove(this@YhyCaller)
        }
        call.cancel()

    }


    fun isCancel(): Boolean {
        synchronized(this) {
            return isCancel
        }
    }

    fun execAsync() : YhyCaller<T, F>{
        return execAsync(true)
    }

    fun execLogAsync() : YhyCaller<T, F>{
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                pendingRegisterDeviceRequestList.remove(this@YhyCaller)
                uiHandler.post {
                    try {
                        callback?.onFail(YhyCode.NETWORK_ERROR, e)
                    }catch (e : Exception){
                        e.printStackTrace()
                    }
                }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: okhttp3.Response) {
                pendingRegisterDeviceRequestList.remove(this@YhyCaller)
                if (response.isSuccessful) {
                    uiHandler.post {
                        val resp = Response<F>()
                        try {
                            callback?.onSuccess(resp as T)
                        }catch (e : Exception){
                            e.printStackTrace()
                        }
                    }
                } else {
                    uiHandler.post {
                        try {
                            callback?.onFail(YhyCode.NETWORK_ERROR, HttpResponseCodeException(response.code()))
                        }catch (e : Exception){
                            e.printStackTrace()
                        }
                    }
                }
            }
        })
        return this
    }

    fun execAsync(check : Boolean = true) : YhyCaller<T, F>{
        if (check){
            if (!checkSecurity(this, req)){
                return this
            }
        }
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                pendingRegisterDeviceRequestList.remove(this@YhyCaller)
                uiHandler.post {
                    try {
                        callback?.onFail(YhyCode.NETWORK_ERROR, e)
                    }catch (e : Exception){
                        e.printStackTrace()
                    }
                }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: okhttp3.Response) {
                pendingRegisterDeviceRequestList.remove(this@YhyCaller)
                if (response.isSuccessful) {
                    val resp = Response<F>()

                    try {
                        val str = response.body()?.string()

                        val t = object : TypeToken<Map<String, Any>>(){}.type

                        val map = gson.fromJson<Map<String, Any>>(str, t)

                        val stat = map["stat"] as Map<*, *>

                        var code = YhyCode.parseCode(stat["code"].toString().toDouble().toLong())
                        if (code != YhyCode.SUCCESS){
                            when(code){
                                YhyCode.NO_TRUSTED_DEVICE, YhyCode.NO_ACTIVE_DEVICE -> {
//                                    NotificationCenter.default.post(name: NSNotification.Name(rawValue:UTravConfig.Notification.notActiveDevice), object: nil)
                                }
                                YhyCode.TOKEN_EXPIRE -> {
//                                    NotificationCenter.default.post(name: NSNotification.Name(rawValue:UTravConfig.Notification.userTokenExpire), object: nil)
                                }
                                YhyCode.TOKEN_ERROR -> {
//                                    NotificationCenter.default.post(name: NSNotification.Name(rawValue:UTravConfig.Notification.deviceTokenError), object: nil)
                                }
                                YhyCode.SIGNATURE_ERROR -> {
//                                    NotificationCenter.default.post(name: NSNotification.Name(rawValue:UTravConfig.Notification.signDeviceError), object: nil)
                                }else -> {

                                }
                            }
                            uiHandler.post {
                                try {
                                    callback?.onFail(code!!, null)
                                }catch (e : Exception){
                                    e.printStackTrace()
                                }
                            }
                            return
                        }

                        val session = stat["session"]?.toString()
                        if (!session.isNullOrEmpty()){
                            DeviceManager.deviceManager.saveSession(session!!)
                        }
//                        val n = "[{\"key\": \"CREDIT\",\"value\":[{\"credit\":5,\"description\":\"圈子内容分享\",\"notification\":\"奖励5积分\"}]}]"
                        //JSONUtils.convertToArrayList(n, Map::class.java)//
                        val notificationList = stat["notificationList"] as List<*>?
                        notificationList?.forEach {
                            try {

                                val m = it as Map<*, *>
                                if (m["key"] != "CREDIT"){
                                    return@forEach
                                }
                                val value = JSONUtils.convertToArrayList(m["value"] as String, Map::class.java)

                                value.forEach {

                                    try {
                                        val intent = Intent(ValueConstants.BROADCASTRECEIVER_ALL_TAST_COMPLETE)
                                        val creditNotification = CreditNotification.deserialize(JSONUtils.toJson(it))
                                        intent.putExtra(SPUtils.EXTRA_DATA, creditNotification)
                                        var score = SPUtils.getScore(YHYBaseApplication.getInstance())
                                        score += creditNotification.credit
                                        SPUtils.setScore(YHYBaseApplication.getInstance(), score)
                                        object : Handler(Looper.getMainLooper()) {
                                            override fun handleMessage(msg: Message) {
                                                when (msg.what) {
                                                    0 -> YHYBaseApplication.getInstance().sendBroadcast(intent)
                                                }
                                            }
                                        }.sendEmptyMessageDelayed(0, (1 * 1000).toLong())
                                    }catch (e : Exception){
                                        e.printStackTrace()
                                    }
                                }


                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        
                        val stateList = stat["stateList"] as List<Map<*, *>>

                        code = YhyCode.parseCode(stateList.first()["code"].toString().toDouble().toLong())
                        if (code != YhyCode.SUCCESS){
                            uiHandler.post {
                                try {
                                    callback?.onFail(code!!, null)
                                }catch (e : Exception){
                                    e.printStackTrace()
                                }
                            }
                            return
                        }
                        val content = map["content"] as ArrayList<*>

//                        val objectType = type(Response::class.java, respClass)

                        resp.content = gson.fromJson(gson.toJson(content.first()), respClass)
                        uiHandler.post {
                            try {
                                callback?.onSuccess(resp as T)
                            }catch (e : Exception){
                                e.printStackTrace()
                            }
                        }

                    }catch (e : Exception){
                        e.printStackTrace()
                        uiHandler.post {
                            try {
                                callback?.onFail(YhyCode.NETWORK_ERROR, e)
                            }catch (e : Exception){
                                e.printStackTrace()
                            }
                        }

                        return
                    }
                } else {
                    uiHandler.post {
                        try {
                            callback?.onFail(YhyCode.NETWORK_ERROR, HttpResponseCodeException(response.code()))
                        }catch (e : Exception){
                            e.printStackTrace()
                        }
                    }
                }
            }
        })
        return this
    }

    fun type(raw: Class<*>, vararg args: Type): ParameterizedType {
        return object : ParameterizedType {
            override fun getRawType(): Type {
                return raw
            }

            override fun getActualTypeArguments(): Array<Type> {
                return args as Array<Type>
            }

            override fun getOwnerType(): Type? {
                return null
            }
        }
    }
}