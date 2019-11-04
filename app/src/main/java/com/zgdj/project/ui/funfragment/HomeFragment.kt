package com.zgdj.project.ui.funfragment

import android.Manifest
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jhj.slimadapter.SlimAdapter
import com.zgdj.lib.base.fragment.BaseFragment
import com.zgdj.lib.extention.glide
import com.zgdj.lib.extention.preventStuck
import com.zgdj.lib.extention.readAssets
import com.zgdj.lib.utils.StatusBarUtil
import com.zgdj.project.R
import com.zgdj.project.WeatherBean
import com.zgdj.project.ui.DescriptionActivity
import com.zgdj.project.ui.MainActivity
import com.zgdj.project.ui.QRCodeActivity
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.startActivity

class HomeFragment : BaseFragment() {


    override val layoutRes: Int
        get() = R.layout.fragment_home


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        StatusBarUtil.setLightMode(mActivity)
        view.tv_main_title.text = "首页"
        view.iv_main_scan.onClick {
            (mActivity as MainActivity).requestPermissions(Manifest.permission.CAMERA) {
                startActivity<QRCodeActivity>()
            }
        }


        val imageList = listOf(
                R.mipmap.banner_01,
                R.mipmap.banner_02,
                R.mipmap.banner_03,
                R.mipmap.banner_04
        )
        view.banner.setViewUrls(imageList)
        view.banner.setOnBannerItemClickListener {
            startActivity<DescriptionActivity>()
        }


        val str = mActivity.readAssets("weather.json")
        val dataList = Gson().fromJson<List<WeatherBean>>(str, object : TypeToken<List<WeatherBean>>() {}.type)
        view.recycler_view.preventStuck()
        view.recycler_view.layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
        SlimAdapter.creator()
                .register<WeatherBean>(R.layout.list_item_weather) { injector, bean, position ->
                    injector.text(R.id.tv_temperature, bean.temperature)
                            .text(R.id.tv_weather, bean.weather)
                            .text(R.id.tv_time, bean.time)
                            .with<ImageView>(R.id.iv_weather) {
                                when (bean.weather) {
                                    "晴" -> it.glide(R.drawable.ic_wb_sunny_black_24dp)
                                    "多云" -> it.glide(R.drawable.ic_cloudy_sunny)
                                    "雷阵雨" -> it.glide(R.drawable.ic_thunder_shower)
                                }
                            }

                }
                .attachTo(view.recycler_view)
                .setDataList(dataList)


    }

}