package com.zgdj.project

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jhj.slimadapter.SlimAdapter
import com.jhj.slimadapter.holder.ViewInjector
import com.zgdj.lib.base.fragment.BaseCommonListFragment
import com.zgdj.lib.extention.readAssets

class ChildMessageFragment : BaseCommonListFragment<MessageBean>() {

    override val hasSplitLine: Boolean
        get() = false

    override val itemLayoutRes: Int
        get() = R.layout.list_item_message


    override fun getDataList(): List<MessageBean> {
        val str = context?.readAssets("data_0.json")
        if (str.isNullOrBlank()) return listOf()
        return Gson().fromJson(str, object : TypeToken<List<MessageBean>>() {}.type)
    }

    override fun itemViewConvert(adapter: SlimAdapter, injector: ViewInjector, bean: MessageBean, position: Int) {
        injector.text(R.id.tv_title, bean.title)
                .text(R.id.tv_name, bean.from)
                .text(R.id.tv_time, bean.time)
    }


}