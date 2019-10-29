package com.zgdj.lib.base.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.zgdj.lib.R
import com.zgdj.lib.config.Config
import com.zgdj.lib.extention.image
import com.zgdj.lib.utils.StatusBarUtil
import com.zgdj.lib.utils.permissions.PermissionsCheck
import com.zgdj.lib.utils.permissions.PermissionsUtil
import kotlinx.android.synthetic.main.layout_top_bar.*
import org.jetbrains.anko.sdk27.coroutines.onClick
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

    fun initTopBar(title: String, isVisibility: Int = View.GONE) {
        tv_top_bar_title?.text = title
        iv_top_bar_back?.visibility = isVisibility
        iv_top_bar_back?.onClick {
            finish()
        }
    }

    fun initTopBar(title: String, right: String, body: () -> Unit) {
        initTopBar(title, View.VISIBLE)
        if (!right.isBlank()) {
            tv_top_bar_right?.text = right
            tv_top_bar_right?.visibility = View.VISIBLE
            tv_top_bar_right?.onClick {
                body()
            }
        }

    }

    fun initTopBar(title: String, imgRes: Int, body: () -> Unit) {
        initTopBar(title, View.VISIBLE)
        iv_top_bar_right?.let {
            image(imgRes, it)
            it.visibility = View.VISIBLE
            it.onClick {
                body()
            }
        }
    }

    fun topBarRightText(text: String, body: () -> Unit) {
        tv_top_bar_right.visibility = View.VISIBLE
        tv_top_bar_right.text = text
        tv_top_bar_right.onClick {
            body()
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