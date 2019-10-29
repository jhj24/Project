package com.zgdj.lib.net.callback

import android.app.Activity
import com.jhj.httplibrary.HttpCall
import com.zgdj.lib.extention.percentUploadDialog
import org.jetbrains.anko.toast

abstract class UploadDialogHttpCallback<T>(activity: Activity, val dialogText: String) : DataHttpCallback<T>(activity) {

    var dialog = mActivity.percentUploadDialog {
        HttpCall.cancelAll()
        mActivity.toast("取消上传...")
    }

    override fun onProgress(percent: Int) {
        mActivity.runOnUiThread {
            if (dialog.getMaxProgress() != percent) {
                dialog.setScaleDisplay().setProgress(percent)
            } else {
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