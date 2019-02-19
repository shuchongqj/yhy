package com.yhy.network.req.outplace

/**
 * Created by yangboxue on 2018/7/10.
 */
import com.yhy.network.annotations.Req
import com.yhy.network.req.BaseRequest
import com.yhy.network.utils.SecurityType

@Req(name = "outplace.cityList")
class GetOutPlaceCityListReq : BaseRequest() {

    init {
        securityType = SecurityType.None
    }

}