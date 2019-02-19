package com.yhy.network.req

import java.util.*

class PageReq(val pageNo : Int = 1, val pageSize : Int) {
    val traceId = UUID.randomUUID().toString()
}