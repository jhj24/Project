package com.zgdj.project.ui.work.jkjc

import android.widget.ImageView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jhj.slimadapter.SlimAdapter
import com.jhj.slimadapter.holder.ViewInjector
import com.zgdj.lib.base.activity.BaseCommonListActivity
import com.zgdj.lib.config.Config
import com.zgdj.lib.extention.glide
import com.zgdj.lib.extention.readAssets
import com.zgdj.project.R
import org.jetbrains.anko.startActivity

class VideoMonitorListActivity : BaseCommonListActivity<String>() {

    override val title: String
        get() = "视频监控"

    override val itemLayoutRes: Int
        get() = R.layout.list_item_video_monitor

    override fun getDataList(): List<String> {
        val str = readAssets("data_4_1.json")
        return Gson().fromJson(str, object : TypeToken<List<String>>() {}.type)
    }

    override fun itemViewConvert(adapter: SlimAdapter, injector: ViewInjector, bean: String, position: Int) {
        injector.text(R.id.tv_title, bean)
                .clicked {
                    startActivity<VideoMonitorActivity>(Config.TITLE to bean)
                }
                .with<ImageView>(R.id.image_view) {
                    when (bean) {
                        "5#泵组上方" -> it.glide(R.mipmap.bzsf)
                        "厂房中控室" -> it.glide(R.mipmap.cfzks)
                        "叠梁门桥头" -> it.glide(R.mipmap.qlmqt)
                        "水闸上游左岸" -> it.glide(R.mipmap.szzasy)
                        "水闸下游全景" -> it.glide(R.mipmap.szsyqj)
                        "水闸下游左岸" -> it.glide(R.mipmap.szxyza)
                    }
                }
    }


}