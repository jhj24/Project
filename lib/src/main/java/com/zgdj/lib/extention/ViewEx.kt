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
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.zgdj.lib.utils.BackGroundUtils
import com.zgdj.lib.utils.SystemEnv
import org.jetbrains.anko.toast

val View.density: Float
    get() = context.density

val View.scaleDensity: Float
    get() = context.scaleDensity

fun View.color(@ColorRes color: Int) = context.getResColor(color)

fun View.circleBackground(color: Int) {
    this.background = BackGroundUtils.roundBackground(color)
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


fun View.drawable(@DrawableRes drawableRes: Int): Drawable =
    ContextCompat.getDrawable(context, drawableRes) ?: resources.getDrawable(drawableRes)


fun ImageView.glide(any: Any) = Glide.with(context).asBitmap().load(any).into(this)

fun ImageView.blankColorFilter(color: Int = 0xff333333.toInt()) = this.setColorFilter(color, PorterDuff.Mode.SRC_IN)

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


/*fun View.authorityOnLongClick(string: String, allowedBody: () -> Unit = {}, forbiddenBody: () -> Unit = {}) {
    setOnLongClickListener {
        val authorityList = SystemEnv.getUserAuthority(context)
        val authority = authorityList?.find { it.path == string }
        if (authority?.authority == 0) {
            forbiddenBody()
            context.toast("没有权限")
            return@setOnLongClickListener true
        } else {
            allowedBody()
            return@setOnLongClickListener false
        }
    }
}*/


/*fun View.authorityOnTouch(string: String, allowedBody: () -> Unit = {}, forbiddenBody: () -> Unit = {}) {
    setOnTouchListener { v, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
            val authorityList = SystemEnv.getUserAuthority(context)
            val authority = authorityList?.find { it.path == string }
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
}*/


fun ImageView.roundImage(context: Context, imgPath: Any) {
    Glide.with(context)
        .asBitmap()
        .load(imgPath)
        .into(this)
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




