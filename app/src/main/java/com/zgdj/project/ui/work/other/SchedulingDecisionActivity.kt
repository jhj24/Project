package com.zgdj.project.ui.work.other

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jhj.slimadapter.SlimAdapter
import com.jhj.slimadapter.holder.ViewInjector
import com.zgdj.lib.base.activity.BaseCommonListActivity
import com.zgdj.lib.extention.readAssets
import com.zgdj.project.R
import com.zgdj.project.SchedulingDecisionBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SchedulingDecisionActivity : BaseCommonListActivity<SchedulingDecisionBean>() {

    lateinit var list: List<SchedulingDecisionBean>

    override val topbar: String
        get() = "调度决策"

    override val itemLayoutRes: Int
        get() = R.layout.list_item_scheduling_decision

    override fun getDataList(): List<SchedulingDecisionBean> {
        return listOf(list.first())
    }

    override fun itemViewConvert(adapter: SlimAdapter, injector: ViewInjector, bean: SchedulingDecisionBean, position: Int) {
        injector.text(R.id.tv_title, bean.message)
                .text(R.id.tv_work, "工况：${bean.work}")
                .with<RecyclerView>(R.id.recycler_view) {
                    SlimAdapter.creator()
                            .register<String>(R.layout.list_item_scheduling_decision_item) { injector, bean, position ->
                                injector.text(R.id.text_view, bean)
                            }
                            .attachTo(it)
                            .setDataList(bean.condition)
                }
    }




    override fun initialize() {
        super.initialize()
        val str = readAssets("data_5.json")
        list = Gson().fromJson(str, object : TypeToken<List<SchedulingDecisionBean>>() {}.type)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        refresh(0)
    }

    private fun refresh(index: Int) {
        var isEnd = true
        GlobalScope.launch(Dispatchers.Main) {
            if (index == 0) {
                isEnd = false
                adapterLocal.setDataList(listOf(list.last()))
            } else if (index < list.size) {
                isEnd = false
                adapterLocal.addData(0, list[list.size - index - 1])
            }
            delay(5000)
            val current = index + 1
            if (!isEnd) {
                refresh(current)
            }
        }
    }
}