package com.zgdj.lib.base.activity

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.View
import com.zgdj.lib.R
import kotlinx.android.synthetic.main.activity_base_fragment.*

abstract class BaseFragmentActivity : DefaultTopBarActivity() {

    var currentIndex = 0

    var fragmentList: List<Pair<Fragment, String>> = listOf()
        set(value) {
            field = value
            tab_layout.addOnTabSelectedListener(object : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
                override fun onTabReselected(p0: TabLayout.Tab?) {
                    onTabSelected(p0)
                }

                override fun onTabUnselected(p0: TabLayout.Tab?) {

                }

                override fun onTabSelected(p0: TabLayout.Tab?) {
                    currentIndex = p0?.position ?: currentIndex
                }
            })


            view_pager.adapter = TabPageAdapter(supportFragmentManager)
            view_pager.offscreenPageLimit = value.size
            tab_layout.setupWithViewPager(view_pager)
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_fragment)
    }


    inner class TabPageAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
        override fun getItem(p0: Int): Fragment {
            return fragmentList[p0].first
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return fragmentList[position].second
        }
    }
}