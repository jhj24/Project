package com.zgdj.lib.net

open class DataResult<T>() {
    var code: Int = 0
    var msg: String = ""
    val data: T? = null
}