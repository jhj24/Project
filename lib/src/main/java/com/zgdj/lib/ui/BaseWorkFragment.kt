package com.zgdj.lib.ui

import android.app.Activity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.TabLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.jhj.slimadapter.SlimAdapter
import com.jhj.slimadapter.itemdecoration.GridItemDecoration
import com.zgdj.lib.R
import com.zgdj.lib.base.fragment.BaseFragment
import com.zgdj.lib.utils.StatusBarUtil
import kotlinx.android.synthetic.main.fragment_work.view.*
import kotlinx.android.synthetic.main.layout_top_bar.view.*
import org.jetbrains.anko.internals.AnkoInternals
import org.jetbrains.anko.wrapContent


abstract class BaseWorkFragment : BaseFragment() {

    abstract val dataList: List<WorkBean>

    abstract val navigationViewTop: Int

    private var isClick = false

    override val layoutRes: Int
        get() = R.layout.fragment_work

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        StatusBarUtil.setLightMode(mActivity)
        view.tv_top_bar_title.text = "工作"
        val layoutManager = LinearLayoutManager(mActivity)
        initAdapter(view, layoutManager)
        initTabLayout(view, layoutManager)
    }

    private fun initAdapter(view: View, layoutManager: LinearLayoutManager) {
        val list = arrayListOf<Any>()
        list.addAll(dataList)
        list.add("")
        view.recycler_view.layoutManager = layoutManager
        view.recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == 0) {
                    isClick = false
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isClick) {
                    val firstPosition = layoutManager.findFirstVisibleItemPosition()
                    view.tab_layout.setScrollPosition(firstPosition, 0f, true)
                }
            }
        })

        SlimAdapter.creator()
            .register<WorkBean>(R.layout.list_item_work) { injector, bean, position ->
                injector.text(R.id.tv_title, bean.title)
                    .with<RecyclerView>(R.id.recycler_view) {
                        it.layoutManager = GridLayoutManager(mActivity, 4)
                        it.addItemDecoration(GridItemDecoration(4, 6, false))
                        SlimAdapter.creator()
                            .register<WorkBean.ItemBean>(R.layout.grid_item_work) { injector, bean, position ->
                                injector.text(R.id.textView, bean.text)
                                    .image(R.id.imageView, bean.res)
                                    .clicked {
                                        AnkoInternals.internalStartActivity(
                                            requireActivity(), bean.clazz, bean.paris
                                                ?: arrayOf()
                                        )
                                    }
                            }
                            .attachTo(it)
                            .setDataList(bean.list)
                    }

            }
            .register<String>(R.layout.list_item_empty_view) { injector, bean, position ->
                injector.with<ConstraintLayout>(R.id.layout_view) {
                    val v = getRecyclerView().getChildAt(list.size - 2)
                    if (v != null) {
                        val itemHeight = v.height
                        val height =
                            navigationViewTop - (view.status_bar.layoutParams.height + view.tv_top_bar_title.layoutParams.height +
                                    view.tab_layout.layoutParams.height)
                        it.layoutParams = ViewGroup.LayoutParams(wrapContent, height - itemHeight)
                    }
                }
            }
            .attachTo(view.recycler_view)
            .setDataList(list)
    }


    private fun initTabLayout(view: View, layoutManager: LinearLayoutManager) {
        dataList.map { it.title }.forEachIndexed { index, s ->
            view.tab_layout.addTab(view.tab_layout.newTab().setText(s).setTag(index))
        }
        view.tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
                onTabSelected(p0)
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                layoutManager.scrollToPositionWithOffset(p0?.position ?: 0, 0)
            }

        })
    }

    data class WorkBean(val title: String, val list: List<ItemBean>) {
        data class ItemBean(
            val res: Int,
            val text: String,
            val clazz: Class<out Activity>,
            val paris: Array<Pair<String, Any>>? = null
        )
    }
}
