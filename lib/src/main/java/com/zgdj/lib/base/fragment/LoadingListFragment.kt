package com.zgdj.lib.base.fragment

import android.os.Bundle
import android.view.View
import com.jhj.httplibrary.HttpCall
import com.jhj.httplibrary.model.HttpParams
import com.zgdj.lib.base.activity.BaseListFragment
import com.zgdj.lib.net.DataResult
import com.zgdj.lib.net.callback.DataDialogHttpCallback
import kotlinx.android.synthetic.main.fragment_recclerview_refresh.view.*
import kotlinx.android.synthetic.main.layout_empty_view.view.*
import kotlinx.android.synthetic.main.layout_search_bar.view.*
import java.lang.reflect.Type

abstract class LoadingListFragment<T> : BaseListFragment<T>() {

    abstract val url: String
    //请求参数
    open val httpParams = arrayListOf<Pair<String, String>>()
    //输入框变化就开始搜索
    open val inputSearchFunc: (T, String) -> Boolean = { _, _ -> true }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        httpRequest()
        view.tv_refresh.setOnClickListener {
            httpRequest()
        }
    }

    override fun refresh() {
        httpRequest()
    }

    override fun inputSearch(search: String?) {
        val list = dataList.filter { inputSearchFunc(it, mParentView.et_search_input.text.toString()) }
        dataListEmpty(list)
    }

    private fun httpRequest() {
        val params = HttpParams()
        httpParams.forEach {
            params.put(it.first, it.second)
        }
        HttpCall.post(url)
                .addParams(params)
                .enqueue(object : DataDialogHttpCallback<List<T>>(mActivity) {

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
                            inputSearch(mParentView.et_search_input.text.toString())
                        } else {
                            mParentView.include_empty_view.visibility = View.VISIBLE
                            mParentView.tv_refresh_reason.text = "查询失败"
                        }
                    }
                })
    }
}