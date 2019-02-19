package com.yhy.network.req.snscenter

import com.yhy.common.utils.JSONUtils
import com.yhy.network.annotations.Param
import com.yhy.network.annotations.Req
import com.yhy.network.req.BaseRequest
import com.yhy.network.utils.SecurityType

@Req(name = "snscenter.getShortVideoDetail")
class GetShortVideoDetail(val snsShortVideoDetailQuery : P) : BaseRequest() {

    init {
    }

    companion object {
        data class P (
                val ugcId : Long?
        )
    }
}