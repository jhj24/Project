package com.zgdj.project.ui.work.sbwz

import android.os.Bundle
import com.zgdj.lib.base.activity.DefaultTopBarActivity
import com.zgdj.lib.config.Config
import com.zgdj.lib.extention.closeKeyboard
import com.zgdj.lib.extention.isNumber
import com.zgdj.lib.utils.activityresult.ActivityResult
import com.zgdj.project.R
import kotlinx.android.synthetic.main.activity_stock_manager_info.layout_operate
import kotlinx.android.synthetic.main.activity_stock_out_or_in.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast

class StockOutOrInActivity : DefaultTopBarActivity() {

    override val title: String
        get() = intent.getStringExtra(Config.TITLE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_out_or_in)
        val type = intent.getStringExtra(Config.TYPE)
        val num = intent.getIntExtra(Config.NUM, 0)

        layout_operate.onClick {
            if (label_num.isBlank()) {
                return@onClick
            }
            if (!label_num.getInputText().isNumber()) {
                toast("请输入正确的数量")
                return@onClick
            }
            if (type == "out" && num < label_num.getInputText().toInt()) {
                toast("出库失败，出库数量大于当前库存")
                return@onClick
            }

            ActivityResult.with(this@StockOutOrInActivity)
                    .putInt(Config.NUM, label_num.getInputText().toInt())
                    .finish()

            closeKeyboard(layout_operate)
        }
    }
}