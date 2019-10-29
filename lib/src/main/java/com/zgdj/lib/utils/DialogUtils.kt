package com.zgdj.lib.utils

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.View
import com.jhj.httplibrary.HttpCall
import com.jhj.prompt.fragment.AlertFragment
import com.jhj.prompt.fragment.LoadingFragment
import com.jhj.prompt.fragment.PercentFragment
import com.jhj.prompt.fragment.base.OnDialogShowOnBackListener
import com.zgdj.lib.extention.toArrayList

object DialogUtils {

    fun customDialog(context: Context, layoutRes: Int, body: (View, AlertFragment) -> Unit) {
        AlertFragment.Builder(context)
                .setCustomLayoutRes(layoutRes, object : AlertFragment.OnCustomListener {
                    override fun onLayout(alert: AlertFragment, view: View) {
                        body(view, alert)
                    }
                })
                .show()
    }

    fun bottomCustomDialog(context: Context, layoutRes: Int, body: (View, AlertFragment) -> Unit): AlertFragment.Builder {
        return AlertFragment.Builder(context)
                .setMarginHorizontal(0)
                .setMarginBottom(0)
                .setDialogGravity(Gravity.BOTTOM)
                .setCustomLayoutRes(layoutRes, object : AlertFragment.OnCustomListener {
                    override fun onLayout(alert: AlertFragment, view: View) {
                        body(view, alert)
                    }
                })
                .show()
    }

    fun bottomSingleDialog(context: Context, title: String, list: List<String>, body: (AlertFragment, String) -> Unit) {
        AlertFragment.Builder(context)
                .setDataList(list.toArrayList())
                .setDialogGravity(Gravity.BOTTOM)
                .setDialogBottomSeparate(true)
                .setTitle(title)
                .setItemClickedListener(object : AlertFragment.OnItemClickListener {
                    override fun onItemClick(alert: AlertFragment, view: View, position: Int) {
                        body(alert, list[position])
                    }
                })
                .setCancelListener(object : AlertFragment.OnButtonClickedListener {
                    override fun onClick(alert: AlertFragment, view: View?) {

                    }
                })
                .show()
    }

    fun bottomMultiDialog(context: Context, title: String, list: List<String>, selectedList: List<Int>, body: (AlertFragment, List<Int>) -> Unit) {
        AlertFragment.Builder(context)
                .setSelectedDataList(list.toArrayList())
                .setSelectedItem(selectedList.toArrayList())
                .setDialogGravity(Gravity.BOTTOM)
                .setDialogBottomSeparate(true)
                .setTitle(title)
                .setListSelectedListener(object : AlertFragment.OnItemSelectedListener {
                    override fun onSelected(alert: AlertFragment, selectedList: List<Int>) {
                        body(alert, selectedList)
                    }
                })
                .show()
    }

    fun uploadDialog(context: Context, msg: String, body: (PercentFragment) -> Unit): PercentFragment.Builder {
        return PercentFragment.Builder(context)
                .setText(msg)
                .setDialogShowOnBackListener(object : OnDialogShowOnBackListener<PercentFragment> {
                    override fun cancel(fragment: PercentFragment) {
                        body(fragment)
                    }
                })
                .setCancelOnTouchOut(false)
                .show()
    }

    fun uploadDialog(context: Context, msg: String, body: (LoadingFragment) -> Unit): LoadingFragment.Builder {
        return LoadingFragment.Builder(context)
                .setText(msg)
                .setCancelOnTouchOut(false)
                .setDialogShowOnBackListener(object : OnDialogShowOnBackListener<LoadingFragment> {
                    override fun cancel(fragment: LoadingFragment) {
                        body(fragment)
                    }
                })
                .show()
    }

    fun downloadDialog(context: Context, tag: Any, text: String, body: (PercentFragment) -> Unit): PercentFragment.Builder {
        return PercentFragment.Builder(context)
                .setDialogShowOnBackListener(object : OnDialogShowOnBackListener<PercentFragment> {
                    override fun cancel(fragment: PercentFragment) {
                        if (context is Activity) context.finish()
                        HttpCall.pauseDownload(tag)
                        body(fragment)
                    }
                })
                .setText(text)
                .setCancelOnTouchOut(false)
                .show()
    }

    fun loadingDialog(context: Context, text: String, body: (LoadingFragment) -> Unit = {}): LoadingFragment.Builder {
        return LoadingFragment.Builder(context)
                .setText(text)
                .setCancelOnTouchOut(false)
                .setDialogShowOnBackListener(object : OnDialogShowOnBackListener<LoadingFragment> {
                    override fun cancel(fragment: LoadingFragment) {
                        if (context is Activity) {
                            context.finish()
                        }
                        body(fragment)
                    }
                })
                .show()
    }

    fun messageDialog(context: Context, title: String = "提示", msg: String, body: (AlertFragment, View?) -> Unit) {
        AlertFragment.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setSubmitListener(object : AlertFragment.OnButtonClickedListener {
                    override fun onClick(alert: AlertFragment, view: View?) {
                        body(alert, view)
                    }
                })
                .setCancelListener(object : AlertFragment.OnButtonClickedListener {
                    override fun onClick(alert: AlertFragment, view: View?) {

                    }
                })
                .show()
    }
}