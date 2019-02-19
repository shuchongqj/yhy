package com.yhy.network.req.snscenter


import com.yhy.network.annotations.Req
import com.yhy.network.req.BaseRequest
import com.yhy.network.utils.SecurityType

@Req(name = "snscenter.getTagInfoListByType")
class GetTagInfoListByTypeReq(var type: Int = 3) : BaseRequest() {

    init {
        securityType = SecurityType.None
    }

}
