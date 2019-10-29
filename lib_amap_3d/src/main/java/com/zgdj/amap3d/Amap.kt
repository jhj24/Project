package com.zgdj.amap3d

import android.content.Context
import com.zgdj.lib.extention.loadingDialog
import com.zgdj.lib.utils.PreferenceUtil

fun Context.getLocation(isCurrentTime: Boolean = false, body: (LocationBean) -> Unit) {
    val position = PreferenceUtil.find(this, "location", LocationBean::class.java)
    val dialog = loadingDialog("正在获取当前位置...")
    if (position != null) {
        val locTime = position.time
        if (System.currentTimeMillis() - locTime <= 5 * 60 * 1000 && !isCurrentTime) {
            body(position)
            dialog.dismiss()
        } else {
            LocationUtils(this) {
                body(it)
                PreferenceUtil.save(this@getLocation, it, "location")
                dialog.dismiss()
            }.start()
        }
    } else {
        LocationUtils(this) {
            body(it)
            PreferenceUtil.save(this@getLocation, it, "location")
            dialog.dismiss()
        }.start()
    }
}