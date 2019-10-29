package com.zgdj.lib.base.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.zgdj.lib.R
import com.zgdj.lib.config.Config
import com.zgdj.lib.utils.StatusBarUtil
import com.zgdj.lib.utils.permissions.PermissionsCheck
import com.zgdj.lib.utils.permissions.PermissionsUtil
import org.jetbrains.anko.toast

abstract class BaseActivity : AppCompatActivity() {

    var key: Int = -1
    open val isDefaultStatusBar = true
    open val isNeedKey = false
    open val isOrientationPortrait = true

    companion object {
        const val DEFAULT_KEY = -0x00000100
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isOrientationPortrait) requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT //竖屏
        if (isNeedKey) {
            key = intent.getIntExtra(Config.KEY, DEFAULT_KEY)
            if (key == DEFAULT_KEY) {
                throw IllegalAccessException("传参错误")
            }
        }
    }


    override fun onResume() {
        super.onResume()
        //JPushInterface.onResume(this)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
                        or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
        )
        if (isDefaultStatusBar) {
            StatusBarUtil.setTransparentForImageViewInFragment(this, null)
        }
    }


    fun requestPermissions(vararg permissions: String, body: () -> Unit) {
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

    fun closeActivity() {
        finish()
        overridePendingTransition(R.anim.activity_alpha_in, R.anim.activity_alpha_out)
    }


    open fun onActivityResult(intent: Intent) {}

    override fun onPause() {
        super.onPause()
        //JPushInterface.onPause(this)
    }

}