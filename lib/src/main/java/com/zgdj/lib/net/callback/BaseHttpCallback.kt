package com.zgdj.lib.net.callback

import android.app.Activity
import com.jhj.httplibrary.callback.JsonHttpCallback
import com.zgdj.lib.config.Config
import com.zgdj.lib.extention.loginExpired
import org.jetbrains.anko.toast
import org.json.JSONObject

abstract class BaseHttpCallback<T>(activity: Activity) : JsonHttpCallback<T>(activity) {


    open val mIsOnFailureFinish = false
    open val mIsOnFailureToast = true
    open val mIsOnSuccessToast = false

    override fun parseJson(str: String, resultType: ResultType) {
        val jsonObject = JSONObject(str)
        if (jsonObject.has("code") && jsonObject.has("msg")) {
            val msg = jsonObject.getString("msg")
            val code = jsonObject.getInt("code")
            if (mIsOnSuccessToast && msg.isNotBlank()) mActivity.toast(msg)
            if (code == 0 && msg == Config.LOGIN_EXPIRED) {
                mActivity.loginExpired(msg)
                return
            }
        }
        super.parseJson(str, resultType)
    }
}