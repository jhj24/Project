package com.zgdj.lib.base.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import com.jhj.httplibrary.model.HttpParams
import com.jhj.slimadapter.SlimAdapter
import com.jhj.slimadapter.holder.ViewInjector
import com.jhj.slimadapter.itemdecoration.LineItemDecoration
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.zgdj.lib.R
import com.zgdj.lib.extention.closeKeyboard
import com.zgdj.lib.extention.openKeyboard
import com.zgdj.lib.extention.toArrayList
import com.zgdj.lib.net.callback.DataHttpCallback
import com.zgdj.lib.extention.httpPost
import kotlinx.android.synthetic.main.fragment_recclerview_refresh.view.*
import kotlinx.android.synthetic.main.fragment_refreshview.*
import kotlinx.android.synthetic.main.layout_empty_view.view.*
import kotlinx.android.synthetic.main.layout_search_bar.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

abstract class BaseRefreshFragment<T> : BaseFragment() {

    companion object {
        private const val REQUEST_FIRST = 0
        private const val REQUEST_OTHER = 1
    }

    abstract val url: String
    abstract val itemLayoutRes: Int

    //分页大小
    open val pageSize = 10
    //起始页
    open val startPageNum = 1
    //分割线
    open val hasSplitLine = true
    //请求参数
    open val httpParams: HttpParams = HttpParams()
    //输入搜索
    open val inputSearch = false
    //要搜索的key
    open val inputSearchKey = ""
    //输入框变化就开始搜索
    open val isInputSearchEach = false

    private val inputSearchParams: HttpParams = HttpParams()
    private var pageNo = 1
    private var isFirstLoading = true

    lateinit var mView: View
    lateinit var adapterLocal: SlimAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.mView = view
        initialize()
        initAdapter()

        //分割线
        if (hasSplitLine) {
            recyclerView.addItemDecoration(LineItemDecoration())
        }

        //输入搜索
        if (inputSearch) {
            view.layout_search_bar.visibility = View.VISIBLE
            inputSearch()
        } else {
            view.layout_search_bar.visibility = View.GONE
        }

        //数据刷新加载
        view.smartRefreshLayout.autoRefresh()
        view.smartRefreshLayout.setEnableLoadMoreWhenContentNotFull(true)
        view.smartRefreshLayout.setOnRefreshListener {
            httpRequest(REQUEST_FIRST)
        }

        view.smartRefreshLayout.setOnLoadMoreListener {
            httpRequest(REQUEST_OTHER)
        }

        //重新加载
        view.tv_refresh.setOnClickListener {
            if (view.smartRefreshLayout.state != RefreshState.None) {
                httpRequest(REQUEST_FIRST)
                return@setOnClickListener;
            }
            view.include_empty_view.visibility = View.GONE
            view.smartRefreshLayout.visibility = View.VISIBLE
            view.smartRefreshLayout.autoRefresh()
        }
        view.layout_empty_view.onClick { }
    }


    override val layoutRes: Int
        get() = R.layout.fragment_recclerview_refresh

    private fun initAdapter() {

        mView.recyclerView.setOnTouchListener { _, _ ->
            mActivity.closeKeyboard(mView.et_search_input)
            if (mView.et_search_input.text.isNullOrBlank()) {
                mView.layout_search_mark.visibility = View.VISIBLE
            }
            return@setOnTouchListener false
        }

        adapterLocal = SlimAdapter.creator()
            .setGenericActualType(getClazz())
            .register<T>(itemLayoutRes) { injector, bean, position ->
                itemViewConvert(this, injector, bean, position)
            }
            .attachTo(mView.recyclerView)
    }

    fun refresh() {
        httpRequest(REQUEST_FIRST)
    }

    private fun httpRequest(type: Int) {

        if (type == REQUEST_FIRST) {
            isFirstLoading = true
            pageNo = startPageNum
            mView.smartRefreshLayout.setNoMoreData(false)
        } else {
            isFirstLoading = false
        }

        httpPost(url)
            .addParam("page", pageNo.toString())
            .addParam("pagesize", pageSize.toString())
            .addParams(inputSearchParams)
            .addParams(httpParams)
            .enqueue(object : DataHttpCallback<List<T>>(mActivity) {

                var isSuccess = false

                override val clazzType: Type
                    get() = type(List::class.java, getClazz())

                override fun onSuccess(data: List<T>?, resultType: ResultType) {
                    isSuccess = true
                    if (isFirstLoading) {
                        adapterLocal.setDataList(data.toArrayList())
                        mView.smartRefreshLayout.finishRefresh()
                    } else {
                        adapterLocal.addDataList(data.toArrayList())
                        mView.smartRefreshLayout.finishLoadMore()
                    }

                    if (!data.isNullOrEmpty()) {
                        pageNo++
                    } else {
                        mView.smartRefreshLayout.setNoMoreData(true)
                    }
                }

                override fun onFailure(msg: String) {
                    isSuccess = false
                    if (isFirstLoading) {
                        mView.smartRefreshLayout.finishRefresh(false)
                    } else {
                        mView.smartRefreshLayout.finishLoadMore(false)
                    }
                }

                override fun onFinish() {
                    super.onFinish()
                    if (isSuccess) {
                        if (adapterLocal.isDataListNotEmpty()) {
                            mView.include_empty_view.visibility = View.GONE
                        } else {
                            mView.include_empty_view.visibility = View.VISIBLE
                            mView.tv_refresh_reason.text = "没有数据"
                        }
                    } else {
                        mView.include_empty_view.visibility = View.VISIBLE
                        mView.tv_refresh_reason.text = "查询失败"
                    }
                }

            })
    }

    /**
     * 输入搜索
     */
    private fun inputSearch() {

        //显示软键盘
        mView.layout_search_mark.setOnTouchListener { _, _ ->
            mView.layout_search_mark.visibility = View.GONE
            mActivity.openKeyboard(mView.et_search_input)
            return@setOnTouchListener false
        }

        //输入搜索
        mView.et_search_input.addTextChangedListener(object : TextWatcher {

            var repeatStr: String? = null

            override fun afterTextChanged(p0: Editable?) {

                if (inputSearchKey.isBlank()) {
                    throw NullPointerException("Please set the key corresponding to the content to be searched.")
                }

                val searchText: String? = p0?.toString()
                if (searchText == repeatStr) { //避免多次调用
                    return
                }
                repeatStr = searchText
                if (searchText.isNullOrBlank()) { //删除完成后自动搜索
                    inputSearchParams.clear()
                    httpRequest(REQUEST_FIRST)
                } else if (isInputSearchEach) { //是否每次输入后自动搜索
                    inputSearchParams.clear()
                    inputSearchParams.put(inputSearchKey, searchText)
                    httpRequest(REQUEST_FIRST)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        //监听软件盘的搜索按钮
        mView.et_search_input.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mActivity.closeKeyboard(mView.et_search_input)
                when {
                    mView.et_search_input.text.isNullOrBlank() -> {
                        mActivity.toast("请输入要搜索的关键字")
                        return@setOnEditorActionListener false
                    }
                    inputSearchKey.isBlank() -> throw NullPointerException("Please set the key corresponding to the content to be searched.")
                    else -> httpRequest(REQUEST_FIRST)
                }
            }
            return@setOnEditorActionListener false
        }
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

    abstract fun itemViewConvert(adapter: SlimAdapter, injector: ViewInjector, data: T, position: Int)
}