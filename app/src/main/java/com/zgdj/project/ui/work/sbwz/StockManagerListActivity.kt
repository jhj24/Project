package com.zgdj.project.ui.work.sbwz

import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jhj.slimadapter.SlimAdapter
import com.jhj.slimadapter.holder.ViewInjector
import com.zgdj.lib.base.activity.BaseCommonListActivity
import com.zgdj.lib.config.Config
import com.zgdj.lib.extention.readAssets
import com.zgdj.project.R
import org.jetbrains.anko.startActivity

class StockManagerListActivity : BaseCommonListActivity<String>() {

    override val topbar: String
        get() = "库存管理"
    override val itemLayoutRes: Int
        get() = R.layout.list_item_stock_manager


    override val inputSearch: Boolean
        get() = true
    override val filterFunc: (String, String) -> Boolean = { bean, str ->
        bean.contains(str)
    }


    override fun getDataList(): List<String> {
        val str = readAssets("data_1_2.json")
        return Gson().fromJson(str, object : TypeToken<List<String>>() {}.type)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       /* topBarRightText("新增") {
            startActivity<DeviceInfoEditActivity>()
        }*/
    }

    override fun itemViewConvert(adapter: SlimAdapter, injector: ViewInjector, bean: String, position: Int) {
        injector.text(R.id.tv_title, bean)
            .clicked {
                startActivity<StockManagerInfoActivity>(Config.TITLE to bean)
            }
    }


}