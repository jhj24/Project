package com.zgdj.lib.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.Serializable
import java.util.*

/**
 * 个人偏好设置
 * Created by jhj on 17-7-31.
 */
object PreferenceUtil {


    fun <T : Any> save(context: Context, entity: T?, key: String): Boolean {
        if (entity == null) {
            return false
        }
        val prefFileName = entity.javaClass.getName()
        val sp = context.getSharedPreferences(prefFileName, 0)
        val et = sp.edit()
        val json = toGson(entity)
        et.putString(key, json)
        return et.commit()
    }

    fun <T> findAll(context: Context, clazz: Class<T>): List<T> {
        val prefFileName = clazz.name
        val sp = context.getSharedPreferences(prefFileName, 0)
        val values = sp.all as Map<String, String>?

        val results = ArrayList<T>()

        if (values == null || values.isEmpty())
            return results

        for (json in values.values) {
            val bean = parseJson<T>(json, clazz)
            bean?.let { results.add(it) }
        }
        return results
    }

    fun <T> find(context: Context, key: String, clazz: Class<T>): T? {
        val prefFileName = clazz.name
        val sp = context.getSharedPreferences(prefFileName, 0)
        val json = sp.getString(key, null) ?: return null
        return parseJson<T>(json, clazz)
    }

    fun <T : Serializable> delete(context: Context, key: String, clazz: Class<T>) {
        val prefFileName = clazz.name
        val sp = context.getSharedPreferences(prefFileName, 0)
        if (sp.contains(key)) {
            sp.edit().remove(key).apply()
        }
    }

    fun <T : Serializable> deleteAll(context: Context, clazz: Class<T>) {
        val prefFileName = clazz.name
        val sp = context.getSharedPreferences(prefFileName, 0)
        sp.edit().clear().apply()
    }


    private fun <T> parseJson(json: String, cls: Class<*>): T? {
        try {
            return Gson().fromJson(json, cls) as T
        } catch (e: JsonSyntaxException) {
            Logger.e("解析gson字符串失败")
        }

        return null
    }

    private fun toGson(obj: Any): String? {
        try {
            return Gson().toJson(obj)
        } catch (e: Exception) {
            Logger.e("数据生成gson字符串失败")
        }
        return null
    }
}