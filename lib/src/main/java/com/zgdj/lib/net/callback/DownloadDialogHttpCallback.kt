package com.zgdj.lib.net.callback

import android.app.Activity
import com.jhj.httplibrary.callback.DownloadHttpCallback
import com.zgdj.lib.extention.downloadDialog
import java.io.File

abstract class DownloadDialogHttpCallback(private val activity: Activity, var downloadFile: File) :
    DownloadHttpCallback(activity, downloadFile) {

    var dialog = mActivity.downloadDialog(mActivity)

    override fun onProgress(percent: Int) {
        mActivity.runOnUiThread {
            dialog.setProgress(percent)
            if (percent == dialog.getMaxProgress()) {
                dialog.dismiss()
            }
        }
    }

    override fun onFinish() {
        super.onFinish()
        if (dialog.isShow()) {
            dialog.dismiss()
        }
    }
}