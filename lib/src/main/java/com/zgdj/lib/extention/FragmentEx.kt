package com.zgdj.lib.extention

import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.view.View
import com.jhj.httplibrary.HttpCall
import com.jhj.httplibrary.model.HttpHeaders
import com.jhj.httplibrary.request.PostRequest
import com.zgdj.lib.base.fragment.BaseFragment
import com.zgdj.lib.config.UrlConfig
import com.zgdj.lib.utils.CommonNetUtils

fun BaseFragment.getResDrawable(@DrawableRes id: Int): Drawable {
    return ContextCompat.getDrawable(mActivity, id) ?: resources.getDrawable(id)
}

fun BaseFragment.closeKeyboard(view: View) {
    context?.closeKeyboard(view)
}

fun BaseFragment.openKeyboard(view: View) {
    context?.openKeyboard(view)
}

fun BaseFragment.httpPost(url: String): PostRequest {
    val login = context?.loginUserInfo ?: return HttpCall.post(UrlConfig.BASE_URL)
    val header = HttpHeaders()
    header.put("key", login.key.toString())
    header.put("token", login.token)
    return HttpCall.post(CommonNetUtils.baseUrl + url).addHeaders(header)
}

fun BaseFragment.pyHttpPost(url: String): PostRequest {
    val login = context?.loginUserInfo ?: return HttpCall.post(UrlConfig.BASE_URL)
    val header = HttpHeaders()
    header.put("key", login.key.toString())
    header.put("token", login.token)
    return HttpCall.post(UrlConfig.PYTHON_URL + url).addHeaders(header)
}