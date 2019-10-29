package com.zgdj.amap3d

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.DrawableRes
import com.amap.api.maps.*
import com.amap.api.maps.model.*
import com.zgdj.lib.R
import com.zgdj.lib.base.activity.BaseActivity
import com.zgdj.lib.config.UrlConfig
import com.zgdj.lib.utils.FileUtils
import com.zgdj.lib.utils.permissions.PermissionsCheck
import java.io.File
import java.net.URL

abstract class BaseAMapActivity : BaseActivity() {


    abstract val layoutResID: Int
    abstract val mapView: TextureMapView

    //定位参数
    var aMap: AMap? = null
    val scaleSize = 15f
    private val locationIconRes = R.mipmap.ic_amap_marker_default

    open val isScaleControlsEnabled = true
    open val isLocation = true
    open val isZoomControlsEnabled = false
    open val isCompassEnabled = false
    open val isTileOverlay = true
    open val isMarkerClickable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResID)
        mapView.onCreate(savedInstanceState)
        if (aMap == null) {
            aMap = mapView.map
        }
        //缩放按钮是否显示,默认显示且在右下
        aMap?.uiSettings?.isZoomControlsEnabled = isZoomControlsEnabled
        aMap?.uiSettings?.zoomPosition = AMapOptions.ZOOM_POSITION_RIGHT_BUTTOM


        //指南针是否显示，默认不显示
        aMap?.uiSettings?.isCompassEnabled = isCompassEnabled

        //控制比例尺控件是否显示
        aMap?.uiSettings?.isScaleControlsEnabled = isScaleControlsEnabled

        //aMap?.minZoomLevel = 14f
        //aMap?.maxZoomLevel = 18f

        if (isTileOverlay) {
            drawTiles()
        }
        aMap?.setOnMarkerClickListener {
            return@setOnMarkerClickListener !isMarkerClickable
        }
    }

    /**
     * 定位
     */
    fun location(body: (LatLng) -> Unit = {}) {
        requestPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION) {
            //定位蓝点样式
            val myLocationStyle = MyLocationStyle()
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) //只定位一次
            myLocationStyle.strokeWidth(0f)//设置定位蓝点精度圈的边框宽度的方法。
            myLocationStyle.strokeColor(Color.TRANSPARENT)//设置定位蓝点精度圆圈的边框颜色的方法。
            myLocationStyle.radiusFillColor(Color.TRANSPARENT)//设置定位蓝点精度圆圈的填充颜色的方法。
            myLocationStyle.showMyLocation(true)//用于满足只想使用定位，不想使用定位小蓝点的场景
            myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(locationIconRes))
            //myLocationStyle.interval(2000); //设置连续定位间隔，只在连续定位模式下生效

            //设置定位蓝点的Style
            aMap?.myLocationStyle = myLocationStyle
            aMap?.isMyLocationEnabled = true// 是否启用定位蓝点，默认是false，不定位不显示蓝点。

            //定位按钮是否显示，默认不显示
            aMap?.uiSettings?.isMyLocationButtonEnabled = isLocation

            //经过定位，获取经纬度信息：
            aMap?.setOnMyLocationChangeListener { location ->
                val bundle = location.extras
                when (bundle.getInt("errorCode")) {
                    0 -> {
                        aMap?.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    location.latitude,
                                    location.longitude
                                ), scaleSize
                            )
                        )
                        body(LatLng(location.latitude, location.longitude))
                    }
                    12 -> {
                        requestPermissions(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) {
                            location()
                        }
                    }
                    else -> {
                        aMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(39.908823, 116.397470), scaleSize))
                        body(LatLng(39.908823, 116.397470))
                    }
                }
            }
        }
    }

    //
    fun DD2DMS(latLng: Double): String {
        val d = latLng.toInt()
        val m = ((latLng - d) * 60).toInt()
        val s = ((latLng - d - m / 60) * 3600).toInt()
        return "$d°$m′$s″"
    }


    //计算多个经纬度距离
    fun calculateLineDistance(list: List<LatLng>): Float {
        val iterator = list.iterator()
        var distance = 0F
        if (!iterator.hasNext()) {
            return distance
        }
        var accumulator = iterator.next()
        while (iterator.hasNext()) {
            val nextPoint = iterator.next()
            distance += AMapUtils.calculateLineDistance(accumulator, nextPoint)
            accumulator = nextPoint
        }
        return distance
    }

    fun moveCenter(latLng: LatLng) = aMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, scaleSize))

    //将多个坐标点移动到可视区域
    fun moveCenter(latLngList: List<LatLng>) {
        val boundsBuilder = LatLngBounds.Builder()
        latLngList.forEach {
            boundsBuilder.include(LatLng(it.latitude, it.longitude))
        }
        // 设置所有maker显示在当前可视区域地图中
        val bounds = boundsBuilder.build()
        aMap?.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150))
    }

    //画marker
    fun drawMarker(
        latLng: LatLng, isDraggable: Boolean = false, title: String? = "", anchorX: Float = 0.5f, anchorY: Float = 1.0f,
        marker: Int = R.mipmap.ic_amap_marker_default
    ): Marker? {

        val markerOption = MarkerOptions()
            .position(latLng)
            .draggable(isDraggable)//设置Marker可拖动
            .anchor(anchorX, anchorY)
            .icon(BitmapDescriptorFactory.fromResource(marker))
            .zIndex(100f)
        if (title != null) {
            markerOption.title(title)
        }
        return aMap?.addMarker(markerOption)
    }

    //画线
    fun drawLine(
        latLng: List<LatLng>,
        width: Float = 10f,
        color: Int = Color.argb(255, 1, 1, 1), @DrawableRes texture: Int? = null,
        zIndex: Float = 100f
    ): Polyline? {
        val polyLine = PolylineOptions()
        if (texture != null) {
            polyLine.customTexture = BitmapDescriptorFactory.fromResource(R.mipmap.ic_amap_custtexure)
        }
        polyLine
            .addAll(latLng)
            .width(width)
            .color(color)
            .zIndex(zIndex)
        return aMap?.addPolyline(polyLine)
    }


    //画多边形
    fun drawPolygon(
        latLng: List<LatLng>, strokeWidth: Float = 5f, strokeColor: Int = Color.argb(180, 63, 145, 252),
        fillColor: Int = Color.argb(163, 118, 212, 243)
    ): Polygon? {
        val polygonOption = PolygonOptions()
            .addAll(latLng)
            .strokeColor(strokeColor)
            .fillColor(fillColor)
            .strokeWidth(strokeWidth)
            .zIndex(100f)
        return aMap?.addPolygon(polygonOption)
    }

    //画文字
    fun drawText(latLng: LatLng, customId: String): Text? {
        val textOptions = TextOptions()
        textOptions
            .text(customId)
            .position(latLng)
            .zIndex(100f)
        return aMap?.addText(textOptions)
    }

    fun drawTiles() {
        val googleUrl = "http://mt1.google.cn/vt/lyrs=y&hl=zh-CN&gl=cn&z=%d&x=%d&s=&y=%d"   //3D卫星地图
        val localUrl = UrlConfig.MAP_URL + UrlConfig.GOOGLE_TILES
        tilesOverlay(googleUrl, "google", 1f)
        tilesOverlay(localUrl, "local", 2f)
    }

    /**
     * 瓦片图层
     */
    private fun tilesOverlay(url: String, cachePath: String, zIndex: Float) {
        val titleOverlayOptions = TileOverlayOptions()
            .tileProvider(object : UrlTileProvider(256, 256) {
                override fun getTileUrl(x: Int, y: Int, zoom: Int): URL? {
                    try {
                        val fileName = "x${x}_y${y}_zoom${zoom + 1}"
                        val filePath = FileUtils.getSDPath("amap/$cachePath/L${zoom + 1}") + fileName
                        var urlPath = ""
                        PermissionsCheck.with(this@BaseAMapActivity)
                            .requestPermissions(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            )
                            .onPermissionsResult { deniedPermissions, allPermissions ->
                                if (deniedPermissions.isEmpty()) {
                                    if (File(filePath).exists()) {
                                        urlPath = "file://$filePath"
                                    } else {
                                        urlPath = String.format(url, zoom, x, y)
                                        val bitmap = FileUtils.getUrlToBitmap(urlPath)
                                        FileUtils.saveFile(bitmap, filePath)
                                    }
                                } else {
                                    urlPath = String.format(url, zoom, x, y)
                                }
                            }
                        return URL(urlPath)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    return null
                }
            })
            .diskCacheEnabled(false)
            .memoryCacheEnabled(false)
            .zIndex(zIndex)
        aMap?.addTileOverlay(titleOverlayOptions)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}