package com.zgdj.project.ui

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.zgdj.lib.base.activity.BaseActivity
import com.zgdj.lib.extention.getResColor
import com.zgdj.lib.utils.TextColorUtils
import com.zgdj.project.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /* button.setOnClickListener {
             ARouter.getInstance().build("/work/test").navigation()
         }*/

        navigation_view.itemTextColor = TextColorUtils.selected(
                getResColor(R.color.menu_selector), getResColor(
                R.color.menu_normal
        )
        )
        navigation_view.itemIconTintList = TextColorUtils.selected(
                getResColor(R.color.menu_selector), getResColor(
                R.color.menu_normal
        )
        )
        val navHostFragment = fragment as NavHostFragment
        NavigationUI.setupWithNavController(navigation_view, navHostFragment.navController)


    }
}
