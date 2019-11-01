package com.zgdj.project.ui.work.sbwz

import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jhj.slimadapter.SlimAdapter
import com.jhj.slimadapter.holder.ViewInjector
import com.zgdj.lib.base.activity.BaseCommonListActivity
import com.zgdj.lib.config.Config
import com.zgdj.lib.extention.readAssets
import com.zgdj.lib.utils.activityresult.ActivityResult
import com.zgdj.project.R
import com.zgdj.project.StockManagerInfoBean
import org.jetbrains.anko.toast

class StockManagerInfoActivity : BaseCommonListActivity<StockManagerInfoBean>() {

    override val title: String
        get() = intent.getStringExtra(Config.TITLE)

    override val itemLayoutRes: Int
        get() = R.layout.list_item_stock_manager_info

    override val hasSplitLine: Boolean
        get() = false

    override val inputSearch: Boolean
        get() = true

    override val filterFunc: (StockManagerInfoBean, String) -> Boolean = { bean, str ->
        bean.title.contains(str)
    }


    override fun getDataList(): List<StockManagerInfoBean> {
        val str = readAssets("data_1_2_1.json")
        return Gson().fromJson(str, object : TypeToken<List<StockManagerInfoBean>>() {}.type)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        topBarRightText("新增") {
            ActivityResult.with(this)
                    .targetActivity(StockManagerInfoEditActivity::class.java)
                    .putString(Config.TITLE, title)
                    .onResult {
                        if (it != null) {
                            val data = it.getSerializableExtra(Config.DATA)
                            adapterLocal.addData(0, data)
                            mRecyclerView?.scrollToPosition(0)
                        }
                    }
        }
    }

    override fun itemViewConvert(adapter: SlimAdapter, injector: ViewInjector, bean: StockManagerInfoBean, position: Int) {
        injector.text(R.id.tv_title, bean.title)
                .text(R.id.tv_code, "规格型号：${bean.code}")
                .text(R.id.tv_unit, "单位：${bean.unit}")
                .text(R.id.tv_num0, "安全库存数：${bean.secure_sum}")
                .text(R.id.tv_num1, "现有库存：${bean.exit_sum}")
                .clicked(R.id.tv_in) {
                    ActivityResult.with(this)
                            .targetActivity(StockOutOrInActivity::class.java)
                            .putString(Config.TITLE, bean.title)
                            .putString(Config.TYPE, "in")
                            .onResult {
                                if (it != null) {
                                    toast("进口成功")
                                    val num = it.getIntExtra(Config.NUM, 0)
                                    bean.exit_sum = bean.exit_sum + num
                                    adapter.notifyItemChanged(position)
                                }

                            }
                }
                .clicked(R.id.tv_out) {
                    ActivityResult.with(this)
                            .targetActivity(StockOutOrInActivity::class.java)
                            .putString(Config.TITLE, bean.title)
                            .putString(Config.TYPE, "out")
                            .putInt(Config.NUM, bean.exit_sum)
                            .onResult {
                                if (it != null) {
                                    toast("出口成功")
                                    val num = it.getIntExtra(Config.NUM, 0)
                                    bean.exit_sum = bean.exit_sum - num
                                    adapter.notifyItemChanged(position)
                                }
                            }
                }
    }


}