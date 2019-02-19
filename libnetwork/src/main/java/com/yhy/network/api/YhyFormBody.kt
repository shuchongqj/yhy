package com.yhy.network.api

import com.yhy.network.manager.AccountManager
import com.yhy.network.req.BaseRequest
import com.yhy.network.utils.*
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink

class YhyFormBody(private var request : BaseRequest) : RequestBody() {

    private val CONTENT_TYPE = MediaType.parse("application/x-www-form-urlencoded")
    private var formBody : FormBody? = null

    private var sign : Boolean = false

    override fun contentType(): MediaType? {
        return CONTENT_TYPE
    }

    override fun writeTo(sink: BufferedSink?) {
        if (!sign){
            val accountManager = AccountManager.accountManager
            val builder = FormBody.Builder()

            request._aid = appId.toString()
            request._ch = channel
            request._domid = domaindId.toString()
            request._vc = versionCode.toString()
            request._uid = accountManager.userId.toString()
            request._tk = accountManager.userToken
            request._dtk = accountManager.getDeviceToken()
            request._pn = accountManager.phoneNumber
            request._did = accountManager.getDeviceId()
            request._session = accountManager.getSession()
            request._cip = DeviceInfoUtils.getLocalIpAddress()
            request.sign().params.forEach {
                builder.add(it.key, it.value.toString())
            }
            formBody = builder.build()
        }
        sign = true
        formBody?.writeTo(sink)
    }
}