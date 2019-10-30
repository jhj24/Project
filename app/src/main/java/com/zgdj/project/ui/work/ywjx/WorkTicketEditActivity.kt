package com.zgdj.project.ui.work.ywjx

import android.os.Bundle
import android.view.View
import com.zgdj.lib.base.activity.DefaultTopBarActivity
import com.zgdj.lib.config.Config
import com.zgdj.lib.extention.formatBooleanArray
import com.zgdj.lib.extention.parse
import com.zgdj.lib.extention.sdf
import com.zgdj.lib.extention.timePick
import com.zgdj.lib.utils.activityresult.ActivityResult
import com.zgdj.project.R
import kotlinx.android.synthetic.main.activity_work_ticket_edit.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class WorkTicketEditActivity : DefaultTopBarActivity() {

    var type: String = ""
    var status: String = ""

    override val title: String
        get() = "工作票"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_ticket_edit)
        type = intent.getStringExtra(Config.TYPE)
        status = intent.getStringExtra(Config.STATUS)
        when (status) {
            "未开始" -> {
                layout_licensor.visibility = View.GONE
            }
            "进行中" -> {
                layout_licensor.visibility = View.VISIBLE
            }
            "已完成" -> {
                layout_licensor.visibility = View.VISIBLE
            }
        }
        when (type) {
            "add" -> {
                editable(true)
                addOrEdit()
            }
            "read" -> {
                editable(false)
                layout_operate.visibility = View.GONE
                info()
            }
            "edit" -> {
                editable(true)
                info()
                addOrEdit()
                layout_operate.setCommitText("修改")
            }
            "approval" -> {
                editable(false)
                approval()
                info()
            }
        }

    }

    fun info() {
        label_unit.setInputText("广汇源环境水务有限公司")
        label_code.setInputText("0000099")
        label_principal.setInputText("黄廉文")
        label_people_num.setInputText("7")
        label_work_people.setInputText("黄廉文，梁必杰，古丽红，李扬标，张美团，张德亮等")
        label_task.setInputText("河口中孔卷扬机清洁维护，检查润滑油，加注润滑油，清扫锁定装置。")
        label_start_time.setInputText("2019-07-22 08:35")
        label_end_time.setInputText("2019-07-22 17:00")
        label_secure.setInputText("穿戴安全绳、安全帽、绝缘鞋。做好防护措施与监护工作。")
        label_signer.setInputText("徐保田")
        label_signer_time.setInputText("2019-07-22")

        if (status == "已完成") {
            label_licensor_secure.setInputText("做好防护措施")
            label_licensor.setInputText("李朝辉")
            label_licensor_time.setInputText("2019-07-22")
        }
    }

    fun editable(isEditable: Boolean) {
        label_unit.setCanInput(isEditable)
        label_code.setCanInput(isEditable)
        label_principal.setCanInput(isEditable)
        label_people_num.setCanInput(isEditable)
        label_work_people.setCanInput(isEditable)
        label_task.setCanInput(isEditable)
        label_secure.setCanInput(isEditable)
        label_signer.setCanInput(isEditable)
        label_licensor_secure.setCanInput(isEditable)
        label_licensor.setCanInput(isEditable)
        if (!isEditable) {
            label_start_time.setShowArrow(false)
            label_end_time.setShowArrow(false)
            label_signer_time.setShowArrow(false)
            label_licensor_time.setShowArrow(false)
        }
    }

    fun approval() {
        label_licensor_secure.setCanInput(true)
        label_licensor.setCanInput(true)
        label_licensor_time.setShowArrow(true)
        label_licensor_time.onClick {
            val pattern = "yyyy-MM-dd"
            val data = pattern.parse(label_licensor_time.getInputText())
            timePick(data, pattern.formatBooleanArray) {
                label_licensor_time.setInputText(pattern.sdf(it))
            }
        }
        layout_operate.setOnCommitListener {
            if (label_licensor_secure.isBlank() or label_licensor.isBlank() or label_licensor_time.isBlank()) {
                return@setOnCommitListener
            }
            ActivityResult.with(this)
                    .putString("unit", label_unit.getInputText())
                    .putString("code", label_code.getInputText())
                    .putString("time", label_signer_time.getInputText())
                    .putString("signer", label_signer.getInputText())
                    .finish()
        }
    }

    fun addOrEdit() {
        label_start_time.onClick {
            val pattern = "yyyy-MM-dd HH:mm"
            val startData = pattern.parse(label_start_time.getInputText())
            val endData = pattern.parse(label_end_time.getInputText())
            timePick(startData, endData, true, pattern.formatBooleanArray, body = {
                label_start_time.setInputText(pattern.sdf(it))
            })
        }

        label_end_time.onClick {
            val pattern = "yyyy-MM-dd HH:mm"
            val startData = pattern.parse(label_start_time.getInputText())
            val endData = pattern.parse(label_end_time.getInputText())
            timePick(startData, endData, false, pattern.formatBooleanArray, body = {
                label_end_time.setInputText(pattern.sdf(it))
            })
        }

        label_signer_time.onClick {
            val pattern = "yyyy-MM-dd"
            val data = pattern.parse(label_signer_time.getInputText())
            timePick(data, pattern.formatBooleanArray) {
                label_signer_time.setInputText(pattern.sdf(it))
            }
        }

        layout_operate.setOnCommitListener {
            if (label_unit.isBlank() or label_code.isBlank() or label_signer_time.isBlank() or label_signer.isBlank()) {
                return@setOnCommitListener
            }
            ActivityResult.with(this)
                    .putString("unit", label_unit.getInputText())
                    .putString("code", label_code.getInputText())
                    .putString("time", label_signer_time.getInputText())
                    .putString("signer", label_signer.getInputText())
                    .finish()
        }
    }

}