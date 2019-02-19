package com.yhy.network.req.snscenter

import com.yhy.common.utils.JSONUtils
import com.yhy.network.annotations.Param
import com.yhy.network.annotations.Req
import com.yhy.network.req.BaseRequest

@Req(name= "snscenter.getUserReplayList")
class GetUserReplayReq(val snsReplayListQuery : P) : BaseRequest() {

    init {
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