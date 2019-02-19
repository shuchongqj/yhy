package com.yhy.network.utils

import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import com.smart.sdk.client.util.Base64Util
import com.yhy.common.base.YHYBaseApplication
import com.yhy.common.utils.JSONUtils
import com.yhy.common.utils.SPUtils
import com.yhy.network.annotations.Param
import com.yhy.network.annotations.Req
import com.yhy.network.api.Api
import com.yhy.network.api.ApiImp
import com.yhy.network.api.user.UserApi
import com.yhy.network.manager.AccountManager
import com.yhy.network.manager.DeviceManager
import com.yhy.network.req.BaseLogRequest
import com.yhy.network.req.BaseRequest
import java.security.MessageDigest
import java.security.PrivateKey
import java.security.Signature
import java.security.cert.X509Certificate
import java.util.*

private val types = arrayOf("java.lang.Integer", "java.lang.Double", "java.lang.Float", "java.lang.Long", "java.lang.Short", "java.lang.Byte", "java.lang.Boolean", "java.lang.Character", "java.lang.String", "int", "double", "long", "short", "byte", "boolean", "char", "float")

val uiHandler = Handler(Looper.getMainLooper())

var api : Api = ApiImp()
val gson = Gson()

var appId = 1
var channel = ""
var versionCode = 0
var domaindId = 1000
var rsaPublicKey = ""
var apiUrl = ""
var apiLogUrl = ""


private fun signRequest(params: ParameterList, securityType: Int) {
    val accountManager = AccountManager.accountManager
    if (params.containsKey("_sig")) {
        return
    }
    try {
        accountManager.getPrivateKey()?.apply {
            if (securityType > 0) {
                if ("EC".equals(algorithm, ignoreCase = true)) {
                    if (!params.containsKey("_sm")) {
                        params.put("_sm", "ecc")
                    }
                } else if ("RSA".equals(algorithm, ignoreCase = true)) {
                    if (!params.containsKey("_sm")) {
                        params.put("_sm", "rsa")
                    }
                } else if (!params.containsKey("_sm")) {
                    params.put("_sm", "ecc")
                }
            }
        }


        val sb = StringBuilder(params.size() * 5)
        val paramNames = ArrayList(params.keySet())
        paramNames.sort()

        paramNames.forEach {
            sb.append(it)
            sb.append('=')
            sb.append(params.get(it))
        }

        if (securityType == SecurityType.None) {
            sb.append("0ce37dd6b927730161a1e559c2336d0a")
            val sha = MessageDigest.getInstance("SHA1")
            params.put("_sig", String(Base64Util.encode(sha.digest(sb.toString().toByteArray(Charsets.UTF_8)), Base64Util.NO_WRAP), Charsets.UTF_8))
        } else {
            if (accountManager.getCertificate() == null) {
                return
            }
            if (accountManager.getPrivateKey() == null) {
                return
            }
            val bs = sb.toString().toByteArray(charset("utf-8"))
            val sig: Signature = when {
                "EC".equals(accountManager.getPrivateKey()?.algorithm, ignoreCase = true) -> Signature.getInstance("SHA1withECDSA")
                "RSA".equals(accountManager.getPrivateKey()?.algorithm, ignoreCase = true) -> Signature.getInstance("SHA1withRSA")
                else -> Signature.getInstance("SHA1withECDSA")
            }

            sig.initSign(accountManager.getPrivateKey())
            sig.update(bs)
            val s = sig.sign()
            val signature = String(Base64Util.encode(s, Base64Util.NO_WRAP), Charsets.UTF_8)
            params.put("_sig", signature)
        }

    } catch (var11: Exception) {
        throw RuntimeException("sign url failed.", var11)
    }
}

fun BaseLogRequest.signLog() : ParameterList{

    val params = ParameterList(2)

    BaseRequest::class.java.declaredFields.forEach {

        if (it.name == "serialVersionUID"){
            return@forEach
        }
        if (it.name == "securityType"){
            return@forEach
        }
        it.isAccessible = true
        val v = it.get(this)
        val paramAnnotation = it.getAnnotation(Param::class.java)
        v?.apply {
            if (paramAnnotation == null){
                if (types.indexOf(it.type.name) == -1){
                    params.put(it.name, JSONUtils.toJson(this))
                }else {
                    params.put(it.name, toString())
                }

            }else {
                if (!paramAnnotation.ignore){
                    if (types.indexOf(it.type.name) == -1){
                        params.put(paramAnnotation.name, JSONUtils.toJson(this))
                    }else {
                        params.put(paramAnnotation.name, toString())
                    }
                }

            }
        }
    }
    BaseLogRequest::class.java.declaredFields.forEach {

        if (it.name == "serialVersionUID"){
            return@forEach
        }
        if (it.name == "securityType"){
            return@forEach
        }
        it.isAccessible = true
        val v = it.get(this)
        val paramAnnotation = it.getAnnotation(Param::class.java)
        v?.apply {
            if (paramAnnotation == null){
                if (types.indexOf(it.type.name) == -1){
                    params.put(it.name, JSONUtils.toJson(this))
                }else {
                    params.put(it.name, toString())
                }

            }else {
                if (!paramAnnotation.ignore){
                    if (types.indexOf(it.type.name) == -1){
                        params.put(paramAnnotation.name, JSONUtils.toJson(this))
                    }else {
                        params.put(paramAnnotation.name, toString())
                    }
                }

            }
        }
    }
    this::class.java.declaredFields.forEach {
        if (it.name == "serialVersionUID"){
            return@forEach
        }
        it.isAccessible = true
        val v = it.get(this)
        val paramAnnotation = it.getAnnotation(Param::class.java)
        v?.apply {
            if (paramAnnotation == null){
                if (types.indexOf(it.type.name) == -1){
                    params.put(it.name, JSONUtils.toJson(this))
                }else {
                    params.put(it.name, toString())
                }

            }else {
                if (!paramAnnotation.ignore){
                    if (types.indexOf(it.type.name) == -1){
                        params.put(paramAnnotation.name, JSONUtils.toJson(this))
                    }else {
                        params.put(paramAnnotation.name, toString())
                    }
                }

            }
        }
    }
    return params
}

fun BaseRequest.sign() : ParameterList{
    val accountManager = AccountManager.accountManager
    val reqAnnotation = javaClass.getAnnotation(Req::class.java)
    _mt = reqAnnotation.name

    val params = ParameterList(2)

    BaseRequest::class.java.declaredFields.forEach {

        if (it.name == "serialVersionUID"){
            return@forEach
        }
        if (it.name == "securityType"){
            return@forEach
        }
        it.isAccessible = true
        val v = it.get(this)
        val paramAnnotation = it.getAnnotation(Param::class.java)
        v?.apply {
            if (paramAnnotation == null){
                if (types.indexOf(it.type.name) == -1){
                    params.put(it.name, JSONUtils.toJson(this))
                }else {
                    params.put(it.name, toString())
                }

            }else {
                if (!paramAnnotation.ignore){
                    if (types.indexOf(it.type.name) == -1){
                        params.put(paramAnnotation.name, JSONUtils.toJson(this))
                    }else {
                        params.put(paramAnnotation.name, toString())
                    }
                }

            }
        }
    }
    this::class.java.declaredFields.forEach {
        if (it.name == "serialVersionUID"){
            return@forEach
        }
        it.isAccessible = true
        val v = it.get(this)
        val paramAnnotation = it.getAnnotation(Param::class.java)
        v?.apply {
            if (paramAnnotation == null){
                if (types.indexOf(it.type.name) == -1){
                    params.put(it.name, JSONUtils.toJson(this))
                }else {
                    params.put(it.name, toString())
                }

            }else {
                if (!paramAnnotation.ignore){
                    if (types.indexOf(it.type.name) == -1){
                        params.put(paramAnnotation.name, JSONUtils.toJson(this))
                    }else {
                        params.put(paramAnnotation.name, toString())
                    }
                }

            }
        }
    }
    accountManager.getPrivateKey()?.apply {
        if (securityType > 0) {
            if ("EC".equals(algorithm, ignoreCase = true)) {
                if (!params.containsKey("_sm")) {
                    params.put("_sm", "ecc")
                }
            } else if ("RSA".equals(algorithm, ignoreCase = true)) {
                if (!params.containsKey("_sm")) {
                    params.put("_sm", "rsa")
                }
            } else if (!params.containsKey("_sm")) {
                params.put("_sm", "ecc")
            }
        }
    }
    if ((securityType and SecurityType.MobileOwner > SecurityType.None) or (securityType and SecurityType.MobileOwnerTrustedDevice > 0)) {
        params.put("_pn", accountManager.phoneNumber)
        params.put("_dyn", accountManager.dynamic)
    }
    signRequest(params, securityType)
    return params
}

fun init(){
    val deviceManager = DeviceManager.deviceManager
    val accountManager = AccountManager.accountManager

    deviceManager.initCert(deviceManager.getCertStr())



    accountManager.init()

    if (accountManager.userToken.isEmpty()){

        AccountManager.accountManager.logout()
//        if (!deviceManager.deviceToken.isEmpty() && !accountManager.getCertStr().isEmpty()){
//            System.err.println("test========doDeviceRegist3 ${deviceManager.deviceId}")
//            NetManagerProxy.doDeviceRegist(deviceManager.getCertStr(), deviceManager.deviceToken)
//        }
    }
//    else {
//        if (!accountManager.getDeviceToken().isEmpty() && !accountManager.getCertStr().isEmpty()){
//            System.err.println("test========doDeviceRegist4 ${accountManager.getDeviceId()}")
//            TempLog.log("doDeviceRegist4 ${accountManager.getDeviceId()}")
//            NetManagerProxy.doDeviceRegist(accountManager.getCertStr(), accountManager.getDeviceToken())
//        }
//    }
}



