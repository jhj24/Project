package com.zgdj.lib.extention

import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.zgdj.lib.bean.AuthorityBean
import com.zgdj.lib.bean.UserBean
import com.zgdj.lib.config.ArouterConfig
import com.zgdj.lib.config.Config
import com.zgdj.lib.utils.PreferenceUtil
import java.io.Serializable

private const val LOGIN_INFO = "login"

fun Context.setLoginInfo(info: UserBean) {
    PreferenceUtil.save(this, info, LOGIN_INFO)
}

fun Context.getLoginInfo(isLoginSuccess: Boolean = true): UserBean? {
    val user = PreferenceUtil.find(this, LOGIN_INFO, UserBean::class.java)
    if (isLoginSuccess && user == null) {
        ARouter.getInstance()
            .build(ArouterConfig.LOGIN)
            .withString(Config.DATA, Config.LOGIN_EXPIRED)
            .navigation()
    }
    return user
}

val Context.deleteLoginInfo: Unit
    get() = PreferenceUtil.delete(this, LOGIN_INFO, UserBean::class.java)

fun Context.deleteAllLogin() {
    PreferenceUtil.deleteAll(this, UserBean::class.java)
}

//===========操作权限===========

private const val USER_AUTHORITY = "user_authority"

data class AuthorityHolder(val list: List<AuthorityBean>) : Serializable

var Context.userAuthority: List<AuthorityBean>?
    get() {
        val holder = PreferenceUtil.find(this, USER_AUTHORITY, AuthorityHolder::class.java)
        return holder?.list
    }
    set(value) {
        PreferenceUtil.save(this, AuthorityHolder(value.orEmpty()), USER_AUTHORITY)
    }

fun Context.deleteAuthority() {
    PreferenceUtil.deleteAll(this, AuthorityHolder::class.java)
}


