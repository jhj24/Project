package com.zgdj.lib.base.fragment

import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseFragment : Fragment() {

    lateinit var mParentView: View

    lateinit var mActivity: FragmentActivity

    @get:LayoutRes
    abstract val layoutRes: Int

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = context as FragmentActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutRes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mParentView = view
    }


    class TabPageAdapter(fragmentManager: FragmentManager, val fragmentList: List<Pair<Fragment, String>>) : FragmentPagerAdapter(fragmentManager) {
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