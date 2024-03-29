package com.zgdj.amap3d

import android.content.Context
import android.support.v4.app.Fragment
import com.zgdj.lib.extention.loadingDialog
import com.zgdj.lib.utils.PreferenceUtil


fun Fragment.getLocation(isCurrentTime: Boolean = false, body: (LocationBean) -> Unit) {
    context?.getLocation(isCurrentTime, body)
}

fun Context.getLocation(isCurrentTime: Boolean = false, body: (LocationBean) -> Unit) {
    val position = PreferenceUtil.find(this, "location", LocationBean::class.java)
    val dialog = loadingDialog("正在获取当前位置...")
    if (position != null) {
        val locTime = position.time
        if (System.currentTimeMillis() - locTime <= 5 * 60 * 1000 && !isCurrentTime) {
            dialog.dismiss()
            body(position)
        } else {
            LocationUtils(this) {
                PreferenceUtil.save(this@getLocation, it, "location")
                dialog.dismiss()
                body(it)
            }.start()
        }
    } else {
        LocationUtils(this) {
            PreferenceUtil.save(this@getLocation, it, "location")
            dialog.dismiss()
            body(it)
        }.start()
    }
}