package com.zgdj.lib.net.callback

import android.app.Activity
import com.alibaba.android.arouter.launcher.ARouter
import com.jhj.httplibrary.callback.JsonHttpCallback
import com.zgdj.lib.config.ArouterConfig
import com.zgdj.lib.config.Config
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
            when(code){
                0->{
                    if (msg == Config.LOGIN_EXPIRED){
                        ARouter.getInstance().build(ArouterConfig.LOGIN).withString(Config.DATA,msg).navigation()
                        return
                    }else{
                        if (mIsOnFailureToast) mActivity.toast(msg)
                        if (mIsOnFailureFinish) mActivity.finish()
                    }
                }
                1->{
                    if (mIsOnSuccessToast && msg.isNotEmpty()) mActivity.toast(msg)
                }
            }
        }
        super.parseJson(str, resultType)
    }

    override fun onFailure(msg: String) {
        if (mIsOnFailureToast) mActivity.toast(msg)
        if (mIsOnFailureFinish) mActivity.finish()
    }
}