package com.zgdj.lib.base.activity

import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.RelativeLayout
import com.jhj.httplibrary.model.HttpParams
import com.jhj.slimadapter.SlimAdapter
import com.jhj.slimadapter.holder.ViewInjector
import com.jhj.slimadapter.itemdecoration.LineItemDecoration
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.zgdj.lib.R
import com.zgdj.lib.extention.*
import com.zgdj.lib.net.callback.DataHttpCallback
import com.zgdj.lib.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_recyclerview_refresh.*
import kotlinx.android.synthetic.main.layout_empty_view.*
import kotlinx.android.synthetic.main.layout_search_bar.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


abstract class BaseRefreshActivity<T> : BaseActivity() {

    companion object {
        private const val REQUEST_FIRST = 0
        private const val REQUEST_OTHER = 1
    }

    abstract val title: String
    abstract val url: String
    abstract val itemLayoutRes: Int

    open val pageSizeKey = "pagesize"

    open val pageNumKey = "page"

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
    open val isInputSearchEach = true

    private var selectorSearchParams: HttpParams = HttpParams()
    private val inputSearchParams: HttpParams = HttpParams()
    private var pageNo = 1
    private var isFirstLoading = true

    lateinit var adapterLocal: SlimAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview_refresh)
        initialize()
        initTopBar(title, View.VISIBLE)
        initDrawerLayout()
        adapterLocal = initAdapter()

        //分割线
        if (hasSplitLine) {
            recyclerView.addItemDecoration(LineItemDecoration())
        }

        //输入搜索
        if (inputSearch) {
            layout_search_bar.visibility = View.VISIBLE
            inputSearch()
        } else {
            layout_search_bar.visibility = View.GONE
        }

        //数据刷新加载
        smartRefreshLayout.autoRefresh()
        smartRefreshLayout.setEnableLoadMoreWhenContentNotFull(true)
        smartRefreshLayout.setOnRefreshListener {
            httpRequest(REQUEST_FIRST)
        }
        smartRefreshLayout.setOnLoadMoreListener {
            httpRequest(REQUEST_OTHER)
        }

        //重新加载
        tv_refresh.setOnClickListener {
            if (smartRefreshLayout.state != RefreshState.None) {
                httpRequest(REQUEST_FIRST)
                return@setOnClickListener
            }
            include_empty_view.visibility = View.GONE
            smartRefreshLayout.visibility = View.VISIBLE
            smartRefreshLayout.autoRefresh()
        }
        layout_empty_view.onClick { }

    }


    private fun initDrawerLayout() {
        //设置抽屉模式
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        drawer_layout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(p0: Int) {
            }

            override fun onDrawerSlide(p0: View, p1: Float) {
                StatusBarUtil.setLightMode(this@BaseRefreshActivity)
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            override fun onDrawerClosed(p0: View) {
                StatusBarUtil.setDarkMode(this@BaseRefreshActivity)
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            override fun onDrawerOpened(p0: View) {

            }

        })
    }

    /**
     *
     */
    fun initDrawerLayout(layoutRes: Int, body: (View, DrawerLayout) -> Unit) {
        val view = inflater.inflate(layoutRes, layout_right_drawer, false)
        layout_right_drawer.addView(view)
        body(view, drawer_layout)
    }

    /**
     * 筛选
     */
    fun filter(vararg pairs: Pair<String, String>) {
        drawer_layout.closeDrawer(Gravity.END)
        pairs.forEach {
            selectorSearchParams.put(it.first, it.second)
        }
        smartRefreshLayout.autoRefresh()
    }

    /**
     * 重置
     */
    fun reSet() {
        drawer_layout.closeDrawer(Gravity.END)
        selectorSearchParams.clear()
        smartRefreshLayout.autoRefresh()
    }

    fun initFilterView(res: Int, body: () -> Unit = {}) {
        closeKeyboard(layout_filter)
        val layoutParams = RelativeLayout.LayoutParams(matchParent, wrapContent)
        layoutParams.setMargins(dip(20), dip(10), dip(10), dip(10))
        layoutParams.leftOf(layout_filter)
        layout_search.layoutParams = layoutParams
        iv_filter.setImageDrawable(getResDrawable(res))
        layout_filter.visibility = View.VISIBLE
        layout_filter.onClick {
            if (!drawer_layout.isDrawerOpen(Gravity.END)) {
                drawer_layout.openDrawer(Gravity.END)
                body()
            } else {
                drawer_layout.closeDrawer(Gravity.END)
            }
        }
    }

    private fun initAdapter(): SlimAdapter {

        recyclerView.setOnTouchListener { _, _ ->
            closeKeyboard(et_search_input)
            if (et_search_input.text.isNullOrBlank()) {
                layout_search_mark.visibility = View.VISIBLE
            }
            return@setOnTouchListener false
        }

        return SlimAdapter.creator()
            .setGenericActualType(getClazz())
            .register<T>(itemLayoutRes) { injector, bean, position ->
                itemViewConvert(this, injector, bean, position)
            }
            .attachTo(recyclerView)
    }

    fun refresh() {
        httpRequest(0)
    }

    private fun httpRequest(type: Int) {

        if (type == REQUEST_FIRST) {
            isFirstLoading = true
            pageNo = startPageNum
            smartRefreshLayout.setNoMoreData(false)
        } else {
            isFirstLoading = false
        }

        httpPost(url)
            .addParam(pageNumKey, pageNo.toString())
            .addParam(pageSizeKey, pageSize.toString())
            .addParams(selectorSearchParams)
            .addParams(inputSearchParams)
            .addParams(httpParams)
            .enqueue(object : DataHttpCallback<List<T>>(this) {

                var isSuccess = false

                override val clazzType: Type
                    get() = type(List::class.java, getClazz())

                override fun onSuccess(data: List<T>?, resultType: ResultType) {
                    isSuccess = true
                    if (isFirstLoading) {
                        adapterLocal.setDataList(data.toArrayList())
                        smartRefreshLayout.finishRefresh()
                    } else {
                        adapterLocal.addDataList(data.toArrayList())
                        smartRefreshLayout.finishLoadMore()
                    }

                    if (data != null && data.isNotEmpty()) {
                        pageNo++
                    } else {
                        smartRefreshLayout.setNoMoreData(true)
                    }
                }


                override fun onFailure(msg: String) {
                    super.onFailure(msg)
                    isSuccess = false
                    if (isFirstLoading) {
                        smartRefreshLayout.finishRefresh(false)
                    } else {
                        smartRefreshLayout.finishLoadMore(false)
                    }
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

        //显示软键盘
        layout_search_mark.setOnTouchListener { _, _ ->
            layout_search_mark.visibility = View.GONE
            openKeyboard(et_search_input)
            return@setOnTouchListener false
        }

        //输入搜索
        et_search_input.addTextChangedListener(object : TextWatcher {

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
                } else { //是否每次输入后自动搜索
                    inputSearchParams.clear()
                    inputSearchParams.put(inputSearchKey, searchText)
                    if (isInputSearchEach)
                        httpRequest(REQUEST_FIRST)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

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
                    else -> {
                        smartRefreshLayout.autoRefresh()
                    }
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