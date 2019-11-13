package com.zgdj.lib.base.activity

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import com.zgdj.lib.R
import com.zgdj.lib.extention.glide
import com.zgdj.lib.extention.inflater
import kotlinx.android.synthetic.main.activity_default_top_bar.*
import kotlinx.android.synthetic.main.layout_top_bar.*
import org.jetbrains.anko.sdk27.coroutines.onClick

abstract class DefaultTopBarActivity : BaseActivity() {


    abstract val title: String

    val topBar: View
        get() = top_bar_background

    val statusBar: View
        get() = status_bar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    fun topBarRightText(text: String, body: () -> Unit) {
        tv_top_bar_right.visibility = View.VISIBLE
        tv_top_bar_right.text = text
        tv_top_bar_right.onClick {
            body()
        }
    }

    fun topBarRightImage(image: Int, color: Int? = null, body: () -> Unit) {
        if (color != null) iv_top_bar_right.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        iv_top_bar_right.visibility = View.VISIBLE
        iv_top_bar_right.glide(image)
        iv_top_bar_right.onClick {
            body()
        }
    }


    override fun setContentView(layoutResID: Int) {
        super.setContentView(R.layout.activity_default_top_bar)
        val view = inflater.inflate(layoutResID, frame_layout_content, false)
        frame_layout_content.addView(view)
        tv_top_bar_title.text = title
        iv_top_bar_back.visibility = View.VISIBLE
        iv_top_bar_back.onClick {
            onBackPressed()
        }
    }
}