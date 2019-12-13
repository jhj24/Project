package com.zgdj.lib.extention

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.jhj.slimadapter.holder.ViewInjector
import com.zgdj.lib.R
import com.zgdj.lib.utils.BackGroundUtils
import org.jetbrains.anko.dip
import org.jetbrains.anko.toast


fun View.color(@ColorRes color: Int) = context.getResColor(color)

fun View.colorRound(color: Int) {
    this.background = BackGroundUtils.roundBackground(color)
}

fun View.drawable(@DrawableRes drawableRes: Int): Drawable =
    context.getResDrawable(drawableRes)

fun ImageView.colorFilter(color: Int) =
    this.setColorFilter(color, PorterDuff.Mode.SRC_IN)

//===========ImageView===========
fun ImageView.glide(imgPath: Any, options: RequestOptions) {
    Glide.with(context).asBitmap().load(imgPath).apply(options).into(this)
}

fun ImageView.glide(path: Any) {
    glide(path, RequestOptions().placeholder(R.mipmap.ic_image_selector_placeholder))
}

fun ImageView.glideRound(path: Any, radius: Int = dip(6)) {
    glide(path, RequestOptions().placeholder(R.mipmap.ic_image_selector_placeholder).transform(RoundedCorners(radius)))
}

fun ImageView.glideCircle(path: Any) {
    glide(path, RequestOptions().placeholder(R.mipmap.ic_image_selector_placeholder).transform(CircleCrop()))
}

fun ImageView.glideAvatar(path: Any) {
    glide(path, RequestOptions().placeholder(R.mipmap.ic_avatar).transform(CircleCrop()))
}

fun ImageView.glideNoCache(path: Any) {
    glide(
        path,
        RequestOptions().placeholder(R.mipmap.ic_image_selector_placeholder).diskCacheStrategy(DiskCacheStrategy.NONE)
    )
}

fun View.openKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager;
    imm.showSoftInput(this, InputMethodManager.RESULT_SHOWN)
    imm.toggleSoftInput(
        InputMethodManager.SHOW_FORCED,
        InputMethodManager.HIDE_IMPLICIT_ONLY
    );
}

fun View.closeKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager;
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.singleSelected(vararg views: View) {
    this.isSelected = true
    views.forEach {
        it.isSelected = false
    }
}

fun View.clicked(isClicked: Boolean) {
    isFocusable = isClicked
    isClickable = isClicked
    if (this is EditText && isClicked) {
        isFocusableInTouchMode = true
    }
}


fun EditText.inputLimit(maxSize: Int = 200, msg: String) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            var s = editable.toString()
            if (s.length > maxSize) {
                s = s.substring(0, s.length - 1)
                editable?.replace(0, editable.length, s.trim({ it <= ' ' }))
                context.toast("${msg}最多输入${maxSize}个字符")
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    })
}

fun View.authorityOnTouch(string: String, allowedBody: () -> Unit = {}, forbiddenBody: () -> Unit = {}) {
    setOnTouchListener { v, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
            val authority = context?.userAuthority?.find { it.path == string }
            if (authority?.authority == 0) {
                forbiddenBody()
                context.toast("没有权限")
                return@setOnTouchListener true
            } else {
                allowedBody()
                return@setOnTouchListener false
            }
        }
        return@setOnTouchListener false
    }
}


/**
 * 解决RecyclerView嵌套RecyclerView滑动卡顿的问题
 */
fun RecyclerView.preventStuck() {
    layoutManager = object : LinearLayoutManager(context) {
        //如果你的RecyclerView是水平滑动的话可以重写canScrollHorizontally方法
        override fun canScrollVertically() = false
    }
    //解决数据加载不完的问题
    isNestedScrollingEnabled = false
    setHasFixedSize(true)
    //解决数据加载完成后, 没有停留在顶部的问题
    isFocusable = false
}

fun ViewInjector.glide(id: Int, path: Any): ViewInjector {
    val imageView = getView<ImageView>(id)
    imageView.glide(path)
    return this
}

fun ViewInjector.imageAvatar(id: Int, path: Any): ViewInjector {
    val imageView = getView<ImageView>(id)
    imageView.glideAvatar(path)
    return this
}




