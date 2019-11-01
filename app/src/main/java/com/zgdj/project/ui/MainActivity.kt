package com.zgdj.project.ui

import android.Manifest
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.zgdj.lib.base.activity.BaseActivity
import com.zgdj.lib.extention.getResColor
import com.zgdj.lib.extention.getResDrawable
import com.zgdj.lib.utils.TextColorUtils
import com.zgdj.lib.utils.bus.LiveDataBus
import com.zgdj.project.R
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.dip
import org.jetbrains.anko.textColor
import org.jetbrains.anko.wrapContent

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /* button.setOnClickListener {
             ARouter.getInstance().build("/work/test").navigation()
         }*/
        requestPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE) {}
        navigation_view.itemTextColor =
                TextColorUtils.selected(getResColor(R.color.menu_selector), getResColor(R.color.menu_normal))
        navigation_view.itemIconTintList =
                TextColorUtils.selected(getResColor(R.color.menu_selector), getResColor(R.color.menu_normal))
        val navHostFragment = fragment as NavHostFragment
        NavigationUI.setupWithNavController(navigation_view, navHostFragment.navController)

        redDot(5)

        LiveDataBus.get()
                .with("number", Int::class.java)
                .observe(this, Observer {
                    if (it != null) {
                        redDot(it)
                    }
                })
    }


    private fun redDot(num: Int) {
        val menuView = navigation_view.getChildAt(0)
        if (menuView != null && menuView is BottomNavigationMenuView) {
            val messageItemView = menuView.getChildAt(2) as BottomNavigationItemView
            if (messageItemView.childCount > 1) {
                for (i in 2 until messageItemView.childCount) {
                    messageItemView.removeViewAt(i)
                }
            }
            val textView = TextView(this).apply {
                text = num.toString()
                gravity = Gravity.CENTER
                textColor = getResColor(R.color.white)
                textSize = 10f
                minWidth = dip(18)
                minHeight = dip(10)
                backgroundDrawable = getResDrawable(R.drawable.shape_message_dot)
                val layoutParams = FrameLayout.LayoutParams(wrapContent, wrapContent)
                layoutParams.topMargin = dip(6)
                layoutParams.leftMargin = dip(15)
                layoutParams.gravity = Gravity.CENTER_HORIZONTAL
                setLayoutParams(layoutParams)
            }
            if (num > 0) {
                messageItemView.addView(textView)
            }
        }
    }

}
