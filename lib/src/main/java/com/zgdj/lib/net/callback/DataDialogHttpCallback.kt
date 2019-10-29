package com.zgdj.lib.net.callback

import android.app.Activity
import com.jhj.httplibrary.HttpCall
import com.zgdj.lib.extention.loadingDialog

abstract class DataDialogHttpCallback<T>(activity: Activity, val dialogText: String = "正在加载中...") :
    DataHttpCallback<T>(activity) {

    var dialog = mActivity.loadingDialog { HttpCall.cancelAll() }

    override fun onFinish() {
        super.onFinish()
        if (dialog.isShow()) {
            dialog.dismiss()
        }
    }
}