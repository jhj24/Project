package com.zgdj.project.ui.work.ywjx

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.jhj.slimadapter.SlimAdapter
import com.zgdj.lib.base.activity.DefaultTopBarActivity
import com.zgdj.lib.config.Config
import com.zgdj.lib.extention.preventStuck
import com.zgdj.lib.utils.activityresult.ActivityResult
import com.zgdj.project.OperateTicketBean
import com.zgdj.project.R
import com.zgdj.project.TicketBean
import kotlinx.android.synthetic.main.activity_operate_ticket_edit.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.sdk27.coroutines.onClick

class OperateTicketEditActivity : DefaultTopBarActivity() {
    override val title: String
        get() = intent.getStringExtra(Config.TITLE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_operate_ticket_edit)
        val data = intent.getSerializableExtra(Config.DATA)
        baseInfo(data as OperateTicketBean)

        val type = intent.getStringExtra(Config.TYPE)


        if (type == "read") {
            layout_operate.visibility = View.GONE
        } else if (type == "edit") {
            layout_operate.onClick {
                ActivityResult.with(this@OperateTicketEditActivity)
                        .putSerializable(Config.DATA, data)
                        .finish()
            }
        }

        recycler_view.preventStuck()
        SlimAdapter.creator()
                .register<TicketBean>(R.layout.list_item_operate_step) { injector, bean, position ->
                    val editText = injector.getView<EditText>(R.id.edit_text)
                    val imageView = injector.getView<ImageView>(R.id.image_view)
                    injector.text(R.id.text_view, bean.title)
                            .text(R.id.edit_text, bean.remark)
                            .with<EditText>(R.id.edit_text) {
                                it.addTextChangedListener(object : TextWatcher {
                                    override fun afterTextChanged(s: Editable?) {
                                        bean.remark = s.toString()
                                    }

                                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                                    }

                                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                                    }

                                })
                            }
                            .with<ImageView>(R.id.image_view) {
                                it.isSelected = bean.isSelector
                            }
                            .with<View>(R.id.line) {
                                if (bean.isSelector) {
                                    it.backgroundColor = 0xff00c9b4.toInt()
                                } else {
                                    it.backgroundColor = 0xffbebebe.toInt()
                                }
                            }

                    if (type == "read") {
                        editText.isFocusable = false
                        editText.isCursorVisible = false
                    } else {
                        imageView.onClick {
                            imageView.isSelected = !imageView.isSelected
                            bean.isSelector = imageView.isSelected
                            if (imageView.isSelected) {
                                injector.getView<View>(R.id.line).backgroundColor = 0xff00c9b4.toInt()
                            } else {
                                injector.getView<View>(R.id.line).backgroundColor = 0xffbebebe.toInt()
                            }
                        }
                    }

                }
                .attachTo(recycler_view)
                .setDataList(data.data)
    }

    fun baseInfo(data: OperateTicketBean?) {

        data?.let {
            label_state_name.setInputText(it.site_name)
            label_code.setInputText(it.code)
            label_machine_name.setInputText(it.machine_name)
            label_operate_task.setInputText(it.title)
            label_operate_people.setInputText("孙钰杰")
            label_guardian.setInputText("李金")
        }
    }


}