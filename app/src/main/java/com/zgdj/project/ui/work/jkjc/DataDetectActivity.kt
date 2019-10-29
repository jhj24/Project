package com.zgdj.project.ui.work.jkjc

import android.support.v4.app.Fragment
import com.zgdj.lib.base.activity.BaseFragmentActivity

class DataDetectActivity : BaseFragmentActivity() {
    override val title: String
        get() = "检测数据"
    override val fragmentList: List<Pair<Fragment, String>>
        get() = listOf(
            DataDetectFragment() to "1#泵组",
            DataDetectFragment() to "2#泵组",
            DataDetectFragment() to "3#泵组",
            DataDetectFragment() to "4#泵组",
            DataDetectFragment() to "5#泵组"
        )
}