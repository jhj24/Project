package com.zgdj.project.ui

import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import com.zgdj.lib.base.activity.BaseActivity
import com.zgdj.project.R
import kotlinx.android.synthetic.main.activity_description.*
import org.jetbrains.anko.dimen
import org.jetbrains.anko.dip

class DescriptionActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)
        val imageHeight = dip(240)
        val titleBarHeight = dimen(R.dimen.statusbar_titlebar_view_height)
        top_bar_background.background.alpha = 0

        scroll_view.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { p0, p1, p2, p3, p4 ->
            if (p2 - dip(20) < imageHeight - titleBarHeight && p2 > imageHeight - titleBarHeight) {
                val percent = (p2 - (imageHeight - titleBarHeight).toFloat()) / dip(20)
                top_bar_background.background.alpha = (255 * percent).toInt()
            } else if (p2 < imageHeight - titleBarHeight) {
                top_bar_background.background.alpha = 0
            } else if (p2 - dip(20) > imageHeight - titleBarHeight) {
                top_bar_background.background.alpha = 255
            }
        })


    }

}