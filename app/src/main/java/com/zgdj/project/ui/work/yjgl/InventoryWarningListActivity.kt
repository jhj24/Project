package com.zgdj.project.ui.work.yjgl

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jhj.slimadapter.SlimAdapter
import com.jhj.slimadapter.holder.ViewInjector
import com.zgdj.lib.base.activity.BaseCommonListActivity
import com.zgdj.lib.extention.getResColor
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val str = readAssets("data_3_2.json")
        dataList = Gson().fromJson(str, object : TypeToken<List<InvertoryWarningBean>>() {}.type)
    }

    override fun itemViewConvert(adapter: SlimAdapter, injector: ViewInjector, bean: InvertoryWarningBean, position: Int) {
        val allSum = 10 + bean.all_sum.toString().length
        val span = SpannableString("安全库存/现有库存：${bean.all_sum}/${bean.exist_sum}")
        span.setSpan(ForegroundColorSpan(getResColor(R.color.menu_selector)), 0, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        span.setSpan(ForegroundColorSpan(getResColor(R.color.red_main)), 5, 9, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        span.setSpan(ForegroundColorSpan(getResColor(R.color.menu_selector)), 10, allSum, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        span.setSpan(ForegroundColorSpan(getResColor(R.color.red_main)), allSum + 1, span.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

        injector.text(R.id.tv_title, bean.type)
                .text(R.id.tv_name, "备用备件名称：${bean.name}")
                .text(R.id.tv_sum, span)
                .text(R.id.tv_type, "规格型号：${bean.model}")
                .text(R.id.tv_principal, "负责人：${bean.principal}")
    }
}