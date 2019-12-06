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
        }
    }

    abstract fun onSuccess(data: T?, resultType: ResultType)
}