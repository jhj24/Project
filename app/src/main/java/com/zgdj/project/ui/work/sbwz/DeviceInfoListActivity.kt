package com.zgdj.project.ui.work.sbwz

import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jhj.slimadapter.SlimAdapter
import com.jhj.slimadapter.holder.ViewInjector
import com.zgdj.lib.base.activity.BaseCommonListActivity
import com.zgdj.lib.config.Config
import com.zgdj.lib.extention.bottomSingleDialog
import com.zgdj.lib.extention.readAssets
import com.zgdj.lib.extention.toArrayList
import com.zgdj.lib.utils.activityresult.ActivityResult
import com.zgdj.project.DeviceBean
import com.zgdj.project.R
import org.jetbrains.anko.startActivity


class DeviceInfoListActivity : BaseCommonListActivity<DeviceBean>() {

    private var dataList = arrayListOf<DeviceBean>()

    override val title: String
        get() = "设备信息"
    override val itemLayoutRes: Int
        get() = R.layout.list_item_device_info

    override val hasSplitLine: Boolean
        get() = false

    override val inputSearch: Boolean
        get() = true
    override val filterFunc: (DeviceBean, String) -> Boolean = { bean, str ->
        bean.title.contains(str) or bean.code.contains(str)
    }


    override fun getDataList(): List<DeviceBean> {
        val str = readAssets("data_1_1.json")
        dataList = Gson().fromJson<List<DeviceBean>>(str, object : TypeToken<List<DeviceBean>>() {}.type).toArrayList()
        return dataList
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        topBarRightText("新增") {
            ActivityResult.with(this)
                .putString(Config.TYPE, "add")
                .targetActivity(DeviceInfoEditActivity::class.java)
                .onResult {
                    if (it != null) {
                        val name = it.getStringExtra("name")
                        val code = it.getStringExtra("code")
                        val factory = it.getStringExtra("factory")
                        dataList.add(0, DeviceBean(name, code, factory))
                        adapterLocal.setDataList(dataList)
                    }

                }
        }
    }

    override fun itemViewConvert(adapter: SlimAdapter, injector: ViewInjector, bean: DeviceBean, position: Int) {
        injector.text(R.id.tv_title, bean.title + "_" + bean.code)
            .text(R.id.tv_factory, bean.factory)
            .clicked {
                startActivity<DeviceInfoEditActivity>(
                    Config.TYPE to "read",
                    "name" to bean.title,
                    "code" to bean.code,
                    "factory" to bean.factory
                )
            }
            .clicked(R.id.iv_more) {
                bottomSingleDialog(list = listOf("编辑", "查看")) { alertFragment, s ->
                    when (s) {
                        "编辑" -> ActivityResult.with(this)
                            .putString(Config.TYPE, "edit")
                            .putString("name", bean.title)
                            .putString("code", bean.code)
                            .putString("factory", bean.factory)
                            .targetActivity(DeviceInfoEditActivity::class.java)
                            .onResult {
                                if (it != null) {
                                    val name = it.getStringExtra("name")
                                    val code = it.getStringExtra("code")
                                    val factory = it.getStringExtra("factory")
                                    adapterLocal.remove(position)
                                    adapterLocal.addData(position, DeviceBean(name, code, factory))
                                }

                            }
                        "查看" -> startActivity<DeviceInfoEditActivity>(
                            Config.TYPE to "read",
                            "name" to bean.title,
                            "code" to bean.code,
                            "factory" to bean.factory
                        )
                    }

                }
            }
    }


}