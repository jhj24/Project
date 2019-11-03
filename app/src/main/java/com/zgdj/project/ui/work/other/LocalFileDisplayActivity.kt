package com.zgdj.project.ui.work.other

import android.Manifest
import android.os.Bundle
import com.zgdj.lib.base.activity.BaseActivity
import com.zgdj.lib.utils.FileUtils
import com.zgdj.lib.utils.StatusBarUtil
import com.zgdj.project.R
import kotlinx.android.synthetic.main.activity_local_file_display.*


class LocalFileDisplayActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_file_display)
        StatusBarUtil.setLightMode(this)

        requestPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE) {

            val file = "android.resource://com.zgdj.project/" + R.raw.video
            val inputStream = resources.assets.open("file.pdf")
            file_view.displayFile(FileUtils.streamToFile(inputStream, FileUtils.getSDPath("file") + "file.pdf"))

            file_view.show()
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        file_view.onStopDisplay()
    }
}
