package com.zgdj.project.ui.work.ywjx

import android.os.Bundle
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.zgdj.lib.base.activity.DefaultTopBarActivity
import com.zgdj.lib.config.Config
import com.zgdj.lib.utils.activityresult.ActivityResult
import com.zgdj.project.R
import kotlinx.android.synthetic.main.activity_qr_code.*
import org.jetbrains.anko.backgroundColor

class InspectionQRCodeActivity : DefaultTopBarActivity() {

    override val title: String
        get() = "二维码"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_code)
        statusBar.backgroundColor = 0xff000000.toInt()
        topBar.backgroundColor = 0xdd000000.toInt()

        qr_code.setDelegate(object : QRCodeView.Delegate {
            override fun onScanQRCodeSuccess(result: String?) {
                ActivityResult.with(this@InspectionQRCodeActivity)
                        .putString(Config.DATA, result ?: "")
                        .finish()
            }

            override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {

            }

            override fun onScanQRCodeOpenCameraError() {

            }
        })
    }

    override fun onStart() {
        super.onStart()
        qr_code.startCamera()
        qr_code.startSpotAndShowRect()
    }

    override fun onStop() {
        super.onStop()
        qr_code.stopCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        qr_code.onDestroy()
    }


}