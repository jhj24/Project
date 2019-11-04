package com.zgdj.project.ui.message

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

class ChildMessageFragment : BaseCommonListFragment<MessageBean>() {

    override val hasSplitLine: Boolean
        get() = false

    override val itemLayoutRes: Int
        get() = R.layout.list_item_message

    var list = arrayListOf<MessageBean>()


    override fun getDataList(): List<MessageBean> {
        val str = arguments?.getString(Config.DATA)
        list = if (str.isNullOrBlank()) arrayListOf<MessageBean>() else Gson().fromJson(str, object : TypeToken<List<MessageBean>>() {}.type)
        return list
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val type = arguments?.getInt(Config.TYPE)
        LiveDataBus.get()
                .with("message", MessageBean::class.java)
                .observe(this, Observer {
                    if (it != null) {
                        val index = adapterLocal.getDataList().indexOf(it)
                        adapterLocal.remove(index)
                        LiveDataBus.get().with("number").value = adapterLocal.getDataList().size
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
                        0 -> {
                            startActivity<InspectionListActivity>()
                            LiveDataBus.get().with("message").value = bean
                        }
                        1 -> {
                            startActivity<WorkTicketListActivity>()
                            LiveDataBus.get().with("message").value = bean
                        }
                        2 -> {
                            startActivity<FaultWarningListActivity>()
                            LiveDataBus.get().with("message").value = bean
                        }
                        3 -> {
                            startActivity<InventoryWarningListActivity>()
                            LiveDataBus.get().with("message").value = bean
                        }
                        4 -> {
                            startActivity<SchedulingDecisionActivity>()
                            LiveDataBus.get().with("message").value = bean
                        }
                    }
                }
    }


}