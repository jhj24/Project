package com.zgdj.project.weight

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.zgdj.lib.extention.scaleDensity
import org.jetbrains.anko.dip

class WaterLevelView : View {

    val paint = Paint()
    val xAxisPath = Path()
    val defaultBlank = dip(2)
    val rectF = RectF()
    val rectWidth = dip(12)


    val yBidText = "0.50m"
    val yTextRect = Rect()

    val xBidText = "进水池液位"
    val xTextRext = Rect()


    val lightPaint: Paint
        get() {
            paint.reset()
            paint.style = Paint.Style.STROKE
            paint.isAntiAlias = true
            paint.color = 0x26000000.toInt()
            paint.textSize = 12 * scaleDensity
            return paint
        }

    val xTextPaint: Paint
        get() {
            paint.reset()
            paint.isAntiAlias = true
            paint.color = 0xaf000000.toInt()
            paint.textSize = 12 * scaleDensity
            return paint
        }

    val xAxisPaint: Paint
        get() {
            paint.reset()
            paint.style = Paint.Style.STROKE
            paint.color = 0xff3aa7ff.toInt()
            paint.strokeWidth = 2f
            paint.isAntiAlias = true
            paint.pathEffect = DashPathEffect(floatArrayOf(8f, 4f), 0f)
            return paint
        }


    val dataList = listOf(
            WaterLevelBean("进水池", 0.64),
            WaterLevelBean("进口闸前", 0.65),
            WaterLevelBean("进口闸后", 0.63),
            WaterLevelBean("出水池", 0.64)
    )

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {


    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        lightPaint.getTextBounds(yBidText, 0, yBidText.length, yTextRect)

        xTextPaint.getTextBounds(xBidText, 0, xBidText.length, xTextRext)

        val xAxisStart = paddingLeft + yTextRect.width() + defaultBlank.toFloat()
        val xAxisEnd = width - paddingRight.toFloat()
        val yAxisStart = paddingTop + xTextRext.height() + defaultBlank.toFloat()
        val yAxisEnd = height - paddingBottom - xTextRext.height() - defaultBlank.toFloat()

        val axisWidth = xAxisEnd - xAxisStart
        val axisHeight = yAxisEnd - yAxisStart


        //0.50 线
        val yMiddle = "0.50m"
        canvas?.drawText(yMiddle, paddingLeft.toFloat(), yAxisStart + axisHeight / 2 + yTextRect.height() / 2, lightPaint)
        canvas?.drawLine(xAxisStart, yAxisStart + axisHeight / 2, xAxisEnd, yAxisStart + axisHeight / 2, lightPaint)

        //0.00 线
        val yOrigin = "0.00m"
        canvas?.drawText(yOrigin, paddingLeft.toFloat(), yAxisEnd + yTextRect.height() / 2, lightPaint)
        xAxisPath.moveTo(xAxisStart, yAxisEnd)
        xAxisPath.lineTo(xAxisEnd, yAxisEnd)
        canvas?.drawPath(xAxisPath, xAxisPaint)


        val bidWidth = axisWidth / 4
        //横坐标标段
        dataList.forEachIndexed { index, bean ->
            val center = xAxisStart + (index * bidWidth) + bidWidth / 2

            // 样本
            val textWidth = xTextPaint.measureText(bean.xBid)
            val start = center - textWidth / 2
            canvas?.drawText(bean.xBid, start, height - paddingBottom.toFloat(), xTextPaint)

            // value
            val valueTextWidth = lightPaint.measureText(bean.value.toString())
            val valueStart = center - valueTextWidth / 2
            canvas?.drawText(bean.value.toString(), valueStart, paddingTop + yTextRect.height() + defaultBlank.toFloat(), lightPaint)

            //样本到value的竖线
            canvas?.drawLine(center, yAxisStart, center, yAxisEnd, lightPaint)

            //圆角矩形
            paint.reset()
            paint.shader = LinearGradient(center, axisHeight * (1 - bean.value).toFloat(), center, yAxisEnd, 0xff9025fc.toInt(), 0xff1308fe.toInt(), Shader.TileMode.CLAMP)
            rectF.set(center - rectWidth / 2, axisHeight * (1 - bean.value).toFloat(), center + rectWidth / 2, yAxisEnd)
            val radio = dip(2).toFloat()
            canvas?.drawRoundRect(rectF, radio, radio, paint)
        }
    }


    data class WaterLevelBean(
            val xBid: String,
            val value: Double
    )
}