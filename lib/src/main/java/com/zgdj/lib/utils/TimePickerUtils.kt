package com.zgdj.lib.utils

import android.content.Context
import android.view.View
import com.jhj.prompt.fragment.TimeFragment
import com.jhj.prompt.fragment.options.interfaces.OnTimeSelectedListener
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*

object TimePickerUtils {
    val type1 = booleanArrayOf(true, true, true, true, true, false)
    val type2 = booleanArrayOf(true, true, true, false, false, false)
    val type3 = booleanArrayOf(false, false, false, true, true, false)


    fun timePicker(context: Context, date: Date?, type: BooleanArray = booleanArrayOf(true, true, true, false, false, false), body: (date: Date) -> Unit) {
        val now = Calendar.getInstance()
        if (type.size != 6) {
            return
        }
        if (date != null) {
            now.time = date
        }
        TimeFragment.Builder(context)
                .setSubmitListener(object : OnTimeSelectedListener {
                    override fun onTimeSelect(date: Date, v: View) {
                        body(date)
                    }
                })
                .setCancelOnTouchOut(true)
                .setDisplayStyle(type)
                .setTitle("请选择")
                .setDate(now)
                .show()
    }


    fun timePicker(context: Context, startDate: Date?, endDate: Date?, isStartTime: Boolean,
                   type: BooleanArray = booleanArrayOf(true, true, true, true, true, false), msg: String,
                   body: (date: Date) -> Unit, calculate: (startDate: Date, endDate: Date) -> Unit = { _, _ -> }) {
        val now = Calendar.getInstance()
        if (type.size != 6) {
            return
        }
        if (isStartTime && startDate != null) {
            now.time = startDate
        } else if (endDate != null) {
            now.time = endDate
        }

        TimeFragment.Builder(context)
                .setSubmitListener(object : OnTimeSelectedListener {
                    override fun onTimeSelect(date: Date, v: View) {
                        if (isStartTime) {
                            if (endDate != null && date.after(endDate)) {
                                context.toast(msg)
                                return
                            }
                            body(date)
                            endDate?.let {
                                calculate(date, it)
                            }
                        } else {
                            if (startDate != null && date.before(startDate)) {
                                context.toast(msg)
                                return
                            }
                            body(date)
                            startDate?.let {
                                calculate(it, date)
                            }
                        }
                    }
                })
                .setCancelOnTouchOut(true)
                .setDisplayStyle(type)
                .setTitle("请选择")
                .setDate(now)
                .show()
    }

    fun parseData(string: String?, format: String = "yyyy-MM-dd"): Date? {
        try {
            val sdf = SimpleDateFormat(format, Locale.CHINA)
            if (string.isNullOrBlank()) {
                return null
            }
            return sdf.parse(string)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun parseData(hour: Int, minute: Int): Date? {
        val calendar = Calendar.getInstance()
        try {
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            return calendar.time
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun format(date: Date, format: String = "yyyy-MM-dd"): String? {
        try {
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            return sdf.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun formatType(format: String): BooleanArray {
        val list = booleanArrayOf(true, true, true, true, true, true)
        list[0] = format.contains("yyyy") //年
        list[1] = format.contains("MM")   //月
        list[2] = format.contains("dd")   //日
        list[3] = format.contains("HH")   //时
        list[4] = format.contains("mm")   //分
        list[5] = format.contains("ss")   //秒
        return list
    }


}