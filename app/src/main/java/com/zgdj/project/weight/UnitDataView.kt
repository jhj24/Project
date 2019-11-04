package com.zgdj.project.weight

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.zgdj.lib.extention.scaleDensity
import org.jetbrains.anko.dip

class UnitDataView : View {
    val paint = Paint()
    val defaultBlank = dip(2)
    val radio = dip(2).toFloat()
    val rectWidth = dip(12).toFloat()

    val yBidText = "1500"
    val yTextRect = Rect()

    val xBidText = "进水池液位"
    val xTextRext = Rect()

    val yCenterPath = Path()//中间蓝色线
    val yOtherPath = Path()//其他线

    val headTextRect = Rect()
    val headTextRect1 = RectF()
    val headTextRect2 = RectF()


    val linePaint: Paint
        get() {
            paint.reset()
            paint.style = Paint.Style.STROKE
            paint.isAntiAlias = true
            paint.strokeWidth = 1f
            paint.color = 0xffdfdfdf.toInt()
            return paint
        }
    val lightPaint: Paint
        get() {
            paint.reset()
            paint.isAntiAlias = true
            paint.color = 0x36000000.toInt()
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
            UnitBean("1#机组", 33, 1600),
            UnitBean("2#机组", 32, 1584),
            UnitBean("3#机组", 31, 1512),
            UnitBean("4#机组", 32, 1620),
            UnitBean("5#机组", 33, 1689)
    )
    val yValue = listOf("30", "20", "10", "0", "500", "1000", "1500")


    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val text = "运行次数"
        lightPaint.getTextBounds(text, 0, text.length, headTextRect)


    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        lightPaint.getTextBounds(yBidText, 0, yBidText.length, yTextRect)

        xTextPaint.getTextBounds(xBidText, 0, xBidText.length, xTextRext)

        val xAxisStart = paddingLeft + yTextRect.width() + defaultBlank.toFloat()
        val xAxisEnd = width - paddingRight.toFloat()
        val yAxisStart = paddingTop + xTextRext.height() * 2 + defaultBlank.toFloat()
        val yAxisEnd = height - paddingBottom - xTextRext.height() * 2 - defaultBlank.toFloat()

        val axisWidth = xAxisEnd - xAxisStart
        val axisHeight = yAxisEnd - yAxisStart


        //=====头=====

        val rectSize = headTextRect.height() - dip(2)
        val yEnd = (paddingTop + headTextRect.height()).toFloat()

        //运行次数
        val xStart1 = (width / 2 - (headTextRect.width() + rectSize + dip(10) * 2)).toFloat()
        headTextRect1.set(xStart1, yEnd - rectSize, xStart1 + rectSize, yEnd)
        paint.reset()
        paint.isAntiAlias = true
        paint.color = 0xff5418fd.toInt()
        canvas?.drawRoundRect(headTextRect1, radio, radio, paint)
        canvas?.drawText("运行次数", xStart1 + rectSize + dip(10), yEnd, lightPaint)

        //排水时长
        val xStart2 = ((width / 2) + dip(10)).toFloat()
        headTextRect2.set(xStart2, yEnd - rectSize, xStart2 + rectSize, yEnd)
        paint.reset()
        paint.isAntiAlias = true
        paint.color = 0xff7ED321.toInt()
        canvas?.drawRoundRect(headTextRect2, radio, radio, paint)
        canvas?.drawText("抽排时长", xStart2 + rectSize + dip(10), yEnd, lightPaint)

        //=====背向图=====
        //线
        val yCenter = yAxisStart + axisHeight / 2
        yCenterPath.moveTo(xAxisStart, yCenter)
        yCenterPath.lineTo(xAxisEnd, yCenter)
        canvas?.drawPath(yCenterPath, xAxisPaint)

        val averageHeight = axisHeight / 8
        yOtherPath.moveTo(xAxisStart, yCenter + averageHeight)
        yOtherPath.lineTo(xAxisEnd, yCenter + averageHeight)
        yOtherPath.moveTo(xAxisStart, yCenter + averageHeight * 2)
        yOtherPath.lineTo(xAxisEnd, yCenter + averageHeight * 2)
        yOtherPath.moveTo(xAxisStart, yCenter + averageHeight * 3)
        yOtherPath.lineTo(xAxisEnd, yCenter + averageHeight * 3)
        yOtherPath.moveTo(xAxisStart, yCenter - averageHeight)
        yOtherPath.lineTo(xAxisEnd, yCenter - averageHeight)
        yOtherPath.moveTo(xAxisStart, yCenter - averageHeight * 2)
        yOtherPath.lineTo(xAxisEnd, yCenter - averageHeight * 2)
        yOtherPath.moveTo(xAxisStart, yCenter - averageHeight * 3)
        yOtherPath.lineTo(xAxisEnd, yCenter - averageHeight * 3)
        canvas?.drawPath(yOtherPath, linePaint)


        canvas?.drawText("30", paddingLeft + yTextRect.width() / 2 - lightPaint.measureText("30") / 2, yCenter - averageHeight * 3 + yTextRect.height() / 2, lightPaint)
        canvas?.drawText("20", paddingLeft + yTextRect.width() / 2 - lightPaint.measureText("20") / 2, yCenter - averageHeight * 2 + yTextRect.height() / 2, lightPaint)
        canvas?.drawText("10", paddingLeft + yTextRect.width() / 2 - lightPaint.measureText("10") / 2, yCenter - averageHeight * 1 + yTextRect.height() / 2, lightPaint)
        canvas?.drawText("0", paddingLeft + yTextRect.width() / 2 - lightPaint.measureText("0") / 2, yCenter - averageHeight * 0 + yTextRect.height() / 2, lightPaint)
        canvas?.drawText("500", paddingLeft + yTextRect.width() / 2 - lightPaint.measureText("500") / 2, yCenter + averageHeight * 1 + yTextRect.height() / 2, lightPaint)
        canvas?.drawText("1000", paddingLeft + yTextRect.width() / 2 - lightPaint.measureText("1000") / 2, yCenter + averageHeight * 2 + yTextRect.height() / 2, lightPaint)
        canvas?.drawText("1500", paddingLeft + yTextRect.width() / 2 - lightPaint.measureText("1500") / 2, yCenter + averageHeight * 3 + yTextRect.height() / 2, lightPaint)

        dataList.forEachIndexed { index, unitBean ->
            val xCenter = xAxisStart + index * (axisWidth / dataList.size) + (axisWidth / dataList.size) / 2
            //样本
            val width = xTextPaint.measureText(unitBean.sampla)
            canvas?.drawText(unitBean.sampla, xCenter - width / 2, height - paddingBottom.toFloat(), xTextPaint)

            //value
            val startY1 = yCenter - (axisHeight / 2) * unitBean.times / 40
            paint.reset()
            paint.shader = LinearGradient(xCenter, startY1, xCenter, yCenter, 0xff9025fc.toInt(), 0xff1308fe.toInt(), Shader.TileMode.CLAMP)
            canvas?.drawRect(xCenter - rectWidth / 2, startY1, xCenter + rectWidth / 2, yCenter, paint)
            canvas?.drawText("${unitBean.times}次", xCenter - lightPaint.measureText("${unitBean.times}次") / 2, startY1 - dip(1), lightPaint)


            val endY2 = yCenter + (axisHeight / 2) * unitBean.duration / 2000
            paint.reset()
            paint.shader = LinearGradient(xCenter, yCenter, xCenter, endY2, 0xff6cdb18.toInt(), 0xff5bd700.toInt(), Shader.TileMode.CLAMP)
            canvas?.drawRect(xCenter - rectWidth / 2, yCenter, xCenter + rectWidth / 2, endY2, paint)
            canvas?.drawText("${unitBean.duration}min", xCenter - lightPaint.measureText("${unitBean.duration}min") / 2, endY2 + yTextRect.height() + dip(1), lightPaint)

            //canvas?.drawText()

        }
    }


    data class UnitBean(
            val sampla: String,
            val times: Int,
            val duration: Int
    )

}