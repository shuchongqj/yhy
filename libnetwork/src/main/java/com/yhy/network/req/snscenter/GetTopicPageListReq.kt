package com.yhy.network.req.snscenter

import com.yhy.network.annotations.Req
import com.yhy.network.req.BaseRequest
import com.yhy.network.req.PageReq
import com.yhy.network.utils.SecurityType
import java.util.*


@Req(name = "snscenter.getTopicPageList")
class GetTopicPageListReq(val pageInfo : P) : BaseRequest() {

    init {
        securityType = SecurityType.None
    }

    companion object {
        class P(val pageInfo : PageReq, val type : Int = 1, startNum: Long) {

        }
    }
}