package com.zgdj.lib.base.activity

import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.jhj.httplibrary.model.HttpParams
import com.jhj.slimadapter.SlimAdapter
import com.jhj.slimadapter.holder.ViewInjector
import com.jhj.slimadapter.itemdecoration.LineItemDecoration
import com.zgdj.lib.R
import com.zgdj.lib.extention.closeKeyboard
import com.zgdj.lib.extention.glide
import com.zgdj.lib.extention.inflater
import com.zgdj.lib.extention.openKeyboard
import kotlinx.android.synthetic.main.activity_recyclerview_refresh.*
import kotlinx.android.synthetic.main.layout_empty_view.*
import kotlinx.android.synthetic.main.layout_search_bar.*
import kotlinx.android.synthetic.main.layout_top_bar.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.leftOf
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.wrapContent
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

abstract class BaseListActivity<T> : BaseActivity() {

    abstract val title: String
    abstract val itemLayoutRes: Int

    lateinit var adapterLocal: SlimAdapter

    var filterParams: HttpParams = HttpParams()


    //输入搜索
    open val inputSearch = false
    //分割线
    open val hasSplitLine = true

    val mRecyclerView: RecyclerView
        get() = recyclerView

    val ivFilter: ImageView
        get() = iv_filter

    var dataList: List<T> = listOf()
        set(value) {
            field = value
            dataListEmpty(value)
        }

    fun dataListEmpty(value: List<T>) {
        if (value.isNullOrEmpty()) {
            include_empty_view.visibility = View.VISIBLE
            tv_refresh_reason.text = "没有数据"
        } else {
            include_empty_view.visibility = View.GONE
        }
        adapterLocal.setDataList(value)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview_refresh)
        initialize()
        tv_top_bar_title.text = title
        iv_top_bar_back.visibility = View.VISIBLE
        iv_top_bar_back.onClick {
            onBackPressed()
        }
        smartRefreshLayout.setEnableLoadMore(false)
        smartRefreshLayout.setEnableRefresh(false)
        //输入搜索
        if (inputSearch) {
            layout_search_bar.visibility = View.VISIBLE
            inputSearch()
        } else {
            layout_search_bar.visibility = View.GONE
        }

        if (hasSplitLine) recyclerView.addItemDecoration(LineItemDecoration())
        initAdapter()
        initDrawerLayout()
    }

    fun topBarRightText(text: String, body: () -> Unit) {
        tv_top_bar_right.visibility = View.VISIBLE
        tv_top_bar_right.text = text
        tv_top_bar_right.onClick {
            body()
        }
    }

    fun topBarRightImage(image: Int, color: Int? = null, body: () -> Unit) {
        if (color != null) iv_top_bar_right.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        iv_top_bar_right.visibility = View.VISIBLE
        iv_top_bar_back.glide(image)
        iv_top_bar_right.onClick {
            body()
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
                .setGenericActualType(genericType())
                .register<T>(itemLayoutRes) { injector, bean, position ->
                    itemViewConvert(this, injector, bean, position)
                }
                .attachTo(recyclerView)
    }


    private fun initDrawerLayout() {
        //设置抽屉模式
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        drawer_layout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(p0: Int) {
            }

            override fun onDrawerSlide(p0: View, p1: Float) {
                //StatusBarUtil.setLightMode(this@BaseListActivity)
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }

            override fun onDrawerClosed(p0: View) {
                //StatusBarUtil.setDarkMode(this@BaseListActivity)
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
        //改变搜索框样式
        val layoutParams = RelativeLayout.LayoutParams(matchParent, wrapContent)
        layoutParams.setMargins(dip(20), dip(10), dip(10), dip(10))
        layoutParams.leftOf(layout_filter)
        layout_search.layoutParams = layoutParams

        //点击筛选按钮
        layout_filter.visibility = View.VISIBLE
        layout_filter.onClick {
            closeKeyboard(layout_filter)
            if (!drawer_layout.isDrawerOpen(Gravity.END)) {
                drawer_layout.openDrawer(Gravity.END)
            } else {
                drawer_layout.closeDrawer(Gravity.END)
            }
        }

        //自定义筛选界面
        val view = inflater.inflate(layoutRes, layout_right_drawer, false)
        layout_right_drawer.addView(view)
        body(view, drawer_layout)

        //
        layout_operate.setOnRejectListener {
            resetFilter()
        }
        layout_operate.setOnCommitListener {
            filter()
        }
    }


    /**
     * 筛选
     */
    open fun filter() {
        drawer_layout.closeDrawer(Gravity.END)
        refresh()
    }

    /**
     * 重置
     */
    open fun resetFilter() {
        filterParams.clear()
        drawer_layout.closeDrawer(Gravity.END)
        refresh()
    }


    /**
     * 输入搜索
     */
    private fun inputSearch() {
        var repeatStr: String? = null
        layout_search_mark.setOnTouchListener { _, _ ->
            layout_search_mark.visibility = View.GONE
            openKeyboard(et_search_input)
            return@setOnTouchListener false
        }

        //输入搜索
        et_search_input.addTextChangedListener(object : TextWatcher {

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

    override fun onBackPressed() {
        closeKeyboard(iv_top_bar_back)
        super.onBackPressed()
    }

    abstract fun refresh()
    abstract fun inputSearch(search: String?)
    abstract fun itemViewConvert(adapter: SlimAdapter, injector: ViewInjector, bean: T, position: Int)
    open fun initialize() {}
}