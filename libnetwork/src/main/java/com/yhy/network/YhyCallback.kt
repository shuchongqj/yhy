package com.yhy.network

interface YhyCallback<in T> {

    fun onSuccess(data : T)

    fun onFail(code : YhyCode, exception : Exception?)
}