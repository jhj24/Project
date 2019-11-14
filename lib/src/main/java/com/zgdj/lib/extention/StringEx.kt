package com.zgdj.lib.extention

import com.jhj.httplibrary.HttpCall
import com.zgdj.lib.utils.TimePickerUtils
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

//=======文件======
val String.fileName: String
    get() {
        val index = this.lastIndexOf("/")
        if (index != -1) {
            return substring(index + 1, this.length)
        }
        return this
    }

val String?.filePath: String
    get() {
        if (this != null) {
            return if (isUrl()) {
                this.replace("\\", "/")
            } else {
                HttpCall.baseUrl + this.replace("\\", "/")
            }
        }
        return ""
    }


// ========日期=======
val String.sdf: SimpleDateFormat
    get() = SimpleDateFormat(this, Locale.getDefault())

val String.formatBooleanArray: BooleanArray
    get() = TimePickerUtils.formatType(this)

fun String.sdf(date: Date): String? {
    try {
        val sdf = SimpleDateFormat(this, Locale.getDefault())
        return sdf.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

fun String?.parse(str: String?): Date? {
    try {
        val sdf = SimpleDateFormat(this, Locale.getDefault())
        return sdf.parse(str)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

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


//=======Math======
fun String.removeDecimalPoint(): String {
    var str = "0"
    if (this.contains(".")) {
        str = this.replace("0+?$".toRegex(), "")
        str = str.replace("[.]$".toRegex(), "")
    }
    return str
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
