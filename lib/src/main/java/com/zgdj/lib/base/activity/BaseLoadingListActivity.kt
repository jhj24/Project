package com.zgdj.lib.base.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.jhj.httplibrary.model.HttpParams
import com.jhj.slimadapter.SlimAdapter
import com.jhj.slimadapter.holder.ViewInjector
import com.jhj.slimadapter.itemdecoration.LineItemDecoration
import com.zgdj.lib.R
import com.zgdj.lib.extention.closeKeyboard
import com.zgdj.lib.extention.httpPost
import com.zgdj.lib.extention.openKeyboard
import com.zgdj.lib.extention.toArrayList
import com.zgdj.lib.net.callback.DataDialogHttpCallback
import kotlinx.android.synthetic.main.activity_recyclerview.*
import kotlinx.android.synthetic.main.layout_empty_view.*
import kotlinx.android.synthetic.main.layout_search_bar.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*

abstract class BaseLoadingListActivity<T> : DefaultTopBarActivity() {

    abstract val url: String
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

    private var dataList: List<T>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)
        initialize()
        initAdapter()
        //分割线
        if (hasSplitLine) {
            recyclerView.addItemDecoration(LineItemDecoration())
        }

        httpRequest()
        //输入搜索
        if (inputSearch) {
            layout_search_bar.visibility = View.VISIBLE
            inputSearch()
        } else {
            layout_search_bar.visibility = View.GONE
        }

        tv_refresh.setOnClickListener {
            include_empty_view.visibility = View.GONE
            httpRequest()
        }

    }


    private fun initAdapter() {

        recyclerView.setOnTouchListener { v, _ ->
            closeKeyboard(v)
            if (et_search_input.text.isNullOrBlank()) {
                layout_search_mark.visibility = View.VISIBLE
            }
            return@setOnTouchListener false
        }

        adapterLocal = SlimAdapter.creator()
                .setGenericActualType(getClazz())
                .register<T>(itemLayoutRes) { injector, bean, position ->
                    itemViewConvert(this, injector, bean, position)
                }
                .attachTo(recyclerView)
    }

    fun refresh() {
        httpRequest()
    }


    private fun httpRequest() {

        httpPost(url)
                .addParams(httpParams)
                .enqueue(object : DataDialogHttpCallback<List<T>>(this) {

                    var isSuccess = false

                    override val clazzType: Type
                        get() = type(List::class.java, getClazz())

                    override fun onSuccess(data: List<T>?, resultType: ResultType) {
                        isSuccess = true
                        dataList = data
                        formatDataList(dataList)
                        adapterLocal.setDataList(dataList.toArrayList())
                    }

                    override fun onFailure(msg: String) {
                        super.onFailure(msg)
                        isSuccess = false
                    }

                    override fun onFinish() {
                        super.onFinish()
                        if (isSuccess) {
                            if (adapterLocal.isDataListNotEmpty()) {
                                include_empty_view.visibility = View.GONE
                            } else {
                                include_empty_view.visibility = View.VISIBLE
                                tv_refresh_reason.text = "没有数据"
                            }
                        } else {
                            include_empty_view.visibility = View.VISIBLE
                            tv_refresh_reason.text = "查询失败"
                        }
                    }
                })
    }

    /**
     * 输入搜索
     */
    private fun inputSearch() {
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
                filterList(searchText)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
    }

    private fun filterList(searchText: String?) {
        var dataList: List<T>? = this.dataList
        if (!searchText.isNullOrBlank() && this.dataList != null) {
            dataList = ArrayList()
            for (item in this.dataList.orEmpty()) {
                if (filterFunc(item, searchText)) {
                    dataList.add(item)
                }
            }
        }
        adapterLocal.setDataList(dataList.toArrayList())
    }

    /**
     * 获取泛参数实际类型
     */
    private fun getClazz(): Type {
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

    open fun formatDataList(dataList: List<T>?): List<T>? {
        return dataList
    }

    abstract fun itemViewConvert(adapter: SlimAdapter, injector: ViewInjector, bean: T, position: Int)

}