package com.yhy.network.manager

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Environment
import android.support.v4.app.ActivityCompat
import com.smart.sdk.client.util.Base64Util
import com.yhy.common.base.YHYBaseApplication
import com.yhy.common.cache.ACache
import java.io.ByteArrayInputStream
import java.io.File
import java.security.KeyStore
import java.security.PrivateKey
import java.security.cert.X509Certificate

class DeviceManager {

    private constructor()
    var certificate : X509Certificate? = null
    var pk : PrivateKey? = null

    var deviceToken = ""
    var deviceId = ""
    var deviceInfo = ""

    fun saveDeviceInfoFromNet(certStr : String, dtk : String){
        saveDeviceToken(dtk)
        saveCertificate(certStr)

        initCert(certStr)

        saveDeviceId(deviceId)
        saveDeviceInfo(deviceInfo)
    }

    fun getCertStr() : String{
        val certStr = getDeviceTokenConfig().getAsString("certificate")
        if (certStr.isNullOrEmpty()){
            return ""
        }
        return certStr
    }

    fun getSession() : String{
        val certStr = getDeviceTokenConfig().getAsString("session")
        if (certStr.isNullOrEmpty()){
            return ""
        }
        return certStr
    }

    fun initCert(certStr : String){
        try {
            val ks = parseKeyStore(certStr)
            certificate = parseX509Certificate(ks)
            pk = parsePrivateKey(ks)
            deviceId = parseDeviceId(certificate!!)
            deviceInfo = parseDeviceInfo(certificate!!)
            deviceToken = getDeviceTokenConfig().getAsString("deviceToken")
        }catch (e : Exception){
            e.printStackTrace()
        }
    }



    @SuppressLint("ApplySharedPref")
    private fun saveDeviceToken(deviceToken : String){
        val sp = getDeviceTokenConfig()
        sp.put("deviceToken", deviceToken)
    }

    @SuppressLint("ApplySharedPref")
    private fun saveCertificate(certStr : String){
        val sp = getDeviceTokenConfig()
        sp.put("certificate", certStr)
    }

    @SuppressLint("ApplySharedPref")
    fun saveSession(session : String){
        val sp = getDeviceTokenConfig()
        sp.put("session", session)
    }

    @SuppressLint("ApplySharedPref")
    private fun saveDeviceId(deviceId : String){
        val sp = getDeviceTokenConfig()
        sp.put("deviceId", deviceId)
    }

    @SuppressLint("ApplySharedPref")
    private fun saveDeviceInfo(deviceInfo : String){
        val sp = getDeviceTokenConfig()
        sp.put("deviceInfo", deviceInfo)
    }


    private fun getDeviceTokenConfig() : ACache {
        val sdCardExist = Environment.getExternalStorageState() == android.os.Environment.MEDIA_MOUNTED //判断sd卡是否存在
        return if(sdCardExist && verifyStoragePermissions()) {
            val dir = File("${Environment.getExternalStorageDirectory().absolutePath}/${YHYBaseApplication.getInstance().packageName}/config/device_${YHYBaseApplication.getInstance().yhyEnvironment.envType}").apply {
                mkdirs()
            }
            when {
                dir.exists() && dir.isDirectory -> ACache.get(dir)//获取跟目录
                else -> ACache.get(YHYBaseApplication.getInstance(), "device_${YHYBaseApplication.getInstance().yhyEnvironment.envType}")
            }
        }else {
            ACache.get(YHYBaseApplication.getInstance(), "device_${YHYBaseApplication.getInstance().yhyEnvironment.envType}")
        }
    }

    private fun verifyStoragePermissions() : Boolean {
        //检查应用程序是否有权写入设备存储
        val permission = ActivityCompat.checkSelfPermission(YHYBaseApplication.getInstance(), Manifest.permission.WRITE_EXTERNAL_STORAGE)

        return permission == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        val deviceManager = DeviceManager()

        fun parseKeyStore(certStr : String) : KeyStore{
            val ks = KeyStore.getInstance("PKCS12")
            val fis = ByteArrayInputStream(Base64Util.decode(certStr))
            ks.load(fis, "sport2018".toCharArray())
            return ks
        }

        fun parseX509Certificate(keyStore : KeyStore) : X509Certificate{
            return keyStore.getCertificate(keyStore.aliases().nextElement() as String) as X509Certificate
        }

        fun parsePrivateKey(keyStore : KeyStore) : PrivateKey{
            return keyStore.getKey(keyStore.aliases().nextElement() as String, "sport2018".toCharArray()) as PrivateKey
        }

        fun parseDeviceId(certificate: X509Certificate) : String{
            val subject = certificate.subjectDN.name
            val cnStart = subject.indexOf("CN=")
            val cnEnd = subject.indexOf(",", cnStart)
            return if (cnEnd != -1) {
                subject.substring(cnStart.plus(3), cnEnd)
            } else {
                subject.substring(cnStart.plus(3))
            }
        }

        fun parseDeviceInfo(certificate: X509Certificate) : String{
            val subject = certificate.subjectDN.name
            val diStart = subject.indexOf("1.2.4.14.4.8.7.21.2=")
            val diEnd = subject.indexOf(",", diStart)
            return if (diEnd != -1) {
                subject.substring(diStart + 20, diEnd)
            } else {
                subject.substring(diStart + 20)
            }
        }
    }
}