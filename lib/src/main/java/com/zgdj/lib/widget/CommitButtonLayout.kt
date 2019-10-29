package com.zgdj.lib.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.zgdj.lib.R
import kotlinx.android.synthetic.main.layout_operate_button.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class CommitButtonLayout : LinearLayout {

    private var view: View

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        view = LayoutInflater.from(context).inflate(R.layout.layout_operate_button, this, false)
        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.CommitButtonLayout)
        val commitText = typedArray?.getString(R.styleable.CommitButtonLayout_commitText) ?: "保存"
        val rejectText = typedArray?.getString(R.styleable.CommitButtonLayout_rejectText) ?: "取消"
        val isShowRejectButton = typedArray?.getBoolean(R.styleable.CommitButtonLayout_isShowRejectButton, false)
        if (isShowRejectButton == true) {
            view.btn_reject.text = rejectText
            view.btn_reject.visibility = View.VISIBLE
        } else {
            view.btn_reject.visibility = View.GONE
        }
        view.btn_commit.text = commitText
        typedArray?.recycle()
        addView(view)
    }

    fun getCommitView(): TextView {
        return view.btn_commit
    }

    fun getRejectView(): TextView {
        return view.btn_reject
    }

    fun setCommitText(text: String?) {
        view.btn_commit.text = text
    }

    fun setOnCommitListener(body: () -> Unit) {
        view.btn_commit.onClick {
            body()
        }
    }

    fun setCommitVisibility(isVisibility: Boolean) {
        view.btn_commit.visibility = if (isVisibility) View.VISIBLE else View.GONE
    }

    fun setRejectVisibility(isVisibility: Boolean) {
        view.btn_reject.visibility = if (isVisibility) View.VISIBLE else View.GONE
    }

    fun setOnRejectListener(body: () -> Unit) {
        view.btn_reject.onClick {
            body()
        }
    }
}