package com.zgdj.project.ui.funfragment

import android.os.Bundle
import android.view.View
import com.zgdj.lib.base.fragment.BaseFragment
import com.zgdj.lib.utils.StatusBarUtil
import com.zgdj.project.R
import com.zgdj.project.ui.DescriptionActivity
import kotlinx.android.synthetic.main.fragment_mine.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.startActivity

class MineFragment : BaseFragment() {


    override val layoutRes: Int
        get() = R.layout.fragment_mine

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        StatusBarUtil.setDarkMode(mActivity)
        view.iv_avatar.onClick {
            startActivity<DescriptionActivity>()
        }
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