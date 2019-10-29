package com.zgdj.project.ui.funfragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.View
import com.zgdj.lib.base.fragment.BaseFragment
import com.zgdj.lib.config.Config
import com.zgdj.lib.utils.StatusBarUtil
import com.zgdj.project.ChildMessageFragment
import com.zgdj.project.R
import kotlinx.android.synthetic.main.fragment_message.view.*
import kotlinx.android.synthetic.main.layout_top_bar_main_fragmen.view.*
import org.jetbrains.anko.support.v4.withArguments

class MessageFragment : BaseFragment() {
    override val layoutRes: Int
        get() = R.layout.fragment_message


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        StatusBarUtil.setLightMode(mActivity)
        view.tv_top_bar_title.text = "消息"

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

    private fun getFragment(type: Int): ChildMessageFragment {
        val fragment = ChildMessageFragment()
        fragment.withArguments(Config.TYPE to type)
        return fragment
    }
}