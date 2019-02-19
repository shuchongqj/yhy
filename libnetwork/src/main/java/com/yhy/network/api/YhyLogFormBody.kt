package com.yhy.network.api

import android.os.Build
import android.util.Base64
import com.yhy.common.base.YHYBaseApplication
import com.yhy.common.utils.JSONUtils
import com.yhy.network.req.BaseLogRequest
import com.yhy.network.req.BaseRequest
import com.yhy.network.req.log.LogUploadReq
import com.yhy.network.utils.*
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink

class YhyLogFormBody(private var request: LogUploadReq) : RequestBody() {
    override fun contentType(): MediaType? {
        return MediaType.parse("")
    }

    override fun writeTo(sink: BufferedSink?) {
    }

    fun makeParam(): String {
        request._aid = appId.toString()
        request._ch = channel
        request._domainId = domaindId.toString()
        request._vc = versionCode.toString()


        request._dm = Build.MODEL
        request._idfa = DeviceInfoUtils.getImsi(YHYBaseApplication.getInstance())
        request._idfv = DeviceInfoUtils.getImsi(YHYBaseApplication.getInstance())
        request._imei = DeviceInfoUtils.getImei(YHYBaseApplication.getInstance())
        request._mac = DeviceInfoUtils.getMacAddress(YHYBaseApplication.getInstance())
        request._sv = Build.VERSION.SDK_INT.toString()
        request._cip = DeviceInfoUtils.getLocalIpAddress()

        val commonMap = request.signLog().params
        val list = arrayListOf<Any>()
        request.logs.forEach {
            it.param["event"] = it.eventCode
            list.add(it.param)
        }

        val p = JSONUtils.toJson(hashMapOf(
                Pair("list", list),
                Pair("comm", commonMap)))

        return "type=2&data=${p}".let {
            Base64.encode(it.toByteArray(), Base64.DEFAULT).let {
                String(it)
            }
        }
    }
}