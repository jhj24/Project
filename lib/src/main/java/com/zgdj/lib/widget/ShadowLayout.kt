package com.zgdj.lib.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.FrameLayout
import com.zgdj.lib.R
import org.jetbrains.anko.dimen
import org.jetbrains.anko.dip


class ShadowLayout : FrameLayout {

    private var mShadowColor: Int = 0
    private var mShadowRadius: Float = 0.toFloat()
    private var mCornerRadius: Float = 0.toFloat()
    private var mDx: Float = 0.toFloat()
    private var mDy: Float = 0.toFloat()

    private var mInvalidateShadowOnSizeChanged = true
    private var mForceInvalidateShadow = false

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShadowLayout)
        mCornerRadius = typedArray.getDimension(R.styleable.ShadowLayout_sl_cornerRadius, dimen(R.dimen.default_corner_radius).toFloat())
        mShadowRadius = typedArray.getDimension(R.styleable.ShadowLayout_sl_shadowRadius, dimen(R.dimen.default_shadow_radius).toFloat())
        mDx = typedArray.getDimension(R.styleable.ShadowLayout_sl_dx, dimen(R.dimen.default_offset_x).toFloat())
        mDy = typedArray.getDimension(R.styleable.ShadowLayout_sl_dy, dimen(R.dimen.default_offset_y).toFloat())
        mShadowColor = typedArray.getColor(R.styleable.ShadowLayout_sl_shadowColor, ContextCompat.getColor(context, R.color.default_shadow_color))
        typedArray.recycle()
        val xPadding = (Math.abs(mDx)).toInt()
        val yPadding = (Math.abs(mDy)).toInt()
        setPadding(xPadding, yPadding, xPadding, yPadding)
    }


    override fun getSuggestedMinimumWidth(): Int {
        return 0
    }

    override fun getSuggestedMinimumHeight(): Int {
        return 0
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && h > 0 && (background == null || mInvalidateShadowOnSizeChanged || mForceInvalidateShadow)) {
            mForceInvalidateShadow = false
            setBackgroundCompat(w, h)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (mForceInvalidateShadow) {
            mForceInvalidateShadow = false
            setBackgroundCompat(right - left, bottom - top)
        }
    }

    fun setInvalidateShadowOnSizeChanged(invalidateShadowOnSizeChanged: Boolean) {
        mInvalidateShadowOnSizeChanged = invalidateShadowOnSizeChanged
    }

    fun invalidateShadow() {
        mForceInvalidateShadow = true
        requestLayout()
        invalidate()
    }


    private fun setBackgroundCompat(w: Int, h: Int) {
        val bitmap = createShadowBitmap(w, h, mCornerRadius, mShadowRadius, mDx, mDy, mShadowColor, Color.TRANSPARENT)
        val drawable = BitmapDrawable(resources, bitmap)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            setBackgroundDrawable(drawable)
        } else {
            background = drawable
        }
    }

    private fun createShadowBitmap(shadowWidth: Int, shadowHeight: Int, cornerRadius: Float, shadowRadius: Float,
                                   dx: Float, dy: Float, shadowColor: Int, fillColor: Int): Bitmap {

        val output = Bitmap.createBitmap(shadowWidth, shadowHeight, Bitmap.Config.ALPHA_8)
        val canvas = Canvas(output)

        val shadowRect = RectF(
                shadowRadius,
                shadowRadius,
                shadowWidth - shadowRadius,
                shadowHeight - shadowRadius)

        if (dy > 0) {
            shadowRect.top -= dip(3)
            shadowRect.bottom -= dy
        } else if (dy < 0) {
            shadowRect.top += Math.abs(dy)
            shadowRect.bottom -= Math.abs(dy)
        }

        if (dx > 0) {
            shadowRect.left -= dip(3)
            shadowRect.right -= dx
        } else if (dx < 0) {
            shadowRect.left += Math.abs(dx)
            shadowRect.right -= Math.abs(dx)
        }

        val shadowPaint = Paint()
        shadowPaint.isAntiAlias = true
        shadowPaint.color = fillColor
        shadowPaint.style = Paint.Style.FILL

        if (!isInEditMode) {
            shadowPaint.setShadowLayer(shadowRadius, dx, dy, shadowColor)
        }

        canvas.drawRoundRect(shadowRect, cornerRadius, cornerRadius, shadowPaint)

        return output
    }

}