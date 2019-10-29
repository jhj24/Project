package com.zgdj.project.ui.work.ywjx

import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jhj.slimadapter.SlimAdapter
import com.jhj.slimadapter.holder.ViewInjector
import com.zgdj.lib.base.activity.BaseCommonListActivity
import com.zgdj.lib.extention.getResDrawable
import com.zgdj.lib.extention.readAssets
import com.zgdj.project.InspectionBean
import com.zgdj.project.R

class InspectionListActivity : BaseCommonListActivity<InspectionBean>() {


    override val topbar: String
        get() = "现场巡检"
    override val itemLayoutRes: Int
        get() = R.layout.list_item_inspection

    override val hasSplitLine: Boolean
        get() = false
    override val inputSearch: Boolean
        get() = true
    override val filterFunc: (InspectionBean, String) -> Boolean = { bean, str ->
        bean.title.contains(str)
    }

    override fun getDataList(): List<InspectionBean> {
        val str = readAssets("data_2_1.json")
        return Gson().fromJson(str, object : TypeToken<List<InspectionBean>>() {}.type)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        topBarRightText("新增") {

        }
    }


    override fun itemViewConvert(adapter: SlimAdapter, injector: ViewInjector, bean: InspectionBean, position: Int) {

        val status = getResDrawable(
            if (bean.status == "已完成") R.drawable.shape_status_green else R.drawable.shape_status_blue
        )
        injector
            .text(R.id.tv_title, bean.title)
            .text(R.id.tv_status, bean.status)
            .text(R.id.tv_time, bean.time)
            .text(R.id.tv_frequency, "当日频次：第一次")
            .background(R.id.tv_status, status)
    }
}