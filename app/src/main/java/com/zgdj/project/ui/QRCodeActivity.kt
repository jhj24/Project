package com.zgdj.project.ui

import android.os.Bundle
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.zgdj.lib.base.activity.DefaultTopBarActivity
import com.zgdj.lib.config.Config
import com.zgdj.project.R
import com.zgdj.project.ui.work.sbwz.DeviceInfoEditActivity
import kotlinx.android.synthetic.main.activity_qr_code.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.startActivity

class QRCodeActivity : DefaultTopBarActivity() {

    override val title: String
        get() = "二维码"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_code)
        statusBar.backgroundColor = 0xff000000.toInt()
        topBar.backgroundColor = 0xdd000000.toInt()

        qr_code.setDelegate(object : QRCodeView.Delegate {
            override fun onScanQRCodeSuccess(result: String?) {
                startActivity<DeviceInfoEditActivity>(
                        Config.TYPE to "read",
                        "name" to "1#主水泵",
                        "code" to "3300ZGB34-223",
                        "factory" to "日立泵（无锡）"
                )
                finish()
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