package com.zgdj.lib.extention

import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.view.View
import com.zgdj.lib.base.fragment.BaseFragment

fun BaseFragment.getResDrawable(@DrawableRes id: Int): Drawable {
    return ContextCompat.getDrawable(mActivity, id) ?: resources.getDrawable(id)
}

fun BaseFragment.closeKeyboard(view: View) {
    context?.closeKeyboard(view)
}

fun BaseFragment.openKeyboard(view: View) {
    context?.openKeyboard(view)
}