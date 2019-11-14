package com.zgdj.lib.bean

import java.io.Serializable

data class U1serBean(
    val key: Int,
    val username: String,
    val name: String,
    val password: String,//md5 加密后的密码
    val gender: Int, //0女，1男
    val filepath: String?,
    val avatar: String?,

    val cate: String,
    val group: String,
    val phone: String,
    val tele: String,
    val email: String,
    val token: String,
    val remark: String,
    val depart: String,
    val position: String,
    val signature_path: String,
    val ip: String,
    val loginTime: Long,
    val pwd: String
) : Serializable {


}