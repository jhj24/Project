package com.zgdj.project.ui.work.ywjx

import android.os.Bundle
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jhj.slimadapter.SlimAdapter
import com.jhj.slimadapter.holder.ViewInjector
import com.zgdj.lib.base.activity.BaseCommonListActivity
import com.zgdj.lib.extention.getResDrawable
import com.zgdj.lib.extention.readAssets
import com.zgdj.project.OperateTicketBean
import com.zgdj.project.R

class OperateTicketListActivity : BaseCommonListActivity<OperateTicketBean>() {


    override val topbar: String
        get() = "工作票"
    override val itemLayoutRes: Int
        get() = R.layout.list_item_work_ticket

    override val hasSplitLine: Boolean
        get() = false
    override val inputSearch: Boolean
        get() = true
    override val filterFunc: (OperateTicketBean, String) -> Boolean = { bean, str ->
        bean.title.contains(str)
    }

    override fun getDataList(): List<OperateTicketBean> {
        val str = readAssets("data_2_3.json")
        return Gson().fromJson(str, object : TypeToken<List<OperateTicketBean>>() {}.type)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        topBarRightText("新增") {

        }
    }

    override fun itemViewConvert(adapter: SlimAdapter, injector: ViewInjector, bean: OperateTicketBean, position: Int) {
        injector.text(R.id.tv_title, bean.title)
            .text(R.id.tv_time, "日期：${bean.time}")
            .text(R.id.tv_principal, "${bean.site_name}_${bean.machine_name}")
            .text(R.id.tv_code, "编号：${bean.code}")
            .with<TextView>(R.id.tv_status) {
                it.text = bean.status
                when (bean.status) {
                    "未开始" -> it.background = getResDrawable(R.drawable.shape_status_gray)
                    "进行中" -> it.background = getResDrawable(R.drawable.shape_status_blue)
                    "已完成" -> it.background = getResDrawable(R.drawable.shape_status_green)
                }
            }

    }
}