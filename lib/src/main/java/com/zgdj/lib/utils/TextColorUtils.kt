package com.zgdj.lib.utils

import android.content.res.ColorStateList

/**
 * 文字颜色
 *
 *
 * Created by jhj on 19-1-10.
 */
object TextColorUtils {

    fun pressed(pressedColor: Int, normalColor: Int): ColorStateList {
        val states = arrayOfNulls<IntArray>(2)
        states[0] = intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled)
        states[1] = intArrayOf()
        val colors = intArrayOf(pressedColor, normalColor)
        return ColorStateList(states, colors)
    }

    fun selected(selectedColor: Int, normalColor: Int): ColorStateList {
        val states = arrayOfNulls<IntArray>(2)
        states[0] = intArrayOf(android.R.attr.state_selected, android.R.attr.state_enabled)
        states[1] = intArrayOf()
        val colors = intArrayOf(selectedColor, normalColor)
        return ColorStateList(states, colors)
    }

}
