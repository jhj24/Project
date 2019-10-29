package com.zgdj.lib.utils

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.StateListDrawable

/**
 * View 背景色以及点击、选择背景样式
 *
 *
 * Created by jhj on 19-1-9.
 */
object BackGroundUtils {

    /**
     * 设置View点击、未点击时的背景色
     *
     * @param pressedColor 点击时的背景色
     * @param normalColor  未点击时的背景色
     * @return StateListDrawable
     */
    fun pressed(pressedColor: Int, normalColor: Int): StateListDrawable {
        return pressed(
                background(pressedColor, 0f),
                background(normalColor, 0f)
        )
    }

    /**
     * 设置View点击、未点击时的背景色，可设置四角圆角半径
     *
     * @param pressedColor 点击时的背景色
     * @param normalColor  未点击时的背景色
     * @param radio        四角圆角半径
     * @return StateListDrawable
     */
    fun pressed(pressedColor: Int, normalColor: Int, radio: Float): StateListDrawable {
        return pressed(
                background(pressedColor, radio),
                background(normalColor, radio)
        )
    }

    /**
     * 设置View点击、未点击时的背景色，可设置四角圆角半径、边框以及边框颜色
     *
     * @param pressedColor 点击时的背景色
     * @param normalColor  未点击时的背景色
     * @param radio        四角圆角半径
     * @param frameWidth   边框的宽度
     * @param frameColor   边框的颜色
     * @return StateListDrawable
     */
    fun pressed(pressedColor: Int, normalColor: Int, radio: Float, frameWidth: Int, frameColor: Int): StateListDrawable {
        return pressed(
                background(pressedColor, radio, frameWidth, frameColor),
                background(normalColor, radio, frameWidth, frameColor)
        )
    }

    /**
     * 设置View点击、未点击时的背景色，可设置四角圆角半径（角度可不同）、边框、边框颜色，以及某一边的边框是否显示
     *
     * @param pressedColor      点击时的背景色
     * @param normalColor       未点击时的背景色
     * @param topLeftRadio      左上角圆角半径
     * @param topRightRadio     右上角圆角半径
     * @param bottomRightRadio  右下角圆角半径
     * @param bottomLeftRadio   左下角圆角半径
     * @param frameWidth        边框的宽度
     * @param frameColor        边距的颜色
     * @param isShowLeftFrame   是否显示左边框
     * @param isShowTopFrame    是否显示上边框
     * @param isShowRightFrame  是否显示右边框
     * @param isShowBottomFrame 是否显示下边框
     * @return StateListDrawable
     */
    fun pressed(pressedColor: Int, normalColor: Int, topLeftRadio: Float, topRightRadio: Float, bottomRightRadio: Float, bottomLeftRadio: Float,
                frameWidth: Int, frameColor: Int, isShowLeftFrame: Boolean, isShowTopFrame: Boolean, isShowRightFrame: Boolean, isShowBottomFrame: Boolean): StateListDrawable {
        return pressed(
                background(pressedColor, topLeftRadio, topRightRadio, bottomRightRadio, bottomLeftRadio,
                        frameWidth, frameColor, isShowLeftFrame, isShowTopFrame, isShowRightFrame, isShowBottomFrame),
                background(normalColor, topLeftRadio, topRightRadio, bottomRightRadio, bottomLeftRadio,
                        frameWidth, frameColor, isShowLeftFrame, isShowTopFrame, isShowRightFrame, isShowBottomFrame)
        )
    }


    /**
     * 设置 View 点击、未点击时的 Drawable
     *
     * @param press       点击时的 Drawable
     * @param normalColor 未点击时的 Drawable
     * @return StateListDrawable
     */
    fun pressed(press: Drawable, normalColor: Drawable): StateListDrawable {
        val drawable = StateListDrawable()
        drawable.addState(intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled), press)
        drawable.addState(intArrayOf(), normalColor)
        return drawable
    }

    /**
     * 设置View选中、未选中时的背景色
     *
     * @param selectedColor 选中时的背景色
     * @param normalColor   未选中时的背景色
     * @return StateListDrawable
     */
    fun selected(selectedColor: Int, normalColor: Int): StateListDrawable {
        return selected(
                background(selectedColor, 0f),
                background(normalColor, 0f)
        )
    }

    /**
     * 设置View选中、未选中时的背景色，可设置四角圆角半径
     *
     * @param selectedColor 选中时的背景色
     * @param normalColor   未选中时的背景色
     * @param radio         四角圆角半径
     * @return StateListDrawable
     */
    fun selected(selectedColor: Int, normalColor: Int, radio: Float): StateListDrawable {
        return selected(
                background(selectedColor, radio),
                background(normalColor, radio)
        )
    }

    /**
     * 设置View选中、未选中时的背景色，可设置四角圆角半径、边框以及边框颜色
     *
     * @param selectedColor 选中时的背景色
     * @param normalColor   未选中时的背景色
     * @param radio         四角圆角半径
     * @param frameWidth    边框的宽度
     * @param frameColor    边框的颜色
     * @return StateListDrawable
     */
    fun selected(selectedColor: Int, normalColor: Int, radio: Float, frameWidth: Int, frameColor: Int): StateListDrawable {
        return selected(
                background(selectedColor, radio, frameWidth, frameColor),
                background(normalColor, radio, frameWidth, frameColor)
        )
    }

    /**
     * 设置View选中、未选中时的背景色，可设置四角圆角半径（角度可不同）、边框、边框颜色，以及某一边的边框是否显示
     *
     * @param selectedColor     选中时的背景色
     * @param normalColor       未选中时的背景色
     * @param topLeftRadio      左上角圆角半径
     * @param topRightRadio     右上角圆角半径
     * @param bottomRightRadio  右下角圆角半径
     * @param bottomLeftRadio   左下角圆角半径
     * @param frameWidth        边框的宽度
     * @param frameColor        边距的颜色
     * @param isShowLeftFrame   是否显示左边框
     * @param isShowTopFrame    是否显示上边框
     * @param isShowRightFrame  是否显示右边框
     * @param isShowBottomFrame 是否显示下边框
     * @return StateListDrawable
     */
    fun selected(selectedColor: Int, normalColor: Int, topLeftRadio: Float, topRightRadio: Float, bottomRightRadio: Float, bottomLeftRadio: Float,
                 frameWidth: Int, frameColor: Int, isShowLeftFrame: Boolean, isShowTopFrame: Boolean, isShowRightFrame: Boolean, isShowBottomFrame: Boolean): StateListDrawable {
        return selected(
                background(selectedColor, topLeftRadio, topRightRadio, bottomRightRadio, bottomLeftRadio,
                        frameWidth, frameColor, isShowLeftFrame, isShowTopFrame, isShowRightFrame, isShowBottomFrame),
                background(normalColor, topLeftRadio, topRightRadio, bottomRightRadio, bottomLeftRadio,
                        frameWidth, frameColor, isShowLeftFrame, isShowTopFrame, isShowRightFrame, isShowBottomFrame)
        )
    }

    /**
     * 设置View选中、未选中时的背景色
     *
     * @param selectedDrawable 选中时的 Drawable
     * @param normalDrawable   未选中时的 Drawable
     * @return StateListDrawable
     */
    fun selected(selectedDrawable: Drawable, normalDrawable: Drawable): StateListDrawable {
        val drawable = StateListDrawable()
        drawable.addState(intArrayOf(android.R.attr.state_selected, android.R.attr.state_enabled), selectedDrawable)
        drawable.addState(intArrayOf(), normalDrawable)
        return drawable
    }

    fun roundBackground(color: Int): Drawable {
        val backgroundDrawable = GradientDrawable()
        backgroundDrawable.shape = GradientDrawable.OVAL
        backgroundDrawable.setColor(color)
        return backgroundDrawable
    }

    /**
     * View 背景色，可设置四角圆角半径、边框以及边框颜色
     *
     * @param color      背景色
     * @param radio      四角圆角半径
     * @param frameWidth 边框的宽度
     * @param frameColor 边框的颜色
     * @return Drawable
     */
    @JvmOverloads
    fun background(color: Int, radio: Float, frameWidth: Int = 0, frameColor: Int = 0): Drawable {
        return background(color, radio, radio, radio, radio, frameWidth, frameColor, true, true, true, true)
    }

    /**
     * View 背景色，可设置四角圆角半径（角度可不同）、边框、边框颜色，以及某一边的边框是否显示
     *
     * @param color             背景色
     * @param topLeftRadio      左上角圆角半径
     * @param topRightRadio     右上角圆角半径
     * @param bottomRightRadio  右下角圆角半径
     * @param bottomLeftRadio   左下角圆角半径
     * @param frameWidth        边框的宽度
     * @param frameColor        边距的颜色
     * @param isShowLeftFrame   是否显示左边框
     * @param isShowTopFrame    是否显示上边框
     * @param isShowRightFrame  是否显示右边框
     * @param isShowBottomFrame 是否显示下边框
     * @return Drawable
     */
    fun background(color: Int, topLeftRadio: Float, topRightRadio: Float, bottomRightRadio: Float, bottomLeftRadio: Float, frameWidth: Int,
                   frameColor: Int, isShowLeftFrame: Boolean, isShowTopFrame: Boolean, isShowRightFrame: Boolean, isShowBottomFrame: Boolean): Drawable {

        val radio = floatArrayOf(topLeftRadio, topLeftRadio, topRightRadio, topRightRadio, bottomRightRadio, bottomRightRadio, bottomLeftRadio, bottomLeftRadio)

        //边框
        val frameDrawable = GradientDrawable()
        frameDrawable.shape = GradientDrawable.RECTANGLE
        frameDrawable.cornerRadii = radio
        frameDrawable.setColor(frameColor)

        //背景
        val backgroundDrawable = GradientDrawable()
        backgroundDrawable.shape = GradientDrawable.RECTANGLE
        backgroundDrawable.cornerRadii = radio
        backgroundDrawable.setColor(color)

        val layerDrawable = LayerDrawable(arrayOf<Drawable>(frameDrawable, backgroundDrawable))
        layerDrawable.setLayerInset(1,
                if (isShowLeftFrame) frameWidth else 0,
                if (isShowTopFrame) frameWidth else 0,
                if (isShowRightFrame) frameWidth else 0,
                if (isShowBottomFrame) frameWidth else 0)

        return layerDrawable
    }
}

