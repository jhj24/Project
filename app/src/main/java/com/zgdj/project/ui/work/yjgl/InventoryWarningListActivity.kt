package com.zgdj.project.ui.work.yjgl

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jhj.slimadapter.SlimAdapter
import com.jhj.slimadapter.holder.ViewInjector
import com.zgdj.lib.base.activity.BaseCommonListActivity
import com.zgdj.lib.extention.readAssets
import com.zgdj.project.InvertoryWarningBean
import com.zgdj.project.R

class InventoryWarningListActivity : BaseCommonListActivity<InvertoryWarningBean>() {

    override val title: String
        get() = "库存报警"
    override val itemLayoutRes: Int
        get() = R.layout.list_item_inventory_warning

    override val hasSplitLine: Boolean
        get() = false


    override fun getDataList(): List<InvertoryWarningBean> {
        val str = readAssets("data_3_2.json")
        return Gson().fromJson(str, object : TypeToken<List<InvertoryWarningBean>>() {}.type)
    }

    override fun itemViewConvert(
        adapter: SlimAdapter,
        injector: ViewInjector,
        bean: InvertoryWarningBean,
        position: Int
    ) {
        injector.text(R.id.tv_title, bean.type)
            .text(R.id.tv_name, "备用备件名称：${bean.name}")
            .text(R.id.tv_sum, "安全库存/现有库存：${bean.all_sum}/${bean.exist_sum}")
            .text(R.id.tv_type, "规格型号：${bean.model}")
            .text(R.id.tv_principal, "负责人：${bean.principal}")
    }
}