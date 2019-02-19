package com.yhy.network.utils

import com.yhy.common.base.YHYBaseApplication
import com.yhy.common.utils.SPUtils
import com.yhy.network.YhyCallback
import com.yhy.network.YhyCaller
import com.yhy.network.YhyCode
import com.yhy.network.api.user.UserApi
import com.yhy.network.manager.AccountManager
import com.yhy.network.manager.DeviceManager
import com.yhy.network.req.BaseRequest
import com.yhy.network.req.device.RegisterReq
import com.yhy.network.resp.Response
import com.yhy.network.resp.device.RegisterResp
import java.util.concurrent.ConcurrentHashMap
val pendingRegisterDeviceRequestList = ConcurrentHashMap<YhyCaller<*, *>, BaseRequest>()
private var isRegisterDevice = false
private var lock = Any()
fun checkSecurity(call : YhyCaller<*, *>?, req : BaseRequest?) : Boolean{
    if (req == null || req !is RegisterReq){
        synchronized(lock){
            if (DeviceManager.deviceManager.pk == null
                    || DeviceManager.deviceManager.getCertStr().isEmpty()
                    || DeviceManager.deviceManager.deviceInfo.isEmpty()
                    || DeviceManager.deviceManager.deviceToken.isEmpty()
                    || DeviceManager.deviceManager.deviceId.isEmpty()){
                if (call != null && req != null){
                    pendingRegisterDeviceRequestList.putIfAbsent(call, req)
                }

                if (isRegisterDevice){
                    return false
                }
                isRegisterDevice = true

                registerDevice()

                return false
            }
        }
    }
    return true
}

fun registerDevice(){
    val r = RegisterReq(DeviceInfoUtils.getDeviceInfoToJsonString(YHYBaseApplication.getInstance()), YHYBaseApplication.getInstance().yhyEnvironment.channel, "rsa")

    val callback = object : YhyCallback<Response<RegisterResp>>{
        override fun onSuccess(data: Response<RegisterResp>) {

            val deviceManager = DeviceManager.deviceManager
            val accountManager = AccountManager.accountManager
            deviceManager.saveDeviceInfoFromNet(data.content.certificate, data.content.deviceToken)
            if (!accountManager.userToken.isEmpty() && !accountManager.getDeviceToken().isEmpty() && !accountManager.getCertStr().isEmpty()){
                System.err.println("test========doDeviceRegist0 ${accountManager.getDeviceId()}")
                NetManagerProxy.doDeviceRegist(accountManager.getCertStr(), accountManager.getDeviceToken())
            }else {
                System.err.println("test========doDeviceRegist5 ${deviceManager.deviceId}")
                NetManagerProxy.doDeviceRegist(deviceManager.getCertStr(), deviceManager.deviceToken)
            }

            //TODO 成功后,保存token等信息然后，继续请求
            synchronized(lock){
                isRegisterDevice = false
                pendingRegisterDeviceRequestList.forEach {
                    if (it.key.isCancel()){
                        return@forEach
                    }
                    it.key.execAsync()
                }
            }
        }

        override fun onFail(code: YhyCode, exception: Exception?) {
            //TODO 通过网络触发，暂时定时请求
//                        if (AndroidUtils.isNetWorkAvalible(YHYBaseApplication.getInstance())){
//
//                        }else {
//
//                        }
//                        val size = pendingRegisterDeviceRequestList.size
            Thread(Runnable {
                Thread.sleep(3000)
                registerDevice()
            }).start()
        }

    }

    val c = api.register(r, callback)
    c.execAsync(false)
}