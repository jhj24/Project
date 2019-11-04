package com.zgdj.project.ui.message

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.jhj.slimadapter.SlimAdapter
import com.jhj.slimadapter.holder.ViewInjector
import com.zgdj.lib.base.fragment.BaseCommonListFragment
import com.zgdj.lib.config.Config
import com.zgdj.lib.extention.glide
import com.zgdj.lib.utils.bus.LiveDataBus
import com.zgdj.project.MessageBean
import com.zgdj.project.R
import com.zgdj.project.ui.work.other.SchedulingDecisionActivity
import com.zgdj.project.ui.work.yjgl.FaultWarningListActivity
import com.zgdj.project.ui.work.yjgl.InventoryWarningListActivity
import com.zgdj.project.ui.work.ywjx.InspectionListActivity
import com.zgdj.project.ui.work.ywjx.WorkTicketListActivity
import org.jetbrains.anko.support.v4.startActivity

class ChildMessageFragment1 : BaseCommonListFragment<MessageBean>() {

    override val hasSplitLine: Boolean
        get() = false

    override val itemLayoutRes: Int
        get() = R.layout.list_item_message

    var list = arrayListOf<MessageBean>(MessageBean(4, "调度事件", "2019年3月22日", 1, "孙钰杰"))


    override fun getDataList(): List<MessageBean> {
        return list
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val type = arguments?.getInt(Config.TYPE)
        LiveDataBus.get()
                .with("message", MessageBean::class.java)
                .observe(this, Observer {
                    if (it != null) {
                        adapterLocal.addData(0, it)
                    }
                })
    }

    override fun itemViewConvert(adapter: SlimAdapter, injector: ViewInjector, bean: MessageBean, position: Int) {

        injector.text(R.id.tv_title, bean.title)
                .text(R.id.tv_name, bean.from)
                .text(R.id.tv_time, bean.time)
                .with<ImageView>(R.id.imageView) { imageView ->
                    when (bean.type) {
                        0 -> imageView.glide(R.drawable.svg_inspection)
                        1 -> imageView.glide(R.drawable.svg_work_ticket)
                        2 -> imageView.glide(R.drawable.svg_fault_warning)
                        3 -> imageView.glide(R.drawable.svg_inventory_warning)
                        4 -> imageView.glide(R.drawable.svg_scheduling)
                    }
                }
                .clicked {
                    when (bean.type) {
                        0 -> startActivity<InspectionListActivity>()
                        1 -> startActivity<WorkTicketListActivity>()
                        2 -> startActivity<FaultWarningListActivity>()
                        3 -> startActivity<InventoryWarningListActivity>()
                        4 -> startActivity<SchedulingDecisionActivity>()
                    }
                }
    }


}