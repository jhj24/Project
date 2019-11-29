package com.zgdj.lib.base.fragment

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import com.zgdj.lib.R

abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    open val topOffset = -1

    abstract val layoutRes: Int

    lateinit var mActivity: FragmentActivity
    lateinit var mParentView: View

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = context as FragmentActivity
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return if (context != null) {
            BottomSheetDialog(context!!, R.style.TransparentBottomSheetStyle)
        } else {
            super.onCreateDialog(savedInstanceState)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutRes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mParentView = view
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog as BottomSheetDialog
        val frameLayout = dialog.delegate.findViewById<FrameLayout>(android.support.design.R.id.design_bottom_sheet)
        if (frameLayout != null) {
            if (topOffset != -1) {
                val layoutParams = frameLayout.layoutParams as CoordinatorLayout.LayoutParams
                layoutParams.height = getHeight()
            }
            val behavior = BottomSheetBehavior.from(frameLayout)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun getHeight(): Int {
        var height = 1920
        val wm = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
        val point = Point()
        if (wm != null) {
            // 使用Point已经减去了状态栏高度
            wm.defaultDisplay.getSize(point)
            height = point.y - topOffset
        }
        return height
    }
}