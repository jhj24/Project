package com.zgdj.lib.extention

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import com.zgdj.lib.utils.permissions.PermissionsCheck
import com.zgdj.lib.utils.permissions.PermissionsUtil
import org.jetbrains.anko.toast


fun Fragment.requestPermissions(vararg permissions: String, body: () -> Unit) {
     activity?.requestPermissions(*permissions, body = body)
}

fun Activity.requestPermissions(vararg permissions: String, body: () -> Unit) {
    PermissionsCheck.with(this)
            .requestPermissions(*permissions)
            .onPermissionsResult { deniedPermissions, allPermissions ->
                if (deniedPermissions.isEmpty()) {
                    body()
                } else {
                    val infoList = arrayListOf<String>()
                    deniedPermissions.forEach {
                        if (PermissionsUtil.permissionsMap.containsKey(it)) {
                            if (!infoList.contains(PermissionsUtil.permissionsMap[it])) {
                                toast("${PermissionsUtil.permissionsMap[it]}权限请求失败")
                            }
                            infoList.add(PermissionsUtil.permissionsMap[it] ?: "")
                            return@forEach
                        }
                    }
                }
            }
}