package com.yhy.network.req.snscenter

import com.yhy.common.utils.JSONUtils
import com.yhy.network.annotations.Param
import com.yhy.network.annotations.Req
import com.yhy.network.req.BaseRequest
import com.yhy.network.utils.SecurityType

@Req(name = "snscenter.dislike")
class DislikeReq(@Param(ignore = true) val p:P) : BaseRequest() {

    var disLikeArgs : String = ""
    init {
        securityType = SecurityType.None
        disLikeArgs = JSONUtils.toJson(p)
    }

    override fun set_did(_did: String?) {
        super.set_did(_did)
        p.snsDid = if (_did.isNullOrEmpty()){
            ""
        }else {
            _did!!
        }
    }

    companion object {
        data class P (
                val traceId : String?,
                val id : String?,
                val ugcId : Long?,
                val authorId : String?,
                val tagInfoList: List<TagInfo>
        ) {
            var snsDid : String = ""
        }

        data class TagInfo (
                val id : Long?,
                val name : String?,
                val type : Int?
        )
    }
}