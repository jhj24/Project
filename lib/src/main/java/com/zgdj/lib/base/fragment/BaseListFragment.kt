package com.zgdj.lib.base.activity

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import com.jhj.httplibrary.model.HttpParams
import com.jhj.slimadapter.SlimAdapter
import com.jhj.slimadapter.holder.ViewInjector
import com.jhj.slimadapter.itemdecoration.LineItemDecoration
import com.zgdj.lib.R
import com.zgdj.lib.base.fragment.BaseFragment
import com.zgdj.lib.extention.closeKeyboard
import com.zgdj.lib.extention.openKeyboard
import kotlinx.android.synthetic.main.fragment_recclerview_refresh.view.*
import kotlinx.android.synthetic.main.layout_empty_view.view.*
import kotlinx.android.synthetic.main.layout_search_bar.view.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

abstract class BaseListFragment<T> : BaseFragment() {

    abstract val itemLayoutRes: Int

    lateinit var adapterLocal: SlimAdapter

    //输入搜索
    open val inputSearch = false
    //分割线
    open val hasSplitLine = true

    override val layoutRes: Int
        get() = R.layout.fragment_recclerview_refresh


    val mRecyclerView: RecyclerView
        get() = mParentView.recyclerView

    var dataList: List<T> = listOf()
        set(value) {
            field = value
            dataListEmpty(value)
        }

    fun dataListEmpty(value: List<T>) {
        if (value.isNullOrEmpty()) {
            mParentView.include_empty_view.visibility = View.VISIBLE
            mParentView.tv_refresh_reason.text = "没有数据"
        } else {
            mParentView.include_empty_view.visibility = View.GONE
        }
        adapterLocal.setDataList(value)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        view.smartRefreshLayout.setEnableLoadMore(false)
        view.smartRefreshLayout.setEnableRefresh(false)
        //输入搜索
        if (inputSearch) {
            view.layout_search_bar.visibility = View.VISIBLE
            inputSearch()
        } else {
            view.layout_search_bar.visibility = View.GONE
        }

        if (hasSplitLine) view.recyclerView.addItemDecoration(LineItemDecoration())
        initAdapter()
    }

    private fun initAdapter() {

        mParentView.recyclerView.setOnTouchListener { v, _ ->
            v.closeKeyboard()
            if (mParentView.et_search_input.text.isNullOrBlank()) {
                mParentView.layout_search_mark.visibility = View.VISIBLE
            }
            return@setOnTouchListener false
        }

        adapterLocal = SlimAdapter.creator()
                .setGenericActualType(genericType())
                .register<T>(itemLayoutRes) { injector, bean, position ->
                    itemViewConvert(this, injector, bean, position)
                }
                .attachTo(mParentView.recyclerView)
    }


    /**
     * 输入搜索
     */
    private fun inputSearch() {
        var repeatStr: String? = null
        mParentView.layout_search_mark.setOnTouchListener { _, _ ->
            mParentView.layout_search_mark.visibility = View.GONE
            mParentView.et_search_input.openKeyboard()
            return@setOnTouchListener false
        }

        //输入搜索
        mParentView.et_search_input.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {

                val searchText: String? = p0?.toString()
                if (searchText == repeatStr) { //避免多次调用
                    return
                }
                repeatStr = searchText
                inputSearch(searchText)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
    }


    /**
     * 获取泛参数实际类型
     */
    fun genericType(): Type {
        //获取当前类带有泛型的父类
        val clazz: Type? = this.javaClass.genericSuperclass
        return if (clazz is ParameterizedType) {
            //获取父类的泛型参数（参数可能有多个，获取第一个）
            clazz.actualTypeArguments[0]
        } else {
            throw IllegalArgumentException()
        }
    }


    abstract fun refresh()
    abstract fun inputSearch(search: String?)
    abstract fun itemViewConvert(adapter: SlimAdapter, injector: ViewInjector, bean: T, position: Int)
    open fun initialize() {}
}