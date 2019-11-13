package com.zgdj.lib.base.activity

import android.os.Bundle
import android.view.Gravity
import com.zgdj.lib.extention.loadingDialog
import com.zgdj.lib.extention.toArrayList
import kotlinx.android.synthetic.main.activity_recyclerview_refresh.*
import kotlinx.android.synthetic.main.layout_empty_view.*
import kotlinx.android.synthetic.main.layout_search_bar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast

abstract class BaseCommonListActivity<T> : BaseListActivity<T>() {

    //输入框变化就开始搜索
    open val inputSearchFunc: (T, String) -> Boolean = { _, _ -> true }
    open val filterFunc: (T) -> Boolean = { _ -> true }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tv_refresh.onClick {
            launch(Dispatchers.Main) {
                val dialog = loadingDialog()
                delay(800)
                dialog.dismiss()
                refresh()
            }
        }
    }

    override fun refresh() {
        val list = dataList.filter { inputSearchFunc(it, et_search_input.text.toString()) && filterFunc(it) }
        dataListEmpty(list)
    }

    override fun inputSearch(search: String?) {
        refresh()
    }

    override fun filter() {
        refresh()
        drawer_layout.closeDrawer(Gravity.END)
    }
}