package com.zgdj.project.ui.work.jkjc

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jhj.slimadapter.SlimAdapter
import com.jhj.slimadapter.holder.ViewInjector
import com.zgdj.lib.base.fragment.BaseCommonListFragment
import com.zgdj.lib.extention.readAssets
import com.zgdj.project.DataDetectBean
import com.zgdj.project.DetectInfoBean
import com.zgdj.project.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DataDetectFragment : BaseCommonListFragment<DataDetectBean>() {

    override val hasSplitLine: Boolean
        get() = false

    override val itemLayoutRes: Int
        get() = R.layout.list_item_data_detect

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val str = mActivity.readAssets("data_4_2.json")
        dataList = Gson().fromJson(str, object : TypeToken<List<DataDetectBean>>() {}.type)
        change()
    }

    private fun change() {
        GlobalScope.launch(Dispatchers.Main) {
            kotlinx.coroutines.delay(3000)
            adapterLocal.notifyDataSetChanged()
            change()
        }
    }

    override fun itemViewConvert(adapter: SlimAdapter, injector: ViewInjector, bean: DataDetectBean, position: Int) {
        injector.text(R.id.tv_title, bean.title)
                .with<RecyclerView>(R.id.recycler_view) {
                    SlimAdapter.creator()
                            .register<DetectInfoBean>(R.layout.list_item_detect_info) { injector, bean, position ->
                                val random = if (bean.value > 0) {
                                    val max = ((if (bean.unit == "%" && bean.value * (1 + 0.1f) > 100) 100f else bean.value * (1 + 0.1f)) * 100).toInt()
                                    val min = (bean.value * (1 - 0.1f) * 100).toInt()
                                    (min..max).random()
                                } else {
                                    val max = (bean.value * (1 - 0.1) * 100).toInt()
                                    val min = (bean.value * (1 + 0.1) * 100).toInt()
                                    (min..max).random()
                                }
                                injector.text(R.id.tv_key, bean.name)
                                        .text(R.id.tv_value, "${random.toFloat() / 100} ${bean.unit}")
                            }
                            .attachTo(it)
                            .setDataList(bean.data)
                }
    }
}