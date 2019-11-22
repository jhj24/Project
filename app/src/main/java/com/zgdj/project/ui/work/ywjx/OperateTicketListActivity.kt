package com.zgdj.project.ui.work.ywjx

import android.os.Bundle
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jhj.slimadapter.SlimAdapter
import com.jhj.slimadapter.holder.ViewInjector
import com.zgdj.lib.base.activity.BaseCommonListActivity
import com.zgdj.lib.config.Config
import com.zgdj.lib.extention.bottomSingleDialog
import com.zgdj.lib.extention.getResDrawable
import com.zgdj.lib.extention.messageDialog
import com.zgdj.lib.extention.readAssets
import com.zgdj.lib.utils.activityresult.ActivityResult
import com.zgdj.project.OperateTicketBean
import com.zgdj.project.R
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class OperateTicketListActivity : BaseCommonListActivity<OperateTicketBean>() {

    override val title: String
        get() = "操作票"
    override val itemLayoutRes: Int
        get() = R.layout.list_item_work_ticket

    override val hasSplitLine: Boolean
        get() = false
    override val inputSearch: Boolean
        get() = true
    override val inputSearchFunc: (OperateTicketBean, String) -> Boolean = { bean, str ->
        bean.title.contains(str)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val str = readAssets("data_2_3.json")
        dataList = Gson().fromJson(str, object : TypeToken<List<OperateTicketBean>>() {}.type)
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
                .clicked(R.id.iv_more) {
                    when (bean.status) {
                        "未开始" -> bottomDialog(bean, position, listOf("查看", "编辑", "删除"))
                        "进行中" -> bottomDialog(bean, position, listOf("操作", "删除"))
                        "已完成" -> bottomDialog(bean, position, listOf("查看", "删除"))
                    }
                }
                .clicked {
                    startActivity<OperateTicketEditActivity>(Config.TYPE to "read", Config.STATUS to bean.status, Config.TITLE to bean.title, Config.DATA to bean)
                }
    }

    fun bottomDialog(bean: OperateTicketBean, index: Int, list: List<String>) {
        bottomSingleDialog(list = list) { alertFragment, str ->
            when (str) {
                "查看" -> {
                    startActivity<OperateTicketEditActivity>(Config.TYPE to "read", Config.STATUS to bean.status, Config.TITLE to bean.title, Config.DATA to bean)
                }
                "操作" -> ActivityResult.with(this)
                        .targetActivity(OperateTicketEditActivity::class.java)
                        .putString(Config.TYPE, "edit")
                        .putString(Config.TITLE, bean.title)
                        .putSerializable(Config.DATA, bean)
                        .onResult {
                            if (it != null) {
                                val data = it.getSerializableExtra(Config.DATA) as OperateTicketBean
                                toast("操作成功")
                                data.status = "已完成"
                                adapterLocal.remove(index)
                                adapterLocal.addData(index, data)
                            }
                        }
                "删除" -> {
                    messageDialog(msg = "是否删除${bean.title}?") { alertFragment, view ->
                        adapterLocal.remove(index)
                        adapterLocal.notifyItemChanged(index)
                        toast("删除成功")
                    }
                }
            }

        }
    }
}