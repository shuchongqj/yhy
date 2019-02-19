package com.yhy.network.req.snscenter


import com.yhy.network.annotations.Req
import com.yhy.network.req.BaseRequest
import com.yhy.network.utils.SecurityType

@Req(name = "snscenter.queryGuidanceRecord")
class QueryGuidanceRecordReq : BaseRequest() {

    var snsDid : String = ""
    init {
        securityType = SecurityType.None
    }

    override fun set_did(_did: String?) {
        super.set_did(_did)
        snsDid = if (_did.isNullOrEmpty()){
            ""
        }else {
            _did!!
        }
    }

}
