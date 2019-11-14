package com.zgdj.lib.base.activity

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import com.jhj.httplibrary.HttpCall
import com.jhj.httplibrary.model.HttpParams
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.zgdj.lib.extention.closeKeyboard
import com.zgdj.lib.extention.toArrayList
import com.zgdj.lib.net.DataResult
import com.zgdj.lib.net.callback.DataHttpCallback
import com.zgdj.lib.utils.Logger
import kotlinx.android.synthetic.main.activity_recyclerview_refresh.*
import kotlinx.android.synthetic.main.layout_empty_view.*
import kotlinx.android.synthetic.main.layout_search_bar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.toast
import java.lang.reflect.Type

abstract class BaseRefreshListActivity<T> : BaseListActivity<T>() {

    abstract val url: String

    //默认请求参数
    open val httpParams = mutableListOf<Pair<String, String>>()
    //默认起始页
    open val startPageNum = 1
    //默认分页大小
    open val pageSize = 10
    //默认起始页key
    open val pageNumKey = "page"
    //默认分页大小key
    open val pageSizeKey = "pagesize"
    //要搜索的key
    open val inputSearchKey = ""
    //输入框变化就开始搜索
    open val isInputSearchEach = true

    //当前页
    private var pageNo = 1
    //输入搜索参数
    private val inputSearchParams: HttpParams = HttpParams()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        smartRefreshLayout.setEnableLoadMore(true)
        smartRefreshLayout.setEnableRefresh(true)
        smartRefreshLayout.autoRefresh()
        smartRefreshLayout.setEnableLoadMoreWhenContentNotFull(true)
        smartRefreshLayout.setOnRefreshListener {
            httpRequest(true)
        }
        smartRefreshLayout.setOnLoadMoreListener {
            httpRequest(false)
        }
        tv_refresh.setOnClickListener {
            refresh()
        }

        //监听软件盘的搜索按钮
        et_search_input.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                closeKeyboard(et_search_input)
                when {
                    et_search_input.text.isNullOrBlank() -> {
                        toast("请输入要搜索的关键字")
                        return@setOnEditorActionListener false
                    }
                    inputSearchKey.isBlank() -> throw NullPointerException("Please set the key corresponding to the content to be searched.")
                    else -> smartRefreshLayout.autoRefresh()
                }
            }
            return@setOnEditorActionListener false
        }
    }

    override fun refresh() {
        if (smartRefreshLayout.state != RefreshState.None) {
            smartRefreshLayout.finishRefresh()
            smartRefreshLayout.setOnMultiPurposeListener(object : SimpleMultiPurposeListener() {
                override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {
                    include_empty_view.visibility = View.GONE
                    smartRefreshLayout.visibility = View.VISIBLE
                    if (newState == RefreshState.None) {
                        refreshLayout.setOnMultiPurposeListener(null)
                        refreshLayout.autoRefresh()
                    }
                }
            })
            return
        }
        include_empty_view.visibility = View.GONE
        smartRefreshLayout.visibility = View.VISIBLE
        smartRefreshLayout.autoRefresh()
    }


    override fun inputSearch(search: String?) {
        if (inputSearchKey.isBlank()) throw NullPointerException("Please set the key corresponding to the content to be searched.")
        if (search.isNullOrBlank()) { //删除完成后自动搜索
            inputSearchParams.clear()
            httpRequest(true)
        } else { //是否每次输入后自动搜索
            inputSearchParams.clear()
            inputSearchParams.put(inputSearchKey, search)
            if (isInputSearchEach) {
                httpRequest(true)
            }
        }
    }


    private fun httpRequest(isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = startPageNum
            smartRefreshLayout.resetNoMoreData()
        }

        val params = HttpParams()
        httpParams.forEach {
            params.put(it.first, it.second)
        }
        HttpCall.post(url)
                .addParam(pageNumKey, pageNo.toString())
                .addParam(pageSizeKey, pageSize.toString())
                .addParams(filterParams)
                .addParams(inputSearchParams)
                .addParams(params)
                .enqueue(object : DataHttpCallback<List<T>>(this) {

                    var isSuccess = false

                    override val clazzType: Type
                        get() = type(DataResult::class.java, type(List::class.java, genericType()))

                    override fun onSuccess(data: List<T>?, resultType: ResultType) {
                        isSuccess = true
                        if (isRefresh) {
                            dataList = data.orEmpty()
                            smartRefreshLayout.finishRefresh()
                        } else {
                            adapterLocal.addDataList(data.toArrayList())
                            smartRefreshLayout.finishLoadMore()
                        }

                        if (data != null && data.isNotEmpty()) {
                            pageNo++
                        } else {
                            smartRefreshLayout.finishLoadMoreWithNoMoreData()
                        }
                    }


                    override fun onFailure(msg: String) {
                        super.onFailure(msg)
                        isSuccess = false
                        if (isRefresh) {
                            smartRefreshLayout.finishRefresh(false)
                        } else {
                            smartRefreshLayout.finishLoadMore(false)
                        }
                    }

                    override fun onFinish() {
                        super.onFinish()
                        if (!isSuccess) {
                            include_empty_view.visibility = View.VISIBLE
                            tv_refresh_reason.text = "查询失败"
                        }
                    }
                })
    }
}