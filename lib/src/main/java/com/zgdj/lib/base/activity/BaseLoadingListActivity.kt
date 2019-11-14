package com.zgdj.lib.base.activity

import android.os.Bundle
import android.view.View
import com.jhj.httplibrary.HttpCall
import com.jhj.httplibrary.model.HttpParams
import com.zgdj.lib.extention.toArrayList
import com.zgdj.lib.net.DataResult
import com.zgdj.lib.net.callback.DataDialogHttpCallback
import kotlinx.android.synthetic.main.activity_recyclerview_refresh.*
import kotlinx.android.synthetic.main.layout_empty_view.*
import kotlinx.android.synthetic.main.layout_search_bar.*
import java.lang.reflect.Type
import java.util.*

abstract class BaseLoadingListActivity<T> : BaseListActivity<T>() {

    abstract val url: String
    //请求参数
    open val httpParams = mutableListOf<Pair<String, String>>()
    //输入框变化就开始搜索
    open val inputSearchFunc: (T, String) -> Boolean = { _, _ -> true }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        httpRequest()
        tv_refresh.setOnClickListener {
            httpRequest()
        }
    }

    override fun refresh() {
        httpRequest()
    }

    override fun inputSearch(search: String?) {
        val list = dataList.filter { inputSearchFunc(it, et_search_input.text.toString()) }
        dataListEmpty(list)
    }


    private fun httpRequest() {
        val params = HttpParams()
        httpParams.forEach {
            params.put(it.first, it.second)
        }
        HttpCall.post(url)
                .addParams(params)
                .addParams(filterParams)
                .enqueue(object : DataDialogHttpCallback<List<T>>(this) {

                    var isSuccess = false

                    override val clazzType: Type
                        get() = type(DataResult::class.java, type(List::class.java, genericType()))

                    override fun onSuccess(data: List<T>?, resultType: ResultType) {
                        isSuccess = true
                        dataList = data.orEmpty()
                    }

                    override fun onFailure(msg: String) {
                        super.onFailure(msg)
                        isSuccess = false
                    }

                    override fun onFinish() {
                        super.onFinish()
                        if (isSuccess) {
                            inputSearch(et_search_input.text.toString())
                        } else {
                            include_empty_view.visibility = View.VISIBLE
                            tv_refresh_reason.text = "查询失败"
                        }
                    }
                })
    }
}