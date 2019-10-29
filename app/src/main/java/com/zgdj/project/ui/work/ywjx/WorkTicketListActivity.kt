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
import com.zgdj.project.R
import com.zgdj.project.WorkTicketBean
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class WorkTicketListActivity : BaseCommonListActivity<WorkTicketBean>() {

    var dataList = arrayListOf<WorkTicketBean>()
    override val topbar: String
        get() = "工作票"
    override val itemLayoutRes: Int
        get() = R.layout.list_item_work_ticket

    override val hasSplitLine: Boolean
        get() = false
    override val inputSearch: Boolean
        get() = true
    override val filterFunc: (WorkTicketBean, String) -> Boolean = { bean, str ->
        bean.title.contains(str)
    }

    override fun getDataList(): List<WorkTicketBean> {
        val str = readAssets("data_2_2.json")
        dataList = Gson().fromJson(str, object : TypeToken<List<WorkTicketBean>>() {}.type)
        return dataList
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        topBarRightText("新增") {
            ActivityResult.with(this)
                .targetActivity(WorkTicketEditActivity::class.java)
                .putString(Config.TYPE, "add")
                .putString(Config.STATUS, "未开始")
                .onResult {
                    if (it != null) {
                        toast("新增成功")
                        val unit = it.getStringExtra("unit")
                        val code = it.getStringExtra("code")
                        val time = it.getStringExtra("time")
                        val signer = it.getStringExtra("signer")
                        val bean = WorkTicketBean(unit, time, "未开始", code, signer)
                        dataList.add(0, bean)
                        adapterLocal.setDataList(dataList)
                    }
                }
        }
    }

    override fun itemViewConvert(adapter: SlimAdapter, injector: ViewInjector, bean: WorkTicketBean, position: Int) {
        injector.text(R.id.tv_title, bean.title)
            .text(R.id.tv_time, "日期：${bean.time}")
            .text(R.id.tv_principal, "负责人：${bean.principal}")
            .text(R.id.tv_code, "表单编号：${bean.code}")
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
                    "未开始" -> bottomDialog(adapter, bean, position, listOf("查看", "编辑", "删除"))
                    "进行中" -> bottomDialog(adapter, bean, position, listOf("查看", "签批"))
                    "已完成" -> bottomDialog(adapter, bean, position, listOf("查看", "删除"))
                }
            }
            .clicked {
                startActivity<WorkTicketEditActivity>(Config.TYPE to "read", Config.STATUS to bean.status)
            }
    }

    fun bottomDialog(adapter: SlimAdapter, bean: WorkTicketBean, index: Int, list: List<String>) {
        bottomSingleDialog(list = list) { alertFragment, str ->
            when (str) {
                "查看" -> {
                    startActivity<WorkTicketEditActivity>(Config.TYPE to "read", Config.STATUS to bean.status)
                }
                "编辑" -> ActivityResult.with(this)
                    .targetActivity(WorkTicketEditActivity::class.java)
                    .putString(Config.TYPE, "edit")
                    .putString(Config.STATUS, bean.status)
                    .onResult {
                        if (it != null) {
                            toast("修改成功")
                            val unit = it.getStringExtra("unit")
                            val code = it.getStringExtra("code")
                            val time = it.getStringExtra("time")
                            val signer = it.getStringExtra("signer")
                            val workbean = WorkTicketBean(unit, time, "未开始", code, signer)
                            adapterLocal.remove(index)
                            adapterLocal.addData(index, workbean)
                        }
                    }
                "签批" -> ActivityResult.with(this)
                    .targetActivity(WorkTicketEditActivity::class.java)
                    .putString(Config.TYPE, "approval")
                    .putString(Config.STATUS, bean.status)
                    .onResult {
                        if (it != null) {
                            toast("签批成功")
                            val workbean = WorkTicketBean(bean.title, bean.time, "已完成", bean.code, bean.principal)
                            adapterLocal.remove(index)
                            adapterLocal.addData(index, workbean)
                        }
                    }
                "删除" -> {
                    messageDialog(msg = "是否删除${bean.title}?") { alertFragment, view ->
                        adapterLocal.remove(index)
                        dataList.removeAt(index)
                        adapterLocal.notifyItemChanged(index)
                        toast("删除成功")
                    }
                }
            }

        }
    }


}