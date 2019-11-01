package com.zgdj.project.ui

import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.zgdj.lib.base.activity.BaseActivity
import com.zgdj.project.R
import kotlinx.android.synthetic.main.activity_description.*
import org.jetbrains.anko.dimen
import org.jetbrains.anko.dip
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick

class DescriptionActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)
        find<TextView>(R.id.tv_top_bar_title).text = "工程概述"
        find<ImageView>(R.id.iv_top_bar_back).visibility = View.VISIBLE
        find<ImageView>(R.id.iv_top_bar_back).onClick {
            onBackPressed()
        }
        val imageHeight = dip(220)
        val titleBarHeight = dimen(R.dimen.statusbar_titlebar_view_height)
        val topBar = find<View>(R.id.top_bar_background)
        topBar.alpha = 0f

        scroll_view.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { p0, p1, p2, p3, p4 ->
            if (p2 - dip(20) < imageHeight - titleBarHeight && p2 > imageHeight - titleBarHeight) {
                val percent = (p2 - (imageHeight - titleBarHeight).toFloat()) / dip(20)
                topBar.alpha = percent
            } else if (p2 < imageHeight - titleBarHeight) {
                topBar.alpha = 0f
            } else if (p2 - dip(20) > imageHeight - titleBarHeight) {
                topBar.alpha = 1f
            }
        })

        tv_standard_one.text = textSpan(SpannableString(tv_standard_one.text), 5)
        tv_standard_two.text = textSpan(SpannableString(tv_standard_two.text), 5)
        tv_scale_one.text = textSpan(SpannableString(tv_scale_one.text), 9)
        tv_scale_two.text = textSpan(SpannableString(tv_scale_two.text), 9)
        tv_scale_three.text = textSpan(SpannableString(tv_scale_three.text), 5)
        tv_scale_four.text = textSpan(SpannableString(tv_scale_four.text), 5)


    }

    fun textSpan(span: SpannableString, end: Int): SpannableString {
        val textSizeSpan = RelativeSizeSpan(1.15f)
        val textColorSpan = ForegroundColorSpan(0xff000000.toInt())
        span.setSpan(textSizeSpan, 0, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        span.setSpan(textColorSpan, 0, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        return span
    }

}