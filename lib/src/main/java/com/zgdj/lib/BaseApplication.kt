package com.zgdj.lib

import android.app.Activity
import android.os.Bundle
import android.support.multidex.MultiDexApplication
import com.alibaba.android.arouter.launcher.ARouter
import com.jhj.httplibrary.HttpCall
import com.jhj.httplibrary.interceptor.LoggerInterceptor
import com.jhj.httplibrary.model.HttpHeaders
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tencent.smtt.sdk.QbSdk
import com.zgdj.lib.config.UrlConfig
import com.zgdj.lib.extention.getResColor
import com.zgdj.lib.utils.ActivityManager
import com.zgdj.lib.utils.Logger

open class BaseApplication : MultiDexApplication() {


    companion object {
        var instance: BaseApplication = BaseApplication()
        var isTBSLoadingSuccess = false
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        HttpCall.init(this.applicationContext)
        if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
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

        //================Activity监听==============
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {
            }

            override fun onActivityResumed(activity: Activity?) {
            }

            override fun onActivityStarted(activity: Activity?) {
            }

            override fun onActivityDestroyed(activity: Activity?) {
                if (activity != null) {
                    ActivityManager.removeActivity(activity)
                }
                Logger.w(activity.toString())
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity?) {
            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                if (activity != null) {
                    ActivityManager.addActivity(activity)
                    onActivityCreated(activity)
                }
                Logger.w(activity.toString())
            }
        })

    }

    open fun onActivityCreated(activity: Activity) {}


}