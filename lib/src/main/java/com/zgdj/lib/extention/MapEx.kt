package com.zgdj.lib.extention

import android.content.Context
import android.graphics.Color
import org.json.JSONObject

fun HashMap<String, Any>.getString(key: String, default: String = ""): String {
    return get(key)?.toString() ?: default
}

fun HashMap<String, Any>.getInt(key: String, default: Int = 0): Int {
    val value = get(key)
    return when (value) {
        is String -> {
            value.toInt()
        }
        is Double -> {
            value.toInt()
        }
        is Int -> {
            value
        }
        else -> {
            default
        }
    }
}

fun HashMap<String, Any>.getColor(context: Context, key: String, default: Int = 0xff000000.toInt()): Int {
    val color = get(key) ?: default
    when (color) {
        is String -> {
            var parseColor = 0
            try {
                parseColor = Color.parseColor(color)
            } catch (e: Exception) {
            }
            return parseColor
        }
        is Int -> {
            try {
                return context.getResColor(color)
            } catch (e: Exception) {
                return color
            }
        }
        else -> {
            return default
        }
    }
}

fun HashMap<String, Any>.getBoolean(key: String, default: Boolean = false): Boolean {
    val value = get(key)
    return when (value) {
        is String -> {
            value.toBoolean()
        }
        is Boolean -> {
            value
        }
        else -> {
            default
        }
    }
}

fun HashMap<String, Any>.getBooleanArray(key: String, default: BooleanArray = booleanArrayOf()): BooleanArray {
    return (get(key) as ArrayList<Boolean>?)?.toBooleanArray() ?: default
}

fun Map<*, *>.toJsonString(): String {
    val jsonObject = JSONObject(this)
    return jsonObject.toString()
}


