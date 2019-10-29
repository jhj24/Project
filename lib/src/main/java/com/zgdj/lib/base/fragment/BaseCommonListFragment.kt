package com.zgdj.lib.base.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.jhj.httplibrary.model.HttpParams
import com.jhj.slimadapter.SlimAdapter
import com.jhj.slimadapter.holder.ViewInjector
import com.jhj.slimadapter.itemdecoration.LineItemDecoration
import com.zgdj.lib.R
import com.zgdj.lib.extention.loadingDialog
import com.zgdj.lib.extention.toArrayList
import com.zgdj.lib.extention.closeKeyboard
import com.zgdj.lib.extention.openKeyboard
import kotlinx.android.synthetic.main.fragment_refreshview.*
import kotlinx.android.synthetic.main.fragment_refreshview.view.*
import kotlinx.android.synthetic.main.layout_empty_view.view.*
import kotlinx.android.synthetic.main.layout_search_bar.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*

abstract class BaseCommonListFragment<T> : BaseFragment() {

    abstract val itemLayoutRes: Int

    //请求参数
    open val httpParams: HttpParams = HttpParams()
    //输入搜索
    open val inputSearch = false
    //分割线
    open val hasSplitLine = true
    //输入框变化就开始搜索
    open val filterFunc: (T, String) -> Boolean = { t, s -> true }

    lateinit var adapterLocal: SlimAdapter


    override val layoutRes: Int
        get() = R.layout.fragment_refreshview

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataList = getDataList()
        initAdapter(view, dataList)
        if (hasSplitLine) {
            recyclerView.addItemDecoration(LineItemDecoration())
        }

        if (dataList.isNullOrEmpty()) {
            view.include_empty_view.visibility = View.VISIBLE
        } else {
            view.include_empty_view.visibility = View.GONE
        }
        view.tv_refresh.onClick {
            launch(Dispatchers.Main) {
                val dialog = context?.loadingDialog()
                delay(800)
                dialog?.dismiss()
                context?.toast("没有数据")
            }
        }

        //输入搜索
        if (inputSearch) {
            view.layout_search_bar.visibility = View.VISIBLE
            inputSearch(view, dataList)
        } else {
            view.layout_search_bar.visibility = View.GONE
        }
    }


    private fun initAdapter(view: View, dataList: List<T>) {

        view.recyclerView.setOnTouchListener { _, _ ->
            closeKeyboard(view.et_search_input)
            if (view.et_search_input.text.isNullOrBlank()) {
                view.layout_search_mark.visibility = View.VISIBLE
            }
            return@setOnTouchListener false
        }

        adapterLocal = SlimAdapter.creator()
            .setGenericActualType(getTClazz())
            .register<T>(itemLayoutRes) { injector, bean, position ->
                itemViewConvert(this, injector, bean, position)
            }
            .attachTo(view.recyclerView)
            .setDataList(dataList.toArrayList())
    }

    /**
     * 输入搜索
     */
    private fun inputSearch(view: View, dataList: List<T>) {
        view.layout_search_mark.setOnTouchListener { _, _ ->
            view.layout_search_mark.visibility = View.GONE
            openKeyboard(view.et_search_input)
            return@setOnTouchListener false
        }

        //输入搜索
        view.et_search_input.addTextChangedListener(object : TextWatcher {

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


    abstract fun itemViewConvert(adapter: SlimAdapter, injector: ViewInjector, bean: T, position: Int)

    abstract fun getDataList(): List<T>

}