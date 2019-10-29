package com.zgdj.amap3d

import android.content.Context
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import org.jetbrains.anko.toast


/**
 * 定位
 * Created by jhj on 18-8-22.
 */
class LocationUtils(context: Context, body: LocationUtils.(LocationBean) -> Unit) {

    //声明AMapLocationClient类对象
    private var mLocationClient: AMapLocationClient? = null
    private var defaultOption = getDefaultOption()

    init {
        synchronized(this) {
            mLocationClient = AMapLocationClient(context)
            //设置定位回调监听
            mLocationClient?.setLocationListener { location ->
                val locationBean = LocationBean()
                locationBean.errorCode = location.errorCode
                locationBean.errorInfo = location.errorInfo
                locationBean.time = location.time
                locationBean.latitude = location.latitude
                locationBean.longitude = location.longitude
                locationBean.country = location.country
                locationBean.province = location.province
                locationBean.city = location.city
                locationBean.cityCode = location.cityCode
                locationBean.district = location.district
                locationBean.address = location.address
                locationBean.adCode = location.adCode
                when (location.errorCode) {
                    0 -> {
                        body(locationBean)
                        stop()
                    }
                    12 -> context.toast("缺少定位权限")
                    4 -> context.toast("网络链接异常")
                    else -> context.toast("定位失败")
                }
            }
        }
    }

    fun start() {
        synchronized(this) {
            if (null != mLocationClient) {
                mLocationClient?.setLocationOption(defaultOption)
                //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
                mLocationClient?.stopLocation()
                mLocationClient?.startLocation()
            }
        }

    }

    fun stop() {
        synchronized(this) {
            mLocationClient?.stopLocation()
        }

    }

    fun destroy() {
        synchronized(this) {
            mLocationClient?.onDestroy()
        }

    }

    /**
     * 创建AMapLocationClientOption对象,用来设置发起定位的模式和相关参数。
     */
    private fun getDefaultOption(): AMapLocationClientOption {
        val mLocationOption = AMapLocationClientOption()

        /*
         * 设置定位模式
         *
         * 高精度模式-Hight_Accuracy
         * 低功耗模式-Battery_Saving
         * 仅设备模式-Device_Sensors
         *
         */
        mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy

        /*
         * 缓存机制
         *
         * true - 高精度和低功耗模式的定位结果会生成本地缓存，设备模式不会被缓存
         * false - 不缓存
         *
         */
        mLocationOption.isLocationCacheEnable = true

        /*
         * 获取3s内精度最高的一次定位
         * true -  setOnceLocation(boolean b) 接口也会被设置为true
         * false - 不是设置setOnceLocation(boolean b)
         *
         */
        //mLocationOption.isOnceLocationLatest = true;


        //获取一次定位结果，默认为false。
        mLocationOption.isOnceLocation = true


        //设置是否返回地址信息，默认为true
        mLocationOption.isNeedAddress = true
        //设置是否强制刷新WIFI，默认为true，强制刷新。每次定位主动刷新WIFI模块会提升WIFI定位精度，但相应的会多付出一些电量消耗。
        mLocationOption.isWifiActiveScan = false

        //设置是否允许模拟位置,默认为false
        mLocationOption.isMockEnable = true

        //设置自定义连续定位,默认间隔2000ms，最低1000ms。
        //mLocationOption.interval = 1000;

        //设置定位请求超时时间，默认为30秒。建议超时时间不要低于8000毫秒。
        //mLocationOption.httpTimeOut = 20000;
        return mLocationOption
    }


}