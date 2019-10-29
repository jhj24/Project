package com.zgdj.lib.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.zgdj.lib.R
import com.zgdj.lib.extention.color
import com.zgdj.lib.extention.getResColor
import com.zgdj.lib.utils.BackGroundUtils
import com.zgdj.lib.utils.Logger
import kotlinx.android.synthetic.main.layout_item_label_input.view.*
import org.jetbrains.anko.*

class LabelInputLayout : LinearLayout {

    companion object {
        const val DEFAULT_LABEL_GRAVITY = 0
    }

    private var style: Int
    private var mLabelText: String? = null
    private var mLabelTextColor: Int
    private var mLabelTextSize: Float
    private var mLabelTextGravity: Int
    private var mIsLabelTextColon: Boolean //是否需要label后的冒号
    private var mIsCanInput = false
    private var mIsShowInputText = false
    private var view: View


    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        view = LayoutInflater.from(context).inflate(R.layout.layout_item_label_input, this, false)
        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.LabelInputLayout)
        style = typedArray?.getInt(R.styleable.LabelInputLayout_style, 0) ?: 0//0-small,1-big

        // labelText
        mLabelText = typedArray?.getString(R.styleable.LabelInputLayout_labelText)
        mLabelTextColor = typedArray?.getColor(R.styleable.LabelInputLayout_labelTextColor, if (style == 0) color(R.color.text_normal) else color(R.color.text_title))
                ?: if (style == 0) color(R.color.text_normal) else color(R.color.text_title)
        mLabelTextSize = typedArray?.getFloat(R.styleable.LabelInputLayout_labelTextSize, if (style == 0) 14f else 16f)
                ?: if (style == 0) 14f else 16f
        mLabelTextGravity = typedArray?.getInt(R.styleable.LabelInputLayout_labelTextGravity, 0)
                ?: 0
        mIsLabelTextColon = typedArray?.getBoolean(R.styleable.LabelInputLayout_isShowLabelColon, true)
                ?: true

        // inputText
        val mInputText = typedArray?.getString(R.styleable.LabelInputLayout_inputText)
        val mInputTextColor = typedArray?.getColor(R.styleable.LabelInputLayout_inputTextColor, if (style == 0) color(R.color.text_normal) else color(R.color.text_message))
                ?: if (style == 0) color(R.color.text_normal) else color(R.color.text_message)
        val mInputTextSize = typedArray?.getDimension(R.styleable.LabelInputLayout_inputTextSize, if (style == 0) 14f else 16f)
                ?: if (style == 0) 14f else 16f

        // unitText
        val unitText = typedArray?.getString(R.styleable.LabelInputLayout_unitText)
        val mUnitTextColor = typedArray?.getColor(R.styleable.LabelInputLayout_unitTextColor, if (style == 0) color(R.color.text_normal) else color(R.color.text_message))
                ?: if (style == 0) color(R.color.text_normal) else color(R.color.text_message)
        val mUnitTextSize = typedArray?.getDimension(R.styleable.LabelInputLayout_unitTextSize, if (style == 0) 14f else 16f)
                ?: if (style == 0) 14f else 16f

        val hintText = typedArray?.getString(R.styleable.LabelInputLayout_hintText)
        val isShowBottomLine = typedArray?.getBoolean(R.styleable.LabelInputLayout_isShowBottomLine, true)
                ?: true
        val mIsShowArrow = typedArray?.getBoolean(R.styleable.LabelInputLayout_isArrowShow, true)
                ?: true
        mIsShowInputText = typedArray?.getBoolean(R.styleable.LabelInputLayout_isShowInputText, true)
                ?: true
        mIsCanInput = typedArray?.getBoolean(R.styleable.LabelInputLayout_isCanInput, false)
                ?: false



        view.tv_colon.visibility = if (mIsLabelTextColon) View.VISIBLE else View.GONE

        view.tv_item_label.text = mLabelText
        view.tv_item_label.textColor = mLabelTextColor
        view.tv_item_label.textSize = mLabelTextSize
        if (mLabelTextGravity == 0) {
            view.tv_item_label.gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL
        } else {
            val layoutParams = LayoutParams(wrapContent, wrapContent)
            layoutParams.weight = 0f
            view.tv_item_label.gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
            view.tv_item_label.leftPadding = dip(20)
            view.tv_item_label.layoutParams = layoutParams
        }
        if (mIsShowInputText) {
            if (mIsCanInput) {//可输入时，使用EditText（不知为何，EditText的父控件获取不到点击事件）
                view.et_item_input.visibility = View.VISIBLE
                view.tv_item_input.visibility = View.GONE
                view.et_item_input.hint = hintText
                view.et_item_input.setText(mInputText)
                view.et_item_input.textColor = mInputTextColor
                view.et_item_input.textSize = mInputTextSize
            } else { //不可输入时，用TextView
                view.et_item_input.visibility = View.GONE
                view.tv_item_input.visibility = View.VISIBLE
                view.tv_item_input.hint = hintText
                view.tv_item_input.text = mInputText
                view.tv_item_input.textColor = mInputTextColor
                view.tv_item_input.textSize = mInputTextSize
            }
        } else {
            view.et_item_input.visibility = View.GONE
            view.tv_item_input.visibility = View.GONE
            view.iv_item_arrow.visibility = View.GONE
        }

        if (!unitText.isNullOrBlank()) {
            view.tv_item_unit.text = unitText
            view.tv_item_unit.textSize = mUnitTextSize
            view.tv_item_unit.textColor = mUnitTextColor
        }

        view.iv_item_arrow.visibility = if (mIsShowArrow) View.VISIBLE else View.GONE

        typedArray?.recycle()
        addView(view)
        val press = context?.getResColor(R.color.bg_item_pressed)
        val normal = context?.getResColor(R.color.bg_item_normal)
        val lineColor = context?.getResColor(R.color.line_color)
        if (press != null && normal != null && lineColor != null) {
            backgroundDrawable = if (isShowBottomLine) {
                BackGroundUtils.pressed(press, normal, 0f, 0f, 0f, 0f,
                        1, lineColor, false, false, false, isShowBottomLine)
            } else {
                BackGroundUtils.background(normal, 0f)
            }
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Logger.w(childCount.toString())
        if (childCount > 1) {
            view.et_item_input.visibility = View.GONE
            view.tv_item_input.visibility = View.GONE
            view.iv_item_arrow.visibility = View.GONE
            view.tv_item_label.gravity = if (mLabelTextGravity == 0) Gravity.RIGHT or Gravity.CENTER_VERTICAL else Gravity.LEFT or Gravity.CENTER_VERTICAL
            view.tv_item_label.textColor = mLabelTextColor
            view.tv_item_label.textSize = mLabelTextSize
            view.tv_item_label.text = mLabelText
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mIsShowInputText) {
            if (mIsCanInput) {
                view.et_item_input.visibility = View.VISIBLE
                view.tv_item_input.visibility = View.GONE
            } else {
                view.et_item_input.visibility = View.GONE
                view.tv_item_input.visibility = View.VISIBLE
            }
        }
    }

    fun setLabelText(label: String?) {
        view.tv_item_label.text = label
    }

    fun setIsShowInputText(isShow: Boolean) {
        view.et_item_input.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    fun setUnitText(text: String?) {
        view.tv_item_unit.text = text
    }

    fun setCanInput(isInput: Boolean) {
        this.mIsCanInput = isInput
        invalidate()
    }

    fun setInputText(text: String?) {
        if (mIsCanInput) {
            view.tv_item_input.visibility = View.GONE
            view.et_item_input.visibility = View.VISIBLE
            view.et_item_input.setText(text ?: "无")
        } else {
            view.et_item_input.visibility = View.GONE
            view.tv_item_input.visibility = View.VISIBLE
            view.tv_item_input.text = text ?: "无"
        }
    }

    fun inputText(): TextView {
        return if (mIsCanInput) {
            view.et_item_input
        } else {
            view.tv_item_input
        }
    }

    fun setShowBottomLine(isShow: Boolean) {
        val press = context?.getResColor(R.color.bg_item_pressed)
        val normal = context?.getResColor(R.color.bg_item_normal)
        val lineColor = context?.getResColor(R.color.line_color)
        if (press != null && normal != null && lineColor != null) {
            backgroundDrawable = if (isShow) {
                BackGroundUtils.pressed(press, normal, 0f, 0f, 0f, 0f,
                        1, lineColor, false, false, false, isShow)
            } else {
                BackGroundUtils.background(normal, 0f)
            }
        }
    }

    fun getInputText(): String {
        return if (mIsCanInput) {
            view.et_item_input.text.toString()
        } else {
            view.tv_item_input.text.toString()
        }
    }

    fun isBlank(): Boolean {
        if (inputText().text.isNullOrBlank()) {
            val text = view.tv_item_label.text.toString().replace("：", "").replace(" ", "")
            context.toast((if (mIsCanInput) "请输入" else "请选择") + text)
            return true
        }
        return false
    }

    fun setShowArrow(isShow: Boolean) {
        view.iv_item_arrow.visibility = if (isShow) View.VISIBLE else View.GONE
    }
}