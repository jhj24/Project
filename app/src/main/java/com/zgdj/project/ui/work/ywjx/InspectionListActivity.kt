package com.zgdj.project.ui.work.ywjx

import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jhj.slimadapter.SlimAdapter
import com.jhj.slimadapter.holder.ViewInjector
import com.zgdj.lib.base.activity.BaseCommonListActivity
import com.zgdj.lib.config.Config
import com.zgdj.lib.extention.getResDrawable
import com.zgdj.lib.extention.readAssets
import com.zgdj.lib.utils.activityresult.ActivityResult
import com.zgdj.project.InspectionBean
import com.zgdj.project.R

class InspectionListActivity : BaseCommonListActivity<InspectionBean>() {


    override val title: String
        get() = "现场巡检"
    override val itemLayoutRes: Int
        get() = R.layout.list_item_inspection

    override val hasSplitLine: Boolean
        get() = false
    override val inputSearch: Boolean
        get() = true
    override val inputSearchFunc: (InspectionBean, String) -> Boolean = { bean, str ->
        bean.title.contains(str)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val str = readAssets("data_2_1.json")
        dataList = Gson().fromJson(str, object : TypeToken<List<InspectionBean>>() {}.type)
        topBarRightText("新增") {
            ActivityResult.with(this)
                    .putString(Config.STATUS, "未开始")
                    .targetActivity(InspectionEditActivity::class.java)
                    .onResult {
                        if (it != null) {
                            val date = it.getStringExtra(Config.DATE)
                            adapterLocal.addData(0, InspectionBean("沙井泵站", date, "已完成"))
                        }

                    }
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
                .text(R.id.tv_inspection, "巡检人：孙钰杰")
                .background(R.id.tv_status, status)
                .clicked {
                    ActivityResult.with(this)
                            .putString(Config.STATUS, bean.status)
                            .targetActivity(InspectionEditActivity::class.java)
                            .onResult {
                                if (it != null) {
                                    bean.status = "已完成"
                                    adapter.notifyItemChanged(position)
                                }
                            }
                }
    }
}