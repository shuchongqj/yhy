package com.yhy.network.req.snscenter

import com.yhy.common.utils.JSONUtils
import com.yhy.network.annotations.Param
import com.yhy.network.annotations.Req
import com.yhy.network.req.BaseRequest
import com.yhy.network.utils.SecurityType

/**
	traceId	// 用于追踪日志的UUID
	title	// 文章标题
    pageNo	// 页码
	pageSize	// 每页大小
 * Created by Jiervs on 2018/7/9.
 */
@Req(name = "snscenter.searchPageList")
class SearchPageListReq(@Param(ignore = true)val s : S): BaseRequest() {

    var searchPageQuery : String = ""

    init {
        securityType = SecurityType.None
        searchPageQuery = JSONUtils.toJson(s)
    }

    companion object {
        data class S (
            val traceId : String?,
            val title : String?,
            val pageNo : Int?,
            val pageSize : Int?
        )
    }
}