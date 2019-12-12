package com.zgdj.lib.extention

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.support.v4.app.Fragment
import com.jhj.httplibrary.HttpCall
import com.jhj.httplibrary.adapt.CallAdapt
import com.jhj.httplibrary.model.HttpParams
import com.zgdj.lib.bean.FileBean
import com.zgdj.lib.config.UrlConfig
import com.zgdj.lib.net.DataResult
import com.zgdj.lib.net.callback.DataDialogHttpCallback
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

fun Fragment.delete(url: String, msg: String, vararg pairs: Pair<String, String>, body: () -> Unit = {}) {
    activity?.delete(url, msg, *pairs, body = body)
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



/**
 * 处理同步网络请求结果
 */
suspend fun <T> DataResult<T>?.applyMain(
    activity: Activity,
    isOnSuccessToast: Boolean = false,
    isOnFailureToast: Boolean = true,
    isOnFailureFinish: Boolean = false,
    block: (T?) -> Unit = {}
): T? {
    if (this == null) return null
    return withContext(Dispatchers.Main) {
        if (!isLoginExpired){
            if (code ==1){
                if (isOnSuccessToast && msg.isNotBlank()) msg.let { activity.toast(it) }
                block(data)
            }else if (code == 0){
                if (isOnFailureToast && msg.isNotBlank()) activity.toast(msg)
                if (isOnFailureFinish && msg.isNotBlank()) activity.finish()
            }else if (code == -1){
                if (msg.isNotBlank()) activity.toast(msg)
            }
        }
        return@withContext data
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




