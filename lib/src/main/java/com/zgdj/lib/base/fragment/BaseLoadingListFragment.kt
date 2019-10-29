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
import com.zgdj.lib.extention.closeKeyboard
import com.zgdj.lib.extention.openKeyboard
import com.zgdj.lib.extention.toArrayList
import com.zgdj.lib.net.callback.DataDialogHttpCallback
import com.zgdj.lib.extention.httpPost
import kotlinx.android.synthetic.main.fragment_refreshview.*
import kotlinx.android.synthetic.main.fragment_refreshview.view.*
import kotlinx.android.synthetic.main.layout_empty_view.view.*
import kotlinx.android.synthetic.main.layout_search_bar.view.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

abstract class BaseLoadingListFragment<T> : BaseFragment() {

    abstract val url: String
    abstract val itemLayoutRes: Int

    //请求参数
    open val httpParams: HttpParams = HttpParams()
    //搜索方式 0-本地搜索，网络搜索
    open val inputType = 0
    //输入搜索
    open val inputSearch = false
    //分割线
    open val hasSplitLine = true
    //输入框变化就开始搜索
    open val filterFunc: (T, String) -> Boolean = { t, s -> true }

    lateinit var adapterLocal: SlimAdapter

    private var dataList: List<T>? = null

    override val layoutRes: Int
        get() = R.layout.fragment_refreshview

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter(view)
        if (hasSplitLine) {
            recyclerView.addItemDecoration(LineItemDecoration())
        }
        //输入搜索
        if (inputSearch) {
            view.layout_search_bar.visibility = View.VISIBLE
            inputSearch(view)
        } else {
            view.layout_search_bar.visibility = View.GONE
        }

        refresh()
        view.tv_refresh.setOnClickListener {
            view.include_empty_view.visibility = View.GONE
            refresh()
        }
    }


    private fun initAdapter(view: View) {
        view.recyclerView.setOnTouchListener { _, _ ->
            context?.closeKeyboard(view.et_search_input)
            if (view.et_search_input.text.isNullOrBlank()) {
                view.layout_search_mark.visibility = View.VISIBLE
            }
            return@setOnTouchListener false
        }

        adapterLocal = SlimAdapter.creator()
            .setGenericActualType(getClazz())
            .register<T>(itemLayoutRes) { injector, bean, position ->
                itemViewConvert(this, injector, bean, position)
            }
            .attachTo(view.recyclerView)
    }

    fun refresh() {
        httpRequest()
    }


    private fun httpRequest() {

        httpPost(url)
            .addParams(httpParams)
            .enqueue(object : DataDialogHttpCallback<List<T>>(mActivity) {

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
                            mParentView.include_empty_view.visibility = View.GONE
                        } else {
                            mParentView.include_empty_view.visibility = View.VISIBLE
                            mParentView.tv_refresh_reason.text = "没有数据"
                        }
                    } else {
                        mParentView.include_empty_view.visibility = View.VISIBLE
                        mParentView.tv_refresh_reason.text = "查询失败"
                    }
                }
            })
    }

    /**
     * 输入搜索
     */
    private fun inputSearch(view: View) {
        view.layout_search_mark.setOnTouchListener { _, _ ->
            view.layout_search_mark.visibility = View.GONE
            context?.openKeyboard(view.et_search_input)
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
            dataList = ArrayList<T>()
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