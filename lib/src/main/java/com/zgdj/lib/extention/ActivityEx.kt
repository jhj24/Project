package com.zgdj.lib.extention

import android.Manifest
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jhj.imageselector.ImageSelector
import com.jhj.imageselector.bean.LocalMedia
import com.zgdj.lib.BaseApplication
import com.zgdj.lib.base.activity.BaseActivity
import com.zgdj.lib.config.Config
import com.zgdj.lib.ui.FileDisplayActivity
import com.zgdj.lib.ui.X5WebViewActivity
import com.zgdj.lib.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.startActivity
import java.io.BufferedReader
import java.io.InputStreamReader


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

fun Activity.imageDisplay(list: List<String>, index: Int) {
    val localMediaList = list.map { LocalMedia(it) }
    ImageSelector.preview(this, localMediaList, index)
}

fun BaseActivity.fileDisplay(path: String) {
    if (BaseApplication.isTBSLoadingSuccess) {
        requestPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE) {
            val dialog = loadingDialog()
            GlobalScope.launch(Dispatchers.Main) {
                var fileSize = 0L
                try {
                    if (isNetworkConnected()) {
                        fileSize = path.fileSize()
                    }
                    dialog.dismiss()
                    startActivity<FileDisplayActivity>(Config.PATH to path, Config.SIZE to fileSize)
                } catch (e: Exception) {
                    dialog.dismiss()
                    startActivity<FileDisplayActivity>(Config.PATH to path, Config.SIZE to fileSize)
                }
            }
        }

    } else {
        startActivity<X5WebViewActivity>()
    }
}

/**
 * 权限是否禁止,true-禁止
 */
fun Context.isAuthorityForbid(str: String?): Boolean {
    val authority = userAuthority?.find { it.path == str }
    return authority?.authority == 0
}

