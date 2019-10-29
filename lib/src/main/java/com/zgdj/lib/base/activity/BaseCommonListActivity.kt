package com.zgdj.lib.base.activity

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.jhj.slimadapter.SlimAdapter
import com.jhj.slimadapter.holder.ViewInjector
import com.jhj.slimadapter.itemdecoration.LineItemDecoration
import com.zgdj.lib.R
import com.zgdj.lib.extention.closeKeyboard
import com.zgdj.lib.extention.loadingDialog
import com.zgdj.lib.extention.openKeyboard
import com.zgdj.lib.extention.toArrayList
import kotlinx.android.synthetic.main.activity_recyclerview.*
import kotlinx.android.synthetic.main.layout_empty_view.*
import kotlinx.android.synthetic.main.layout_search_bar.*
import kotlinx.android.synthetic.main.layout_top_bar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*

abstract class BaseCommonListActivity<T> : BaseActivity() {

    abstract val topbar: String
    abstract val itemLayoutRes: Int

    //输入搜索
    open val inputSearch = false
    //分割线
    open val hasSplitLine = true
    //输入框变化就开始搜索
    open val filterFunc: (T, String) -> Boolean = { _, _ -> true }

    lateinit var adapterLocal: SlimAdapter
    var mRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)
        initialize()
        tv_top_bar_title?.text = topbar
        iv_top_bar_back?.visibility = View.VISIBLE
        iv_top_bar_back?.onClick {
            closeKeyboard(iv_top_bar_back)
            onBackPressed()
        }
        val dataList = getDataList()
        if (dataList.isNullOrEmpty()) {
            include_empty_view.visibility = View.VISIBLE
        } else {
            include_empty_view.visibility = View.GONE
        }
        tv_refresh.onClick {
            launch(Dispatchers.Main) {
                val dialog = loadingDialog()
                delay(800)
                dialog.dismiss()
                toast("没有数据")
            }
        }

        //输入搜索
        if (inputSearch) {
            layout_search_bar.visibility = View.VISIBLE
            inputSearch(dataList)
        } else {
            layout_search_bar.visibility = View.GONE
        }

        adapterLocal = initAdapter(dataList)
        if (hasSplitLine) {
            recyclerView.addItemDecoration(LineItemDecoration())
        }
        mRecyclerView = recyclerView
    }

    private fun initAdapter(dataList: List<T>): SlimAdapter {

        recyclerView.setOnTouchListener { _, _ ->
            closeKeyboard(et_search_input)
            if (et_search_input.text.isNullOrBlank()) {
                layout_search_mark.visibility = View.VISIBLE
            }
            return@setOnTouchListener false
        }

        return SlimAdapter.creator()
                .setGenericActualType(getTClazz())
                .register<T>(itemLayoutRes) { injector, bean, position ->
                    itemViewConvert(this, injector, bean, position)
                }
                .attachTo(recyclerView)
                .setDataList(dataList.toArrayList())
    }

    /**
     * 输入搜索
     */
    private fun inputSearch(dataList: List<T>) {
        layout_search_mark.setOnTouchListener { _, _ ->
            layout_search_mark.visibility = View.GONE
            openKeyboard(et_search_input)
            return@setOnTouchListener false
        }

        //输入搜索
        et_search_input.addTextChangedListener(object : TextWatcher {

            var repeatStr: String? = null

            override fun afterTextChanged(p0: Editable?) {

                val searchText: String? = p0?.toString()
                if (searchText == repeatStr) { //避免多次调用
                    return
                }
                repeatStr = searchText
                filterList(dataList, searchText)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
    }

    private fun filterList(dataList: List<T>, searchText: String?) {
        var list = dataList
        if (!searchText.isNullOrBlank()) {
            list = ArrayList()
            for (item in dataList) {
                if (filterFunc(item, searchText)) {
                    list.add(item)
                }
            }
        }
        adapterLocal.setDataList(list.toArrayList())
    }


    /**
     * 获取泛参数实际类型
     */
    private fun getTClazz(): Type {
        //获取当前类带有泛型的父类
        val clazz: Type? = this.javaClass.genericSuperclass
        return if (clazz is ParameterizedType) {
            //获取父类的泛型参数（参数可能有多个，获取第一个）
            clazz.actualTypeArguments[0]
        } else {
            throw IllegalArgumentException()
        }
    }

    open fun initialize() {}
    abstract fun getDataList(): List<T>
    abstract fun itemViewConvert(adapter: SlimAdapter, injector: ViewInjector, bean: T, position: Int)
}