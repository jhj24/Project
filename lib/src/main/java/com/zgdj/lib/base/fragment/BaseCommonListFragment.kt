package com.zgdj.lib.base.fragment

import android.os.Bundle
import android.view.View
import com.zgdj.lib.base.activity.BaseListFragment
import com.zgdj.lib.extention.loadingDialog
import kotlinx.android.synthetic.main.layout_empty_view.view.*
import kotlinx.android.synthetic.main.layout_search_bar.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk27.coroutines.onClick

abstract class BaseCommonListFragment<T> : BaseListFragment<T>() {

    //输入框变化就开始搜索
    open val inputSearchFunc: (T, String) -> Boolean = { _, _ -> true }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.tv_refresh.onClick {
            launch(Dispatchers.Main) {
                val dialog = view.context.loadingDialog()
                delay(800)
                dialog.dismiss()
                refresh()
            }
        }

    }

    override fun refresh() {
        val list = dataList.filter { inputSearchFunc(it, mParentView.et_search_input.text.toString()) }
        dataListEmpty(list)
    }

    override fun inputSearch(search: String?) {
        refresh()
    }
}