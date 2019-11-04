package com.zgdj.project.ui.funfragment

import android.Manifest
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.View
import com.zgdj.lib.base.fragment.BaseFragment
import com.zgdj.lib.config.Config
import com.zgdj.lib.extention.readAssets
import com.zgdj.lib.utils.StatusBarUtil
import com.zgdj.project.R
import com.zgdj.project.ui.MainActivity
import com.zgdj.project.ui.QRCodeActivity
import com.zgdj.project.ui.message.ChildMessageFragment
import com.zgdj.project.ui.message.ChildMessageFragment1
import kotlinx.android.synthetic.main.fragment_message.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.withArguments

class MessageFragment : BaseFragment() {
    override val layoutRes: Int
        get() = R.layout.fragment_message


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.tv_main_title.text = "消息"
        view.iv_main_scan.onClick {
            (mActivity as MainActivity).requestPermissions(Manifest.permission.CAMERA) {
                startActivity<QRCodeActivity>()
            }
        }
        StatusBarUtil.setLightMode(mActivity)


        view.tab_layout.addOnTabSelectedListener(object : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
            override fun onTabReselected(p0: TabLayout.Tab?) {
                onTabSelected(p0)
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {

            }
        })
        //状态，1未提交，2审批中，3已完成，-1被驳回
        val fragmentList = arrayListOf(
                getFragment(0) to "未读",
                getFragment(1) to "已读"
        )
        view.view_pager.offscreenPageLimit = 2
        view.view_pager.adapter = TabPageAdapter(childFragmentManager, fragmentList)
        view.tab_layout.setupWithViewPager(view.view_pager)
    }

    private fun getFragment(type: Int): Fragment {
        val str = if (type == 0) {
            context?.readAssets("data_0.json")
        } else {
            null
        }

        val fragment = if (type == 0) ChildMessageFragment() else ChildMessageFragment1()
        fragment.withArguments(Config.TYPE to type, Config.DATA to str)
        return fragment
    }
}