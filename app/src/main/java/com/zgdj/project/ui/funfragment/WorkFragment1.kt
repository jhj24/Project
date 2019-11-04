package com.zgdj.project.ui.funfragment

import android.Manifest
import android.os.Bundle
import android.view.View
import com.zgdj.lib.base.fragment.BaseFragment
import com.zgdj.project.R
import com.zgdj.project.ui.MainActivity
import com.zgdj.project.ui.QRCodeActivity
import com.zgdj.project.ui.work.jkjc.DataDetectActivity
import com.zgdj.project.ui.work.jkjc.VideoMonitorListActivity
import com.zgdj.project.ui.work.other.KnowledgeBaseListActivity
import com.zgdj.project.ui.work.other.SchedulingDecisionActivity
import com.zgdj.project.ui.work.sbwz.DeviceInfoListActivity
import com.zgdj.project.ui.work.sbwz.StockManagerListActivity
import com.zgdj.project.ui.work.yjgl.FaultWarningListActivity
import com.zgdj.project.ui.work.yjgl.InventoryWarningListActivity
import com.zgdj.project.ui.work.ywjx.InspectionListActivity
import com.zgdj.project.ui.work.ywjx.OperateTicketListActivity
import com.zgdj.project.ui.work.ywjx.WorkTicketListActivity
import kotlinx.android.synthetic.main.fragment_wrok_fragment.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.startActivity

class WorkFragment1 : BaseFragment() {
    override val layoutRes: Int
        get() = R.layout.fragment_wrok_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.tv_main_title.text = "工作"
        view.iv_main_scan.onClick {
            (mActivity as MainActivity).requestPermissions(Manifest.permission.CAMERA) {
                startActivity<QRCodeActivity>()
            }
        }
        view.layout_device_info.onClick {
            startActivity<DeviceInfoListActivity>()
        }
        view.layout_stock_manager.onClick {
            startActivity<StockManagerListActivity>()
        }
        view.layout_inspection.onClick {
            startActivity<InspectionListActivity>()
        }
        view.layout_work_ticket.onClick {
            startActivity<WorkTicketListActivity>()
        }
        view.layout_operate.onClick {
            startActivity<OperateTicketListActivity>()
        }
        view.layout_fault_warning.onClick {
            startActivity<FaultWarningListActivity>()
        }
        view.layout_inventory_warning.onClick {
            startActivity<InventoryWarningListActivity>()
        }
        view.layout_video_monitor.onClick {
            startActivity<VideoMonitorListActivity>()
        }
        view.layout_data_detect.onClick {
            startActivity<DataDetectActivity>()
        }
        view.layout_scheduling.onClick {
            startActivity<SchedulingDecisionActivity>()
        }
        view.layout_knowledge.onClick {
            startActivity<KnowledgeBaseListActivity>()
        }
    }
}