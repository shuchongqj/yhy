package com.yhy.network.req.snscenter

import com.yhy.common.utils.JSONUtils
import com.yhy.network.annotations.Param
import com.yhy.network.annotations.Req
import com.yhy.network.req.BaseRequest
import com.yhy.network.utils.SecurityType

@Req(name = "snscenter.getRecommendPageList")
class GetRecommendPageListReq(@Param(ignore = true)val p : P) : BaseRequest() {

    var recommendPageQuery : String = ""

    init {
        securityType = SecurityType.None
        recommendPageQuery = JSONUtils.toJson(p)
    }

    companion object {
        data class P (
            val traceId : String?,
            val pageNo : Int?,
            val pageSize : Int?,
            val opType : Int,
            val minSeqNo : Long,
            val maxSeqNo : Long,
            val tagId : Long
        )
    }
}