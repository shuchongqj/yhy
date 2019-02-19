package com.yhy.network.req.activitycenter

import com.yhy.common.utils.JSONUtils
import com.yhy.network.annotations.Param
import com.yhy.network.annotations.Req
import com.yhy.network.req.BaseRequest
import com.yhy.network.utils.SecurityType

/**
 * Created by yangboxue on 2018/7/19.
 */
@Req(name = "activitycenter.play")
class PlayReq(@Param(ignore = true) val p: P) : BaseRequest() {

    var playInfo: String = ""

    init {
        securityType = SecurityType.RegisteredDevice
        playInfo = JSONUtils.toJson(p)
    }

    companion object {
        data class P(
                val bizId: Long?,
                val bizType: String?
        )
    }
}