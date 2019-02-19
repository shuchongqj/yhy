package com.yhy.network.req.snscenter

import com.yhy.common.utils.JSONUtils
import com.yhy.network.annotations.Param
import com.yhy.network.annotations.Req
import com.yhy.network.req.BaseRequest

@Req(name= "snscenter.getUserSuperbList")
class GetUserSuperbListReq(@Param(ignore = true)val p : P) : BaseRequest() {

    var snsSuperbListQuery : String = ""

    init {
        snsSuperbListQuery = JSONUtils.toJson(p)
    }

    companion object {
        data class P (
                val traceId : String?,
                val userId : Long,
                val pageNo : Int,
                val pageSize : Int,
                val notInLiveIds : List<String>
        )
    }

}