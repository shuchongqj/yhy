package com.yhy.network.manager

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.yhy.common.base.YHYBaseApplication
import com.yhy.common.utils.SPUtils
import com.yhy.network.utils.*
import java.security.PrivateKey
import java.security.cert.X509Certificate

class AccountManager {
    var phoneNumber = ""
    var dynamic = ""
    var userId = 0L
    var userToken = ""
    var webToken = ""

    //ACCOUNT备份
    private var cert : X509Certificate? = null
    private var certString : String = ""
    private var pk : PrivateKey? = null

    private var dtk = ""
    private var did = ""
    private var dinfo = ""

    private constructor()
    fun getPrivateKey() : PrivateKey?{
        return if (pk == null){
            DeviceManager.deviceManager.pk
        }else {
            pk
        }
    }

    fun getDeviceId(): String {
        return if (did.isEmpty()){
            DeviceManager.deviceManager.deviceId
        }else {
            did
        }
    }

    fun getDeviceInfo(): String {
        return if (dinfo.isEmpty()){
            DeviceManager.deviceManager.deviceInfo
        }else {
            dinfo
        }
    }

    fun getDeviceToken(): String {
        return if (dtk.isEmpty()){
            DeviceManager.deviceManager.deviceToken
        }else {
            dtk
        }
    }

    fun getCertStr(): String {
        return if (certString.isEmpty()){
            DeviceManager.deviceManager.getCertStr()
        }else {
            certString
        }
    }

    fun getCertificate(): X509Certificate? {
        return if (cert == null){
            DeviceManager.deviceManager.certificate
        }else {
            cert
        }
    }

    fun getSession() : String{
        return DeviceManager.deviceManager.getSession()
    }

    fun init(){
        phoneNumber = getLoginConfig().getString("phone", "")
        dynamic = getLoginConfig().getString("phoneCode", "")
        userToken = getLoginConfig().getString("token", "")
        userId = getLoginConfig().getLong("userId", 0L)
        webToken = getLoginConfig().getString("webToken", "")


        refreshCert()
        initCert(certString)
    }

    fun refreshCert(){
        certString = getLoginConfig().getString("certStr", "")
        dtk = getLoginConfig().getString("dtk", "")
    }

    fun initCert(certStr : String){
        try {
            certString = certStr
            val ks = DeviceManager.parseKeyStore(certStr)
            cert = DeviceManager.parseX509Certificate(ks)
            pk = DeviceManager.parsePrivateKey(ks)
            did = DeviceManager.parseDeviceId(cert!!)
            dinfo = DeviceManager.parseDeviceInfo(cert!!)
            NetManagerProxy.doDeviceRegist(certStr, dtk)
        }catch (e : Exception){
            e.printStackTrace()
        }
    }


    @SuppressLint("ApplySharedPref")
    fun saveCertString(certStr: String) {
        getLoginConfig().edit().putString("certStr", certStr).commit()
    }

    @SuppressLint("ApplySharedPref")
    fun saveDtk(dtk: String) {
        getLoginConfig().edit().putString("dtk", dtk).commit()
    }

    @SuppressLint("ApplySharedPref")
    fun saveLoginToken(token: String) {
        getLoginConfig().edit().putString("token", token).commit()
        userToken = token
    }

    @SuppressLint("ApplySharedPref")
    fun savePhone(phone: String) {
        getLoginConfig().edit().putString("phone", phone).commit()
        phoneNumber = phone
    }

    @SuppressLint("ApplySharedPref")
    fun savePhoneCode(code: String) {
        getLoginConfig().edit().putString("phoneCode", code).commit()
        dynamic = code
    }

    @SuppressLint("ApplySharedPref")
    fun saveUserId(userId: Long) {
        getLoginConfig().edit().putLong("userId", userId).commit()
        this.userId = userId
    }

    @SuppressLint("ApplySharedPref")
    fun saveWebToken(webToken: String) {
        getLoginConfig().edit().putString("webToken", webToken).commit()
        this.webToken = webToken
    }

    private fun getLoginConfig(): SharedPreferences {
        return YHYBaseApplication.getInstance().getSharedPreferences("yhy_user_login", Context.MODE_PRIVATE)
    }

    companion object {
        val accountManager = AccountManager()
    }

    fun logout(){
        SPUtils.clearLogStatus(YHYBaseApplication.getInstance())
        saveUserId(0)
        savePhone("")
        saveLoginToken("")
        saveWebToken("")
        savePhoneCode("")
        saveDtk("")
        saveCertString("")

        cert = null
        pk = null
        certString = ""
        dtk = ""
        did = ""
        dinfo = ""

        NetManagerProxy.doDeviceRegist(DeviceManager.deviceManager.getCertStr(), DeviceManager.deviceManager.deviceToken)
    }
}