package com.zgdj.lib.extention

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zgdj.lib.base.fragment.BaseFragment
import com.zgdj.lib.utils.BackGroundUtils
import java.io.BufferedReader
import java.io.InputStreamReader

fun BaseFragment.getResDrawable(@DrawableRes id: Int): Drawable = mActivity.getResDrawable(id)

val Context.screenHeight: Int
    get() = resources.displayMetrics.heightPixels

val Context.screenWidth: Int
    get() = resources.displayMetrics.widthPixels

val Context.inflater: LayoutInflater
    get() = LayoutInflater.from(this)


fun Context.selected(press: Int, normal: Int): StateListDrawable {
    return selected(getResDrawable(press), getResDrawable(normal))
}

fun Context.getResColor(@ColorRes color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun Context.getResDrawable(@DrawableRes id: Int): Drawable {
    return ContextCompat.getDrawable(this, id) ?: resources.getDrawable(id)
}

fun selected(selected: Drawable, normal: Drawable): StateListDrawable {
    return BackGroundUtils.selected(selected, normal)
}

fun pressed(press: Drawable, normal: Drawable): StateListDrawable {
    return BackGroundUtils.pressed(press, normal)
}

fun <T> Context.assets(assetsFileName: String): T {
    val inputStream = resources.assets.open(assetsFileName)
    val text = inputStream.use {
        val buf = BufferedReader(InputStreamReader(inputStream, "utf-8"))
        return@use buf.readText()
    }
    return Gson().fromJson(text, object : TypeToken<T>() {}.type)
}


//===================================file=========================
fun Context.readAssets(name: String): String {
    val inputStream = resources.assets.open(name)
    val text = inputStream.use {
        val buf = BufferedReader(InputStreamReader(inputStream, "utf-8"))
        return@use buf.readText()
    }
    return text
}