package com.zgdj.lib.net

import com.alibaba.android.arouter.launcher.ARouter
import com.zgdj.lib.config.ArouterConfig
import com.zgdj.lib.config.Config

open class DataResult<T>() {
    var code: Int = 0
    var msg: String = ""
    val data: T? = null

    val isLoginExpired: Boolean
        get() {
            val isExpired = code == 0 && msg == Config.LOGIN_EXPIRED
            if (isExpired) ARouter.getInstance().build(ArouterConfig.LOGIN).withString(Config.DATA, msg).navigation()
            return isExpired
        }
}