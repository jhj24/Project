package com.zgdj.lib.extention

import android.content.Context
import android.support.v4.app.Fragment
import android.view.View
import com.jhj.prompt.fragment.TimeFragment
import com.jhj.prompt.fragment.options.interfaces.OnTimeSelectedListener
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*

private const val defaultPattern = "yyyy-MM-dd"

fun Fragment.timePicker(date: Date?, pattern: String = defaultPattern, body: (date: Date) -> Unit) {
    context?.timePicker(date, pattern, body)
}

fun Fragment.timePicker(startDate: Date?, endDate: Date?, isStartTime: Boolean, pattern: String = defaultPattern,
                        msg: String = "开始时间小于结束事件，请重新选择", body: (date: Date) -> Unit) {
    context?.timePicker(startDate, endDate, isStartTime, pattern, msg, body)
}

fun Context.timePicker(date: Date?, pattern: String = defaultPattern, body: (date: Date) -> Unit) {
    val now = Calendar.getInstance()
    if (date != null) {
        now.time = date
    }
    TimeFragment.Builder(this)
        .setSubmitListener(object : OnTimeSelectedListener {
            override fun onTimeSelect(date: Date, v: View) {
                body(date)
            }
        })
        .setCancelOnTouchOut(true)
        .setDisplayStyle(pattern.formatBooleanArray)
        .setTitle("请选择")
        .setDate(now)
        .show()
}

fun Context.timePicker(startDate: Date?, endDate: Date?, isStartTime: Boolean, pattern: String = defaultPattern,
                       msg: String = "开始时间小于结束事件，请重新选择", body: (date: Date) -> Unit) {
    val now = Calendar.getInstance()
    if (isStartTime && startDate != null) {
        now.time = startDate
    } else if (endDate != null) {
        now.time = endDate
    }

    TimeFragment.Builder(this)
        .setSubmitListener(object : OnTimeSelectedListener {
            override fun onTimeSelect(date: Date, v: View) {
                if (isStartTime) {
                    if (endDate != null && date.after(endDate)) {
                        this@timePicker.toast(msg)
                        return
                    }
                } else {
                    if (startDate != null && date.before(startDate)) {
                        this@timePicker.toast(msg)
                        return
                    }
                }
                body(date)
            }
        })
        .setCancelOnTouchOut(true)
        .setDisplayStyle(pattern.formatBooleanArray)
        .setTitle("请选择")
        .setDate(now)
        .show()
}


val String.sdf: SimpleDateFormat
    get() = SimpleDateFormat(this, Locale.getDefault())

val String.formatBooleanArray: BooleanArray
    get() {
        val list = booleanArrayOf(true, true, true, true, true, true)
        list[0] = this.contains("yyyy") //年
        list[1] = this.contains("MM")   //月
        list[2] = this.contains("dd")   //日
        list[3] = this.contains("HH")   //时
        list[4] = this.contains("mm")   //分
        list[5] = this.contains("ss")   //秒
        return list
    }

/**
 * 日期格式化
 */
fun Date?.format(pattern: String = defaultPattern): String? {
    if (this == null) return null
    try {
        return pattern.sdf.format(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}


/**
 * 字符串格式成日期
 */
fun String?.parse(pattern: String = defaultPattern): Date? {
    if (this == null) return null
    try {
        return pattern.sdf.parse(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}