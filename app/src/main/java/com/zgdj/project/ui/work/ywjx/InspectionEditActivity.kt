package com.zgdj.project.ui.work.ywjx

import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jhj.slimadapter.SlimAdapter
import com.zgdj.lib.base.activity.DefaultTopBarActivity
import com.zgdj.lib.config.Config
import com.zgdj.lib.extention.*
import com.zgdj.lib.utils.activityresult.ActivityResult
import com.zgdj.lib.widget.SwitchButton
import com.zgdj.project.InspectionEditBean
import com.zgdj.project.R
import kotlinx.android.synthetic.main.activity_inspection_edit.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast

class InspectionEditActivity : DefaultTopBarActivity() {

    override val title: String
        get() = "现场巡检"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inspection_edit)
        val status = intent.getStringExtra(Config.STATUS)
        val str: String
        val pattern = "yyyy-MM-dd HH:mm:ss"
        if (status == "未开始") {
            str = readAssets("data_2_1_2.json")
            label_time.onClick {
                val date = pattern.parse(label_time.getInputText())
                timePick(date, pattern.formatBooleanArray) {
                    label_time.setInputText(pattern.sdf(it))
                }
            }
            layout_operate.setOnCommitListener {
                if (label_time.isBlank()) {
                    return@setOnCommitListener
                }
                ActivityResult.with(this)
                        .putString(Config.DATE, label_time.getInputText())
                        .finish()

            }
        } else if (status == "进行中") {
            info()
            str = readAssets("data_2_1_3.json")
            layout_operate.setOnCommitListener {
                if (label_time.isBlank()) {
                    return@setOnCommitListener
                }
                ActivityResult.with(this)
                        .finish()

            }
        } else {
            layout_operate.visibility = View.GONE
            str = readAssets("data_2_1_1.json")
            read()
            info()
        }
        val list = Gson().fromJson<List<InspectionEditBean>>(str, object : TypeToken<List<InspectionEditBean>>() {}.type)
        val adapter = SlimAdapter.creator()
                .register<InspectionEditBean>(R.layout.list_item_inspection_edit) { injector, bean, position ->
                    injector.text(R.id.tv_title, bean.title)
                            .image(R.id.iv_punch, if (bean.isPunch) R.mipmap.ic_punch else R.mipmap.ic_unpunch)
                    val switchButton = injector.getView<SwitchButton>(R.id.switch_button)
                    switchButton.isOpened = bean.isNormal
                    if (status == "已完成") {
                        switchButton.setOnTouchListener { v, event ->
                            return@setOnTouchListener true
                        }
                    }
                }
                .attachTo(recycler_view)
                .setDataList(list)


        if (status == "未开始" || status == "进行中") {

            topBarRightImage(R.drawable.ic_scan, 0xffffffff.toInt()) {
                ActivityResult.with(this)
                        .targetActivity(InspectionQRCodeActivity::class.java)
                        .onResult {
                            if (it != null) {
                                val data = it.getStringExtra(Config.DATA)
                                list.forEachIndexed { index, inspectionEditBean ->
                                    if (inspectionEditBean.title.replace("\n", "") == data) {
                                        toast("${data}已定位检测")
                                        adapter.getDataList<InspectionEditBean>()[index].isPunch = true
                                        adapter.notifyItemChanged(index)
                                    }
                                }
                            }
                        }
            }
        }

    }

    fun info() {

        label_time.setInputText("2019-7-30 7:30")
        label_10kv_1_ua.setInputText("6.09")
        label_10kv_1_ub.setInputText("6.09")
        label_10kv_1_uc.setInputText("6.09")
        label_10kv_2_ua.setInputText("6.09")
        label_10kv_2_ub.setInputText("6.09")
        label_10kv_2_uc.setInputText("6.09")
        label_04kv_1_ua.setInputText("0.23")
        label_04kv_1_ub.setInputText("0.23")
        label_04kv_1_uc.setInputText("0.23")
        label_04kv_2_ua.setInputText("0.23")
        label_04kv_2_ub.setInputText("0.23")
        label_04kv_2_uc.setInputText("0.23")

        label_voltage_0.setInputText("120")
        label_voltage_1.setInputText("110")
        label_voltage_2.setInputText("110")

        label_temperature_1_a.setInputText("48.8")
        label_temperature_1_b.setInputText("44.2")
        label_temperature_1_c.setInputText("49.0")
        label_temperature_1_d.setInputText("74.9")


        label_temperature_2_a.setInputText("49.4")
        label_temperature_2_b.setInputText("53.8")
        label_temperature_2_c.setInputText("48.6")
        label_temperature_2_d.setInputText("76.3")

        label_remark.setInputText("设备正常")
        label_patrol.setInputText("黄廉文")


    }

    fun read() {
        label_time.setShowArrow(false)
        label_10kv_1_ua.setCanInput(false)
        label_10kv_1_ub.setCanInput(false)
        label_10kv_1_uc.setCanInput(false)
        label_10kv_2_ua.setCanInput(false)
        label_10kv_2_ub.setCanInput(false)
        label_10kv_2_uc.setCanInput(false)
        label_04kv_1_ua.setCanInput(false)
        label_04kv_1_ub.setCanInput(false)
        label_04kv_1_uc.setCanInput(false)
        label_04kv_2_ua.setCanInput(false)
        label_04kv_2_ub.setCanInput(false)
        label_04kv_2_uc.setCanInput(false)

        label_voltage_0.setCanInput(false)
        label_voltage_1.setCanInput(false)
        label_voltage_2.setCanInput(false)

        label_temperature_1_a.setCanInput(false)
        label_temperature_1_b.setCanInput(false)
        label_temperature_1_c.setCanInput(false)
        label_temperature_1_d.setCanInput(false)


        label_temperature_2_a.setCanInput(false)
        label_temperature_2_b.setCanInput(false)
        label_temperature_2_c.setCanInput(false)
        label_temperature_2_d.setCanInput(false)

        label_remark.setCanInput(false)
        label_patrol.setCanInput(false)
    }
}