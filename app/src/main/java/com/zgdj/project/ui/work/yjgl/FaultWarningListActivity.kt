package com.zgdj.project.ui.work.yjgl

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jhj.slimadapter.SlimAdapter
import com.jhj.slimadapter.holder.ViewInjector
import com.zgdj.lib.base.activity.BaseCommonListActivity
import com.zgdj.lib.extention.readAssets
import com.zgdj.lib.widget.SwitchButton
import com.zgdj.project.FaultWarningBean
import com.zgdj.project.R

class FaultWarningListActivity : BaseCommonListActivity<FaultWarningBean>() {


    override val title: String
        get() = "故障报警"
    override val itemLayoutRes: Int
        get() = R.layout.list_item_fault_warning

    override val hasSplitLine: Boolean
        get() = false


    override fun getDataList(): List<FaultWarningBean> {
        val str = readAssets("data_3_1.json")
        return Gson().fromJson(str, object : TypeToken<List<FaultWarningBean>>() {}.type)
    }

    override fun itemViewConvert(adapter: SlimAdapter, injector: ViewInjector, bean: FaultWarningBean, position: Int) {
        injector.text(R.id.tv_time, bean.time)
            .text(R.id.tv_name, "名称：${bean.name}")
            .text(R.id.tv_group, "组：${bean.group}")
            .text(R.id.tv_status, "状态：${bean.status}")
            .text(R.id.tv_type, "类型：${bean.type}")
            .text(R.id.tv_value, "值：${bean.value}")
            .text(R.id.tv_value_warning, "报警值：${bean.alarm}")
            .text(R.id.tv_remark, bean.remark)
            .with<SwitchButton>(R.id.switch_button) {
                it.isOpened = bean.isDeal
            }
    }
}