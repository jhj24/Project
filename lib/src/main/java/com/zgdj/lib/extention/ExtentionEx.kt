package com.zgdj.lib.extention

import android.app.Activity
import com.zgdj.lib.config.Config
import com.zgdj.lib.net.DataResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.anko.toast


/**
 * 处理同步网络请求结果
 */
suspend fun <T> DataResult<T>?.applyMain(
    activity: Activity,
    isOnSuccessToast: Boolean = false,
    isOnFailureToast: Boolean = true,
    isOnFailureFinish: Boolean = false,
    block: (T?) -> Unit
) {
    if (this == null) return
    withContext(Dispatchers.Main) {
        if (code == 1) {
            if (isOnSuccessToast && msg.isNotBlank()) msg.let { activity.toast(it) }
            block(data)
        } else if (code == 0) {
            if (msg == Config.LOGIN_EXPIRED) {//身份过期
                activity.loginExpired(msg)
            } else {
                if (isOnFailureToast && msg.isNotBlank()) activity.toast(msg)
                if (isOnFailureFinish && msg.isNotBlank()) activity.finish()
            }
        } else if (code == -1) {
            if (msg.isNotBlank()) activity.toast(msg)
        }
        return@withContext
    }
}