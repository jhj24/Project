package com.zgdj.lib.net.callback

import android.app.Activity
import com.jhj.httplibrary.HttpCall
import com.zgdj.lib.extention.loadingDialog
import org.jetbrains.anko.toast

abstract class BaseDialogHttpCallback<T>(activity: Activity, val dialogText: String = "正在加载中...") :
        BaseHttpCallback<T>(activity) {


    var dialog = mActivity.loadingDialog { HttpCall.cancelAll() }

    override fun onFailure(msg: String) {
        if (mIsOnFailureToast) mActivity.toast(msg)
        if (mIsOnFailureFinish) mActivity.finish()
    }

    override fun onFinish() {
        super.onFinish()
        if (dialog.isShow()) {
            dialog.dismiss()
        }
    }
}