package com.zgdj.lib.bean

import android.support.annotation.DrawableRes
import com.zgdj.lib.extention.filePath
import java.io.Serializable

/**
 * 用户信息
 */
data class UserBean(
        val key: Int = 0,
        val username: String = "",
        val name: String = "",
        val password: String = "",//md5 加密后的密码
        val gender: Int = 0, //0女，1男
        val filepath: String? = "",
        val avatar: String? = "",

        val cate: String = "",
        val group: String = "",
        val phone: String = "",
        val tele: String = "",
        val email: String = "",
        val token: String = "",
        val remark: String = "",
        val depart: String = "",
        val position: String = "",
        val signature_path: String = "",
        val ip: String = "",
        val loginTime: Long = 0,
        val pwd: String = ""
) : Serializable

/**
 * 文件类
 */
data class FileBean(
        var key: Int = 0,
        var filename: String? = null,
        private val src: String? = null,
        private var url: String? = null
) : Serializable {
    val formatPath: String
        get() = (src ?: url).filePath

}

/**
 * 操作权限
 */
data class AuthorityBean(
        var path: String? = null, // /admin/group/editNode
        var authority: Int = 0,   //  1
        var title: String? = null // 新增编辑组织机构树节点
) : Serializable


/**
 * 大坝管理
 */
data class DabaBean(
        val key: Int,
        val daba_num: String = "",
        val title: String = "",
        val type_name: String = "",
        val type: Int,
        val actual_con_start: String?,
        val actual_con_end: String?,
        val actual_con_period: String?,
        val plan_con_start: String?,
        val plan_con_end: String?,
        val plan_con_period: String?,
        val lon_lat_area: String?,
        var isSelector: Boolean
) : Serializable

/**
 * 自定义底部弹出窗
 */
data class BottomDialogBean(
        val title: String,
        val authority: String? = null,
        @DrawableRes val path: Int
)

/**
 * id—value
 */
data class ID_VALUE(
        val id: Int,
        val value: String
) : Serializable