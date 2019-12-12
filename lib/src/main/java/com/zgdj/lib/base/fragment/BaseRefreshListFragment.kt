package com.zgdj.lib.base.fragment

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import com.jhj.httplibrary.HttpCall
import com.jhj.httplibrary.model.HttpParams
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.zgdj.lib.base.activity.BaseListFragment
import com.zgdj.lib.extention.closeKeyboard
import com.zgdj.lib.net.DataResult
import com.zgdj.lib.net.callback.DataHttpCallback
import kotlinx.android.synthetic.main.fragment_recclerview_refresh.view.*
import kotlinx.android.synthetic.main.layout_empty_view.view.*
import kotlinx.android.synthetic.main.layout_search_bar.view.*
import org.jetbrains.anko.toast
import java.lang.reflect.Type

abstract class BaseRefreshListFragment<T> : BaseListFragment<T>() {

    abstract val url: String

    //默认请求参数
    open val httpParams = listOf<Pair<String, String>>()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.smartRefreshLayout.setEnableLoadMore(true)
        view.smartRefreshLayout.setEnableRefresh(true)
        view.smartRefreshLayout.autoRefresh()
        view.smartRefreshLayout.setEnableLoadMoreWhenContentNotFull(true)
        view.smartRefreshLayout.setOnRefreshListener {
            httpRequest(true)
        }
        view.smartRefreshLayout.setOnLoadMoreListener {
            httpRequest(false)
        }
        view.tv_refresh.setOnClickListener {
            refresh()
        }

        //监听软件盘的搜索按钮
        view.et_search_input.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                view.et_search_input.closeKeyboard()
                when {
                    view.et_search_input.text.isNullOrBlank() -> {
                        context?.toast("请输入要搜索的关键字")
                        return@setOnEditorActionListener false
                    }
                    inputSearchKey.isBlank() -> throw NullPointerException("Please set the key corresponding to the content to be searched.")
                    else -> view.smartRefreshLayout.autoRefresh()
                }
            }
            return@setOnEditorActionListener false
        }


    }

    override fun refresh() {
        if (mParentView.smartRefreshLayout.state != RefreshState.None) {
            mParentView.smartRefreshLayout.finishRefresh()
            mParentView.smartRefreshLayout.setOnMultiPurposeListener(object : SimpleMultiPurposeListener() {
                override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {
                    mParentView.include_empty_view.visibility = View.GONE
                    mParentView.smartRefreshLayout.visibility = View.VISIBLE
                    if (newState == RefreshState.None) {
                        refreshLayout.setOnMultiPurposeListener(null)
                        refreshLayout.autoRefresh()
                    }
                }
            })
            return
        }
        mParentView.include_empty_view.visibility = View.GONE
        mParentView.smartRefreshLayout.visibility = View.VISIBLE
        mParentView.smartRefreshLayout.autoRefresh()
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


    protected fun httpRequest(isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = startPageNum
            mParentView.smartRefreshLayout.resetNoMoreData()
        }

        val params = HttpParams()
        httpParams.forEach {
            params.put(it.first, it.second)
        }
        HttpCall.post(url)
                .addParam(pageNumKey, pageNo.toString())
                .addParam(pageSizeKey, pageSize.toString())
                .addParams(inputSearchParams)
                .addParams(params)
                .enqueue(object : DataHttpCallback<List<T>>(mActivity) {

                    var isSuccess = false

                    override val clazzType: Type
                        get() = type(DataResult::class.java, type(List::class.java, genericType()))

                    override fun onSuccess(data: List<T>?, resultType: ResultType) {
                        isSuccess = true
                        if (isRefresh) {
                            dataList = data.orEmpty()
                            mParentView.smartRefreshLayout.finishRefresh()
                        } else {
                            adapterLocal.addDataList(data.orEmpty())
                            mParentView.smartRefreshLayout.finishLoadMore()
                        }

                        if (data != null && data.isNotEmpty()) {
                            pageNo++
                        } else {
                            mParentView.smartRefreshLayout.finishLoadMoreWithNoMoreData()
                        }
                    }


                    override fun onFailure(msg: String) {
                        super.onFailure(msg)
                        isSuccess = false
                        if (isRefresh) {
                            mParentView.smartRefreshLayout.finishRefresh(false)
                        } else {
                            mParentView.smartRefreshLayout.finishLoadMore(false)
                        }
                    }

                    override fun onFinish() {
                        super.onFinish()
                        if (!isSuccess) {
                            mParentView.include_empty_view.visibility = View.VISIBLE
                            mParentView.tv_refresh_reason.text = "查询失败"
                        }
                    }
                })
    }
}