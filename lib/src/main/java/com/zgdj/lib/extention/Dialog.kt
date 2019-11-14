package com.zgdj.lib.extention

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.jhj.httplibrary.HttpCall
import com.jhj.prompt.fragment.AlertFragment
import com.jhj.prompt.fragment.LoadingFragment
import com.jhj.prompt.fragment.PercentFragment
import com.jhj.prompt.fragment.base.OnDialogShowOnBackListener
import com.zgdj.lib.R
import kotlinx.android.synthetic.main.dialog_edit_and_detele.view.*
import org.jetbrains.anko.alarmManager
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sdk27.coroutines.onTouch
import org.w3c.dom.Text


fun Fragment.customDialog(layoutRes: Int, body: (View, AlertFragment) -> Unit) {
    context?.customDialog(layoutRes, body)
}

fun Fragment.bottomCustomDialog(layoutRes: Int, body: (View, AlertFragment) -> Unit): AlertFragment.Builder? {
    return context?.bottomCustomDialog(layoutRes, body)
}

fun Fragment.bottomSingleDialog(title: String = "请选择", list: List<String>, body: (AlertFragment, String) -> Unit) {
    context?.bottomSingleDialog(title, list, body)
}

fun Fragment.bottomMultiDialog(title: String = "请选择", list: List<String>, selectedList: List<Int>, body: (AlertFragment, List<Int>) -> Unit) {
    context?.bottomMultiDialog(title, list, selectedList, body)
}

fun Fragment.percentUploadDialog(msg: String = "正在上传...", body: (PercentFragment) -> Unit): PercentFragment.Builder? {
    return context?.percentUploadDialog(msg, body)
}

fun Fragment.uploadDialog(msg: String = "正在上传...", body: (LoadingFragment) -> Unit): LoadingFragment.Builder? {
    return context?.uploadDialog(msg, body)
}

fun Fragment.downloadDialog(tag: Any, text: String = "正在下载...", body: (PercentFragment) -> Unit): PercentFragment.Builder? {
    return context?.downloadDialog(tag, text, body)
}

fun Fragment.loadingDialog(text: String = "正在下载...", body: (LoadingFragment) -> Unit = {}): LoadingFragment.Builder? {
    return context?.loadingDialog(text, body)
}

fun Fragment.messageDialog(title: String = "提示", msg: String, body: (AlertFragment, View?) -> Unit = { _, _ -> }) {
    context?.messageDialog(title, msg, body)
}

fun Context.customDialog(layoutRes: Int, body: (View, AlertFragment) -> Unit) {
    AlertFragment.Builder(this)
            .setCustomLayoutRes(layoutRes, object : AlertFragment.OnCustomListener {
                override fun onLayout(alert: AlertFragment, view: View) {
                    body(view, alert)
                }
            })
            .show()
}

fun Context.bottomCustomDialog(layoutRes: Int, body: (View, AlertFragment) -> Unit): AlertFragment.Builder {
    return AlertFragment.Builder(this)
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

fun Context.bottomEditDialog(title: String = "提示", edit: AlertFragment.(TextView) -> Unit, delete: AlertFragment.(TextView) -> Unit) {
    bottomCustomDialog(R.layout.dialog_edit_and_detele) { view, alertFragment ->
        view.tv_dialog_name.text = title
        alertFragment.edit(view.tv_dialog_edit)
        alertFragment.delete(view.tv_dialog_delete)
    }
}

fun Context.bottomSingleDialog(title: String = "请选择", list: List<String>, body: (AlertFragment, String) -> Unit) {
    AlertFragment.Builder(this)
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

fun Context.bottomMultiDialog(title: String = "请选择", list: List<String>, selectedList: List<Int>, body: (AlertFragment, List<Int>) -> Unit) {
    AlertFragment.Builder(this)
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

fun Context.percentUploadDialog(msg: String = "正在上传...", body: (PercentFragment) -> Unit): PercentFragment.Builder {
    return PercentFragment.Builder(this)
            .setText(msg)
            .setDialogShowOnBackListener(object : OnDialogShowOnBackListener<PercentFragment> {
                override fun cancel(fragment: PercentFragment) {
                    body(fragment)
                }
            })
            .setCancelOnTouchOut(false)
            .show()
}

fun Context.uploadDialog(msg: String = "正在上传...", body: (LoadingFragment) -> Unit): LoadingFragment.Builder {
    return LoadingFragment.Builder(this)
            .setText(msg)
            .setCancelOnTouchOut(false)
            .setDialogShowOnBackListener(object : OnDialogShowOnBackListener<LoadingFragment> {
                override fun cancel(fragment: LoadingFragment) {
                    body(fragment)
                }
            })
            .show()
}

fun Context.downloadDialog(tag: Any, text: String = "正在下载...", body: (PercentFragment) -> Unit = {}): PercentFragment.Builder {
    return PercentFragment.Builder(this)
            .setDialogShowOnBackListener(object : OnDialogShowOnBackListener<PercentFragment> {
                override fun cancel(fragment: PercentFragment) {
                    if (this@downloadDialog is Activity) {
                        this@downloadDialog.finish()
                    }
                    HttpCall.pauseDownload(tag)
                    body(fragment)
                }
            })
            .setText(text)
            .setCancelOnTouchOut(false)
            .show()
}

fun Context.loadingDialog(text: String = "正在下载...", body: (LoadingFragment) -> Unit = {}): LoadingFragment.Builder {
    return LoadingFragment.Builder(this)
            .setText(text)
            .setCancelOnTouchOut(false)
            .setDialogShowOnBackListener(object : OnDialogShowOnBackListener<LoadingFragment> {
                override fun cancel(fragment: LoadingFragment) {
                    if (this@loadingDialog is Activity) {
                        this@loadingDialog.finish()
                    }
                    body(fragment)
                }
            })
            .show()
}

fun Context.messageDialog(title: String = "提示", msg: String, body: (AlertFragment, View?) -> Unit = { _, _ -> }) {
    AlertFragment.Builder(this)
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