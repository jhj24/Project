package com.zgdj.lib

import android.support.multidex.MultiDexApplication
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.tencent.smtt.sdk.QbSdk
import com.zgdj.lib.extention.getResColor
import com.zgdj.lib.utils.Logger

open class BaseApplication : MultiDexApplication() {

    companion object {
        var instance: BaseApplication = BaseApplication()
        var isTBSLoadingSuccess = false
    }

    override fun onCreate() {
        super.onCreate()
        instance = this


        //x5内核
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

        //===============下拉刷新=================
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColors(0xfff1f1f1.toInt(), getResColor(R.color.black))
            return@setDefaultRefreshHeaderCreator ClassicsHeader(context)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            layout.setPrimaryColors(0xfff1f1f1.toInt(), getResColor(R.color.black))
            return@setDefaultRefreshFooterCreator ClassicsFooter(context)
        }
    }
}