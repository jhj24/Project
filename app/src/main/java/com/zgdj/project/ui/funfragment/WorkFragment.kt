package com.zgdj.project.ui.funfragment

import com.zgdj.lib.ui.BaseWorkFragment
import com.zgdj.project.R
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
import kotlinx.android.synthetic.main.activity_main.*

class WorkFragment : BaseWorkFragment() {

    override val dataList: List<WorkBean>
        get() = listOf(
            WorkBean(
                "设备物资", listOf(
                    WorkBean.ItemBean(R.drawable.svg_stock_manager, "设备信息", DeviceInfoListActivity::class.java),
                    WorkBean.ItemBean(R.drawable.svg_device_info, "库存管理", StockManagerListActivity::class.java)
                )
            ),
            WorkBean(
                "运维检修", listOf(
                    WorkBean.ItemBean(R.drawable.svg_inspection, "现场巡检", InspectionListActivity::class.java),
                    WorkBean.ItemBean(R.drawable.svg_work_ticket, "工作票", WorkTicketListActivity::class.java),
                    WorkBean.ItemBean(R.drawable.svg_operate_ticket, "操作票", OperateTicketListActivity::class.java)
                )
            ),
            WorkBean(
                "预警管理", listOf(
                    WorkBean.ItemBean(R.drawable.svg_fault_warning, "故障预警", FaultWarningListActivity::class.java),
                    WorkBean.ItemBean(
                        R.drawable.svg_inventory_warning,
                        "库存预警",
                        InventoryWarningListActivity::class.java
                    )
                )
            ),
            WorkBean(
                "监控监测", listOf(
                    WorkBean.ItemBean(R.drawable.svg_video_monitor, "视频监控", VideoMonitorListActivity::class.java),
                    WorkBean.ItemBean(R.drawable.svg_data_detect, "监测数据", DataDetectActivity::class.java)
                )
            ),
            WorkBean(
                "其他", listOf(
                    WorkBean.ItemBean(R.drawable.svg_scheduling, "调度决策", SchedulingDecisionActivity::class.java),
                    WorkBean.ItemBean(R.drawable.svg_knowledge, "知识库", KnowledgeBaseListActivity::class.java)
                )
            )
        )

    override val navigationViewTop: Int
        get() = mActivity.navigation_view.top
}