package com.zgdj.lib.extention

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.os.Looper
import com.jhj.httplibrary.HttpCall
import com.jhj.httplibrary.adapt.CallAdapt
import com.jhj.httplibrary.model.HttpParams
import com.zgdj.lib.bean.FileBean
import com.zgdj.lib.bean.UserBean
import com.zgdj.lib.config.Config
import com.zgdj.lib.config.UrlConfig
import com.zgdj.lib.net.DataResult
import com.zgdj.lib.net.callback.DataDialogHttpCallback
import com.zgdj.lib.utils.ActivityManager
import com.zgdj.lib.utils.SystemEnv
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.toast
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

fun Context.isNetworkConnected(): Boolean {
    val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = manager.activeNetworkInfo
    if (networkInfo != null) {
        return networkInfo.isConnected
    }
    return false
}

val Context.loginUserInfo: UserBean?
    get() {
        val user: UserBean? = SystemEnv.getLogin(this)
        if (user == null) {
            loginExpired()
            return null
        }
        return user
    }


fun Context.uploadMedia(module: String, use: String, vararg pathList: String, body: (Boolean, List<FileBean>) -> Unit) {
    GlobalScope.launch(Dispatchers.Main) {
        val dialog = uploadDialog { HttpCall.cancelAll() }
        var isSuccess = true
        val list = arrayListOf<FileBean>()
        withContext(Dispatchers.IO) {
            for (path in pathList) {
                val result = HttpCall.post(UrlConfig.MEDIA_UPLOAD)
                    .addHeader("module", module)
                    .addHeader("use", use)
                    .addFile("file", File(path))
                    .adaptSync(object : CallAdapt<DataResult<FileBean>>() {})
                if (result?.code != 1) {
                    isSuccess = false
                }
                if (result?.data != null) {
                    list.add(result.data)
                }
            }
        }
        body(isSuccess, list)
        dialog.dismiss()
    }
}

fun Activity.delete(url: String, msg: String, vararg pairs: Pair<String, String>, body: () -> Unit = {}) {
    val httpParams = HttpParams()
    pairs.forEach { httpParams.put(it.first, it.second) }
    messageDialog(msg = msg) { alert, view ->
        HttpCall.post(url)
            .addParams(httpParams)
            .enqueue(object : DataDialogHttpCallback<Any>(this, "正在删除...") {

                override val mIsOnSuccessToast: Boolean
                    get() = true

                override fun onSuccess(data: Any?, resultType: ResultType) {
                    body()
                }
            })
    }
}

fun Context.loginExpired(msg: String? = Config.LOGIN_EXPIRED): Boolean {
    if (msg == Config.LOGIN_EXPIRED && ActivityManager.activityCount != 0) {
        if (Looper.myLooper() == Looper.getMainLooper()) toast(Config.LOGIN_EXPIRED)
        ActivityManager.finishAllActivity()
        //startActivity<LoginActivity>()
        SystemEnv.deleteLogin(this)
        //SystemEnv.deleteAuthority(this)
        val user = SystemEnv.getLogin(this) ?: return true
        //JPushInterface.deleteAlias(this, user.key)
        return true
    }
    return false
}


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


@Throws(Exception::class)
suspend fun String.fileSize(): Long {
    return withContext(Dispatchers.Default) {
        if (isUrl()) {
            val url = URL(this@fileSize)
            val urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.connectTimeout = 5 * 1000
            urlConnection.requestMethod = "GET"
            urlConnection.connect()
            urlConnection.contentLength.toLong()
        } else {
            val file = File(this@fileSize)
            file.length()
        }
    }
}




