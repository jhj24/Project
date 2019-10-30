package com.zgdj.project.ui.work.sbwz

import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jhj.imageselector.ImageSelector
import com.zgdj.lib.base.activity.BaseActivity
import com.zgdj.lib.config.Config
import com.zgdj.lib.extention.glide
import com.zgdj.lib.utils.activityresult.ActivityResult
import com.zgdj.project.R
import kotlinx.android.synthetic.main.activity_device_info.*
import org.jetbrains.anko.dimen
import org.jetbrains.anko.dip
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick

class DeviceInfoEditActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_info)

        find<TextView>(R.id.tv_top_bar_title).text = "设备信息"
        find<ImageView>(R.id.iv_top_bar_back).visibility = View.VISIBLE
        find<ImageView>(R.id.iv_top_bar_back).onClick {
            onBackPressed()
        }
        val imageHeight = dip(220)
        val titleBarHeight = dimen(R.dimen.statusbar_titlebar_view_height)
        val topBar = find<View>(R.id.top_bar_background)
        topBar.alpha = 0f

        scroll_view.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { p0, p1, p2, p3, p4 ->
            if (p2 - dip(20) < imageHeight - titleBarHeight && p2 > imageHeight - titleBarHeight) {
                val percent = (p2 - (imageHeight - titleBarHeight).toFloat()) / dip(20)
                topBar.alpha = percent
            } else if (p2 < imageHeight - titleBarHeight) {
                topBar.alpha = 0f
            } else if (p2 - dip(20) > imageHeight - titleBarHeight) {
                topBar.alpha = 1f
            }
        })

        val type = intent.getStringExtra(Config.TYPE)
        val name = intent.getStringExtra("name")
        val code = intent.getStringExtra("code")
        val factory = intent.getStringExtra("factory")

        if (type == "add") {
            image_view.setImageResource(R.drawable.bg_camera)
            layout_operate.visibility = View.VISIBLE
            layout_operate.setCommitText("保存")
            add()

        } else if (type == "edit") {
            layout_operate.visibility = View.VISIBLE
            layout_operate.setCommitText("修改")
            edit(name, code, factory)
        } else {
            layout_operate.visibility = View.GONE
            read(name, code, factory)
        }



        label_name
        label_code
        label_factory
        label_position
        label_depart
        label_status
        label_add
        label_ABC
        label_assets
        label_device
        label_type
        label_pump_type
        label_pump_flow
        label_pump_head
        label_pump_effective
        label_pump_power
        label_pump_diameter


    }

    fun read(name: String, code: String, factory: String) {
        status(false)
        info(name, code, factory)
    }


    fun edit(name: String, code: String, factory: String) {
        status(true)
        info(name, code, factory)
        image_view.onClick {
            ImageSelector.singleSelected(this@DeviceInfoEditActivity, null) {
                image_view.glide(it.first().path)
            }
        }
        layout_operate.setOnCommitListener {
            if (label_name.isBlank() || label_code.isBlank() || label_factory.isBlank()) {
                return@setOnCommitListener
            }
            ActivityResult.with(this)
                    .putString("name", label_name.getInputText())
                    .putString("code", label_code.getInputText())
                    .putString("factory", label_factory.getInputText())
                    .finish()
        }
    }

    fun add() {
        image_view.onClick {
            ImageSelector.singleSelected(this@DeviceInfoEditActivity, null) {
                image_view.glide(it.first().path)
            }
        }
        layout_operate.setOnCommitListener {
            if (label_name.isBlank() || label_code.isBlank() || label_factory.isBlank()) {
                return@setOnCommitListener
            }
            ActivityResult.with(this)
                    .putString("name", label_name.getInputText())
                    .putString("code", label_code.getInputText())
                    .putString("factory", label_factory.getInputText())
                    .finish()
        }

    }

    private fun info(name: String, code: String, factory: String) {
        label_name.setInputText(name)
        label_code.setInputText(code)
        label_factory.setInputText(factory)
        label_position.setInputText("主机间")
        label_depart.setInputText("沙井泵站")
        label_status.setInputText("正常")
        label_add.setInputText("自购")
        label_ABC.setInputText("A")
        label_assets.setInputText("Y")
        label_device.setInputText("Y")
        label_type.setInputText("固定资产")
        label_pump_type.setInputText("竖井式贯流泵")
        label_pump_flow.setInputText("34")
        label_pump_head.setInputText("2.23")
        label_pump_effective.setInputText("89.5")
        label_pump_power.setInputText("860.5")
        label_pump_diameter.setInputText("3300")
    }

    fun status(isEditable: Boolean) {
        label_name.setCanInput(isEditable)
        label_code.setCanInput(isEditable)
        label_factory.setCanInput(isEditable)
        label_position.setCanInput(isEditable)
        label_depart.setCanInput(isEditable)
        label_status.setCanInput(isEditable)
        label_add.setCanInput(isEditable)
        label_ABC.setCanInput(isEditable)
        label_assets.setCanInput(isEditable)
        label_device.setCanInput(isEditable)
        label_type.setCanInput(isEditable)
        label_pump_type.setCanInput(isEditable)
        label_pump_flow.setCanInput(isEditable)
        label_pump_head.setCanInput(isEditable)
        label_pump_effective.setCanInput(isEditable)
        label_pump_power.setCanInput(isEditable)
        label_pump_diameter.setCanInput(isEditable)
    }


}