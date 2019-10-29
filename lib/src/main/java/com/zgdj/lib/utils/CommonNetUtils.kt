package com.zgdj.lib.utils

import android.app.Activity
import android.content.Context
import com.jhj.httplibrary.HttpCall
import com.jhj.httplibrary.adapt.CallAdapt
import com.jhj.httplibrary.model.HttpParams
import com.zgdj.lib.bean.FileBean
import com.zgdj.lib.config.GlobalConfig
import com.zgdj.lib.config.UrlConfig
import com.zgdj.lib.extention.*
import com.zgdj.lib.net.DataResult
import com.zgdj.lib.net.callback.DataDialogHttpCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

object CommonNetUtils {

    val baseUrl: String
        get() {
            if (GlobalConfig.IS_TEST_USER) {
                val topActivity = ActivityManager.topActivity
                val user = topActivity.loginUserInfo
                if (user != null && user.ip.isUrl()) {
                    return user.ip
                }
            }
            return UrlConfig.BASE_URL
        }


    /**
     * 文件图片上传
     */
    fun uploadMedia(
        context: Context,
        module: String,
        use: String,
        vararg pathList: String,
        body: (Boolean, List<FileBean>) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            val dialog = context.uploadDialog { HttpCall.cancelAll() }
            var isSuccess = true
            val list = arrayListOf<FileBean>()
            withContext(Dispatchers.IO) {
                for (path in pathList) {
                    val result = context.httpPost(UrlConfig.UPLOAD)
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


    fun delete(activity: Activity, url: String, msg: String, vararg pairs: Pair<String, String>, body: () -> Unit) {
        val httpParams = HttpParams()
        pairs.forEach { httpParams.put(it.first, it.second) }
        activity.messageDialog(msg = msg) { alert, view ->
            activity.httpPost(url)
                .addParams(httpParams)
                .enqueue(object : DataDialogHttpCallback<Any>(activity, "正在删除...") {

                    override val mIsOnSuccessToast: Boolean
                        get() = true

                    override fun onSuccess(data: Any?, resultType: ResultType) {
                        body()
                    }
                })
        }
    }
}