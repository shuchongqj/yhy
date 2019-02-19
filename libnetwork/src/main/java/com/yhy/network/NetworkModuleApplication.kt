package com.yhy.network

import android.content.pm.PackageManager
import com.yhy.common.base.ModuleApplication
import com.yhy.common.base.YHYBaseApplication
import com.yhy.common.utils.AndroidUtils
import com.yhy.network.api.ApiImp
import com.yhy.network.api.log.LogApi
import com.yhy.network.req.device.RegisterReq
import com.yhy.network.req.log.LogUploadReq
import com.yhy.network.resp.Response
import com.yhy.network.resp.device.RegisterResp
import com.yhy.network.resp.log.LogUploadResp
import com.yhy.network.utils.*

class NetworkModuleApplication : ModuleApplication {


    override fun onCreate(application: YHYBaseApplication) {

        try {
            val appInfo = application.packageManager.getApplicationInfo(application.packageName, PackageManager.GET_META_DATA)
            rsaPublicKey = appInfo.metaData.get("WEB_API_PUBLIC_KEY")!!.toString()
            apiUrl = appInfo.metaData.get("WEB_API_URL")!!.toString()
            apiLogUrl = appInfo.metaData.get("WEB_LOG_API_URL")!!.toString()
            versionCode = AndroidUtils.getVerCode(application)
            appId = 1
            channel = application.yhyEnvironment.channel
            domaindId = 1000
        } catch (e: Exception) {
            e.printStackTrace()
        }

        init()
        checkSecurity(null, null)

//        LogApi().uploadLog(LogUploadReq(System.currentTimeMillis(), "", "", arrayListOf()), object : YhyCallback<Response<LogUploadResp>>{
//            override fun onSuccess(data: Response<LogUploadResp>) {
//            }
//
//            override fun onFail(code: YhyCode, exception: Exception?) {
//            }
//        }).execAsync()
    }

}