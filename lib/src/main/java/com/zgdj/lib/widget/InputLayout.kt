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
import kotlinx.android.synthetic.main.layout_input.view.*
import org.jetbrains.anko.*

class InputLayout : LinearLayout {
    companion object {
        const val RIGHT = 0
        const val LEFT = 1

        const val STYLE_SELECTOR = 0
        const val STYLE_INPUT = 1
        const val STYLE_MULTI_LEFT_RIGHT = 2
        const val STYLE_SWITCH = 3
        const val STYLE_DISPLAY = 4
        const val STYLE_TITLE = 5
        const val STYLE_MULTI_UP_DOWN = 6
    }

    val mView: View
    var labelView: TextView? = null
    var colonView: TextView? = null
    var contentView: TextView? = null
    var unitView: TextView? = null
    var switchButton: SwitchButton? = null


    //样式
    var style = STYLE_SELECTOR

    //label
    private val defaultLabelTextColor: Int
        get() = if (style == STYLE_TITLE) 0xff333333.toInt() else color(R.color.text_normal)
    private var labelTextColor: Int = defaultLabelTextColor
    private var labelTextSize: Float = 14f
    private var labelTextGravity: Int = RIGHT

    //colon
    private var isColonVisibility = true


    //content
    private var contentTextColor: Int = color(R.color.text_normal)
    private var contentTextSize: Float = 14f
    private var contentTextGravity: Int = LEFT
    private var contentTextHint: String? = ""
    private var isContentVisibility = true

    //switch
    private var isOpen = true
    private var isShowSwitchText = false
    private var openText: String? = null
    private var closeText: String? = null


    var labelText: String?
        set(value) {
            labelView?.text = value
            field = value
        }

    var contentText: String?
        get() = contentView?.text?.toString()
        set(value) {
            val text = if (value.isNullOrBlank() && style == STYLE_DISPLAY) "/" else value
            contentView?.text = text
            field = text
        }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mView = LayoutInflater.from(context).inflate(R.layout.layout_input, this, false)
        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.InputLayout)
        style = typedArray?.getInt(R.styleable.InputLayout_il_style, style) ?: style
        format(mView)

        //label
        labelText = typedArray?.getString(R.styleable.InputLayout_il_labelText)
        labelTextColor = typedArray?.getColor(R.styleable.InputLayout_il_labelTextColor, defaultLabelTextColor)
            ?: defaultLabelTextColor
        labelTextSize = typedArray?.getFloat(R.styleable.InputLayout_il_labelTextSize, labelTextSize)
            ?: labelTextSize
        labelTextGravity = typedArray?.getInt(R.styleable.InputLayout_il_labelTextGravity, labelTextGravity)
            ?: labelTextGravity

        //colon
        isColonVisibility = typedArray?.getBoolean(R.styleable.InputLayout_il_isColonVisibility, isColonVisibility)
            ?: isColonVisibility

        //content
        contentText = typedArray?.getString(R.styleable.InputLayout_il_contentText)
        contentTextColor = typedArray?.getColor(R.styleable.InputLayout_il_contentTextColor, contentTextColor)
            ?: contentTextColor
        contentTextSize = typedArray?.getFloat(R.styleable.InputLayout_il_contentTextSize, contentTextSize)
            ?: contentTextSize
        contentTextGravity = typedArray?.getInt(R.styleable.InputLayout_il_contentTextGravity, contentTextGravity)
            ?: contentTextGravity
        contentTextHint = typedArray?.getString(R.styleable.InputLayout_il_hint)
            ?: (if (style == STYLE_SELECTOR) "请选择" else "请输入")
        isContentVisibility = typedArray?.getBoolean(R.styleable.InputLayout_il_isContentVisibility, isContentVisibility)
            ?: isContentVisibility

        //switch
        isOpen = typedArray?.getBoolean(R.styleable.InputLayout_il_isOpen, isOpen) ?: isOpen
        isShowSwitchText = typedArray?.getBoolean(R.styleable.InputLayout_il_isShowSwitchText, isShowSwitchText)
            ?: isShowSwitchText
        openText = typedArray?.getString(R.styleable.InputLayout_il_openText)
        closeText = typedArray?.getString(R.styleable.InputLayout_il_closeText)


        val isShowBottomLine = typedArray?.getBoolean(R.styleable.InputLayout_il_isShowBottomLine, true)
            ?: true

        initContent()
        typedArray?.recycle()
        mView.line.visibility = if (isShowBottomLine) View.VISIBLE else View.GONE
        addView(mView)
    }

    fun setLayoutStyle(style: Int) {
        this.style = style
        format(mView)
        initContent()
        invalidate()
    }

    val isSwitchOpen: Boolean
        get() = switchButton?.isOpened ?: false

    fun setShowBottomLine(isShow: Boolean) {
        mView.line.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    fun isBlank(): Boolean {
        if (contentView?.text.isNullOrBlank()) {
            val text = labelView?.text.toString().replace("：", "").replace(" ", "")
            context.toast((if (style == STYLE_SELECTOR) "请选择" else "请输入") + text)
            return true
        }
        return false
    }

    private fun initContent() {
        var labelGravity = Gravity.CENTER_VERTICAL
        var contentGravity = Gravity.CENTER_VERTICAL
        when (style) {
            STYLE_MULTI_UP_DOWN -> {
                contentGravity = Gravity.TOP
            }
            STYLE_MULTI_LEFT_RIGHT -> {
                labelGravity = Gravity.TOP
                contentGravity = Gravity.TOP
            }
        }

        labelView?.let {
            it.text = labelText
            it.textColor = labelTextColor
            it.textSize = labelTextSize
            if (style == STYLE_TITLE) {
                it.gravity = Gravity.LEFT or labelGravity
            } else if (labelTextGravity == RIGHT) {
                it.gravity = Gravity.RIGHT or labelGravity
            } else if (labelTextGravity == LEFT && style != STYLE_MULTI_UP_DOWN) {
                val layoutParams = LayoutParams(wrapContent, wrapContent)
                layoutParams.weight = 0f
                it.gravity = Gravity.LEFT or labelGravity
                it.leftPadding = dip(20)
                it.layoutParams = layoutParams
            }
        }
        colonView?.let {
            it.visibility = if (isColonVisibility) View.VISIBLE else View.GONE
        }
        contentView?.let {
            if (isContentVisibility) {
                it.visibility = View.VISIBLE
                it.text = contentText
                it.textColor = contentTextColor
                it.textSize = contentTextSize
                it.gravity = if (contentTextGravity == RIGHT) Gravity.RIGHT or contentGravity else Gravity.LEFT or contentGravity
                it.hint = contentTextHint
            } else {
                it.visibility = View.INVISIBLE
            }

        }
        unitView?.let {

        }
        switchButton?.let {
            it.isShowText = isShowSwitchText
            it.openText = openText
            it.closeText = closeText
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        context.toast("onDraw()")
    }


    private fun format(view: View) {
        when (style) {
            STYLE_SELECTOR, STYLE_DISPLAY -> {
                viewVisibility(view.layout_selector)
                labelView = view.tv_selector_label
                colonView = view.tv_selector_colon
                contentView = view.tv_selector_content
                unitView = view.tv_selector_unit
                if (style == STYLE_DISPLAY) {
                    view.iv_selector_arrow.visibility = View.GONE
                } else {
                    view.iv_selector_arrow.visibility = View.VISIBLE
                }
            }
            STYLE_INPUT -> {
                viewVisibility(view.layout_input_single)
                labelView = view.tv_input_single_label
                colonView = view.tv_input_single_colon
                contentView = view.et_input_single_content
                unitView = view.tv_input_single_unit
            }
            STYLE_MULTI_LEFT_RIGHT -> {
                viewVisibility(view.layout_input_multi)
                labelView = view.tv_input_multi_label
                colonView = view.tv_input_multi_colon
                contentView = view.et_input_multi_content
            }
            STYLE_SWITCH -> {
                viewVisibility(view.layout_switch)
                labelView = view.tv_switch_label
                switchButton = view.switch_button
            }
            STYLE_TITLE -> {
                viewVisibility(mView.tv_title_label)
                labelView = view.tv_title_label
            }
            STYLE_MULTI_UP_DOWN -> {
                viewVisibility(mView.layout_multi_up_down)
                labelView = view.tv_multi_up_down_label
                contentView = view.et_multi_up_down
            }
        }
    }

    private fun viewVisibility(view: View) {
        mView.layout_selector.visibility = View.GONE
        mView.layout_input_single.visibility = View.GONE
        mView.layout_input_multi.visibility = View.GONE
        mView.layout_switch.visibility = View.GONE
        mView.tv_title_label.visibility = View.GONE
        mView.layout_multi_up_down.visibility = View.GONE
        view.visibility = View.VISIBLE
    }
}