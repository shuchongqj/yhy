package com.yhy.network.req.snscenter


import com.yhy.network.annotations.Param
import com.yhy.network.annotations.Req
import com.yhy.network.req.BaseRequest
import com.yhy.network.utils.SecurityType
import org.json.JSONArray
import org.json.JSONObject

@Req(name = "snscenter.saveUserCorrelation")
class SaveUserCorrelationReq(@Param(ignore = true) var idList : List<String>?) : BaseRequest() {
    var userCorrelation : String = ""
    init {
        securityType = SecurityType.None
    }

    override fun set_did(_did: String?) {
        super.set_did(_did)
        val snsDid = if (_did.isNullOrEmpty()){
            ""
        }else {
            _did!!
        }
        val jsonObject = JSONObject()
        jsonObject.put("snsDid", snsDid)
        jsonObject.put("idList", JSONArray().apply {
            idList?.forEach {
                this.put(it)
            }
        })
        userCorrelation = jsonObject.toString()
    }

}
