package com.zgdj.lib.utils

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

object FragmentManagerUtils {

    fun addFragment(activity: FragmentActivity, fragment: Fragment, layoutId: Int, bundle: Bundle?) {
        val fragmentManager = activity.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        if (bundle != null){
            if (fragment.arguments == null){
                fragment.arguments = bundle
            }else {
                fragment.arguments?.putAll(bundle)
            }
        }
        fragmentTransaction.add(layoutId, fragment)
        fragmentTransaction.commitAllowingStateLoss()
    }

    fun replaceFragment(activity: FragmentActivity, fragment: Fragment, layoutId: Int, bundle: Bundle?) {
        val fragmentManager = activity.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        if (bundle != null){
            if (fragment.arguments == null){
                fragment.arguments = bundle
            }else {
                fragment.arguments?.putAll(bundle)
            }
        }
        fragmentTransaction.replace(layoutId, fragment)
        //fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commitAllowingStateLoss()
    }
}