package com.zgdj.lib.utils

import android.content.Context
import com.zgdj.lib.bean.AuthorityBean
import com.zgdj.lib.bean.UserBean
import java.io.Serializable

object SystemEnv {

    //=========用户信息===============
    private const val LOGIN = "login"

    fun saveLogin(context: Context, user: UserBean) {
        //JPushInterface.setAlias(context, user.key, user.username)
        PreferenceUtil.save(context, user, LOGIN)
    }

    fun getLogin(context: Context): UserBean? {
        return PreferenceUtil.find(context, LOGIN, UserBean::class.java)
    }

    fun deleteLogin(context: Context) {
        PreferenceUtil.deleteAll(context, UserBean::class.java)
    }

    //==========用户详细信息================
    private const val USER_INFO = "user_info"

    fun saveUserInfo(context: Context, userInfo: UserBean) {
        PreferenceUtil.save(context, userInfo, USER_INFO);
    }

    fun getUserInfo(context: Context): UserBean? {
        return PreferenceUtil.find(context, USER_INFO, UserBean::class.java)
    }

    fun deleteUserInfo(context: Context) {
        PreferenceUtil.deleteAll(context, UserBean::class.java)
    }

    //权限
    private const val USER_AUTHORITY = "user_authority"

    fun saveUserAuthority(context: Context, list: List<AuthorityBean>) {
        PreferenceUtil.save(context, AuthorityHolder(list), USER_AUTHORITY)
    }

    fun getUserAuthority(context: Context): List<AuthorityBean>? {
        val holder = PreferenceUtil.find(context, USER_AUTHORITY, AuthorityHolder::class.java)
        return holder?.list
    }

    fun deleteAuthority(context: Context) {
        PreferenceUtil.deleteAll(context, AuthorityHolder::class.java)
    }

    data class AuthorityHolder(val list: List<AuthorityBean>) : Serializable

}