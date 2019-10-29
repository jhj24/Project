package com.zgdj.lib

import android.support.multidex.MultiDexApplication
import com.tencent.smtt.sdk.QbSdk
import com.zgdj.lib.utils.Logger

open class BaseApplication : MultiDexApplication() {

    companion object {
        var instance: BaseApplication = BaseApplication()
        var isTBSLoadingSuccess = false
    }

    override fun onCreate() {
        super.onCreate()
        instance = this



        QbSdk.setDownloadWithoutWifi(true)
        QbSdk.initX5Environment(this, object : QbSdk.PreInitCallback {
            override fun onCoreInitFinished() {
                //x5内核初始化完成回调接口，此接口回调并表示已经加载起来了x5，有可能特殊情况下x5内核加载失败，切换到系统内核。
            }

            override fun onViewInitFinished(b: Boolean) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Logger.w("tbs加载内核是否成功:$b")
                isTBSLoadingSuccess = b

            }
        })
    }
}