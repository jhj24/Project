package com.zgdj.lib.net.callback

import android.app.Activity
import com.zgdj.lib.net.DataResult
import org.jetbrains.anko.toast
import java.lang.reflect.Type

abstract class DataHttpCallback<T>(activity: Activity) : BaseHttpCallback<DataResult<T>>(activity) {

    override val clazzType: Type
        get() = type(DataResult::class.java, getTClazz())

    override fun onResult(data: DataResult<T>?, resultType: ResultType) {
        if (data?.code == 1) {
            onSuccess(data.data, resultType)
            if (mIsOnSuccessToast && data.msg != null) mActivity.toast(data.msg)
        } else {
            if (mIsOnFailureToast && data?.msg != null) mActivity.toast(data.msg)
            if (mIsOnFailureFinish) mActivity.finish()
        }
    }

    override fun onFailure(msg: String) {
        if (mIsOnFailureToast) mActivity.toast(msg)
        if (mIsOnFailureFinish) mActivity.finish()
    }

    abstract fun onSuccess(data: T?, resultType: ResultType)
}