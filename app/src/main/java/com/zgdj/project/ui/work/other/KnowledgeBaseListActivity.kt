package com.zgdj.project.ui.work.other

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jhj.slimadapter.SlimAdapter
import com.jhj.slimadapter.holder.ViewInjector
import com.zgdj.lib.base.activity.BaseCommonListActivity
import com.zgdj.lib.config.Config
import com.zgdj.lib.extention.readAssets
import com.zgdj.project.KnowledgeBaseBean
import com.zgdj.project.R
import org.jetbrains.anko.startActivity

class KnowledgeBaseListActivity : BaseCommonListActivity<KnowledgeBaseBean>() {
    override val title: String
        get() = "知识库"
    override val itemLayoutRes: Int
        get() = R.layout.list_item_knowledge_base

    override val inputSearch: Boolean
        get() = true

    override val filterFunc: (KnowledgeBaseBean, String) -> Boolean = { bean, str ->
        bean.name.contains(str)
    }

    override fun getDataList(): List<KnowledgeBaseBean> {
        val list = listOf("沙井泵站", "潭头水闸")
        val data = intent.getParcelableArrayListExtra<KnowledgeBaseBean>(Config.DATA)
        if (data.isNullOrEmpty()) {
            val str = readAssets("data_5_2.json")
            return Gson().fromJson(str, object : TypeToken<List<KnowledgeBaseBean>>() {}.type)
        } else {
            return data
        }


    }

    override fun itemViewConvert(adapter: SlimAdapter, injector: ViewInjector, bean: KnowledgeBaseBean, position: Int) {
        injector.text(R.id.tv_title, bean.name)
            .image(R.id.iv_folder, if (bean.level == 1) R.mipmap.ic_work_file else R.mipmap.ic_work_folder)
            .clicked {
                if (bean.level == 1) {
                    startActivity<KnowledgeBaseInfoActivity>(Config.TITLE to bean.name)
                } else {
                    startActivity<KnowledgeBaseListActivity>(Config.DATA to bean.children)
                }
            }
    }
}