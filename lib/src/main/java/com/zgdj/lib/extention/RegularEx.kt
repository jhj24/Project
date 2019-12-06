package com.zgdj.lib.extention

import java.util.regex.Pattern


// ======正则表达式======
fun String?.isUrl(): Boolean {
    val pattern = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~/])+$";
    return match(pattern)
}

fun String?.isEmail(): Boolean {
    val pattern = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
    return match(pattern)
}

fun String?.isPhone(): Boolean {
    val pattern = "^1[34578]\\d{9}$"
    return match(pattern)
}

fun String?.isNumber(): Boolean {
    val pattern = "(^[0-9]+.?[0-9]+\$)|(^[0-9]+\$)"
    return match(pattern)
}

private fun String?.match(pattern: String): Boolean {
    if (this.isNullOrBlank()) {
        return false
    }
    val httpPattern = Pattern.compile(pattern)
    if (httpPattern.matcher(this).matches()) {
        return true
    }
    return false
}
