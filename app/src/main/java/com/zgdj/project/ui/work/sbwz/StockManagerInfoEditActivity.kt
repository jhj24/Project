package com.zgdj.project.ui.work.sbwz

import android.os.Bundle
import com.zgdj.lib.base.activity.DefaultTopBarActivity
import com.zgdj.lib.config.Config
import com.zgdj.lib.extention.isNumber
import com.zgdj.lib.utils.activityresult.ActivityResult
import com.zgdj.project.R
import com.zgdj.project.StockManagerInfoBean
import kotlinx.android.synthetic.main.activity_stock_manager_info.*
import org.jetbrains.anko.toast

class StockManagerInfoEditActivity : DefaultTopBarActivity() {

    override val title: String
        get() = intent.getStringExtra(Config.TITLE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_manager_info)

        layout_operate.setOnCommitListener {
            if (label_title.isBlank() || label_code.isBlank() || label_unit.isBlank() || label_secure_sum.isBlank() || label_exit_sum.isBlank()) {
                return@setOnCommitListener
            }
            if (!label_secure_sum.getInputText().isNumber()) {
                toast("请输入正确的安全库存")
                return@setOnCommitListener
            }
            if (!label_exit_sum.getInputText().isNumber()) {
                toast("请输入正确的现有库存")
                return@setOnCommitListener
            }
            val data = StockManagerInfoBean(
                    label_title.getInputText(),
                    label_code.getInputText(),
                    label_unit.getInputText(),
                    label_secure_sum.getInputText().toInt(),
                    label_exit_sum.getInputText().toInt())
            ActivityResult.with(this)
                    .putSerializable(Config.DATA, data)
                    .finish()

        }

    }
}