package com.zgdj.project.ui.work.other

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jhj.slimadapter.SlimAdapter
import com.jhj.slimadapter.holder.ViewInjector
import com.zgdj.lib.BaseApplication
import com.zgdj.lib.base.activity.BaseCommonListActivity
import com.zgdj.lib.config.Config
import com.zgdj.lib.extention.readAssets
import com.zgdj.lib.ui.X5WebViewActivity
import com.zgdj.project.KnowledgeBaseInfoBean
import com.zgdj.project.R
import org.jetbrains.anko.startActivity

class KnowledgeBaseInfoActivity : BaseCommonListActivity<KnowledgeBaseInfoBean>() {

    override val title: String
        get() = intent.getStringExtra(Config.TITLE)
    override val itemLayoutRes: Int
        get() = R.layout.list_item_file
    override val hasSplitLine: Boolean
        get() = false

    override fun getDataList(): List<KnowledgeBaseInfoBean> {
        val str = readAssets("data_5_2_2.json")
        return Gson().fromJson(str, object : TypeToken<List<KnowledgeBaseInfoBean>>() {}.type)
    }

    override fun itemViewConvert(adapter: SlimAdapter, injector: ViewInjector, bean: KnowledgeBaseInfoBean, position: Int) {
        injector.text(R.id.tv_title, bean.title)
                .text(R.id.tv_name, bean.create)
                .text(R.id.tv_time, bean.time)
                .clicked {
                    if (BaseApplication.isTBSLoadingSuccess) {
                        startActivity<LocalFileDisplayActivity>()
                    } else {
                        startActivity<X5WebViewActivity>()
                    }
                }
    }
}