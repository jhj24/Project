package com.zgdj.project.ui.funfragment

import android.os.Bundle
import android.view.View
import com.zgdj.lib.base.fragment.BaseFragment
import com.zgdj.lib.utils.StatusBarUtil
import com.zgdj.project.R

class MineFragment : BaseFragment() {


    override val layoutRes: Int
        get() = R.layout.fragment_mine

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        StatusBarUtil.setDarkMode(mActivity)
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    private fun getVersion(): String {
        return try {
            val manager = mActivity.packageManager
            val info = manager.getPackageInfo(mActivity.packageName, 0)
            info.versionName
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}