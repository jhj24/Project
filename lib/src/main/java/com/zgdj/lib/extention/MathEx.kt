package com.zgdj.lib.extention

import java.util.*

/**
 * 保留两位小数
 */
fun Number.decimalDigits(): String {
    return String.format(Locale.getDefault(), "%.2f", this)
}

/**
 * 去除小数后面多余的0
 */
fun Number.removeDecimalPoint(): String {
    var str = this.toString()
    if (str.contains(".")) {
        str = str.replace("0+?$".toRegex(), "")
        str = str.replace("[.]$".toRegex(), "")
    }
    return str
}

/**
 * 距离转换
 */
fun Float.formatDistance(): String {
    return if (this > 1000) {
        "${(this / 1000).decimalDigits()}km"
    } else {
        "${this.decimalDigits()}m"
    }
}
