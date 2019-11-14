package com.zgdj.lib.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import com.jhj.httplibrary.HttpCall
import com.jhj.httplibrary.callback.DownloadHttpCallback
import com.jhj.httplibrary.utils.MimeTypeUtils
import com.zgdj.lib.BuildConfig
import com.zgdj.lib.extention.isUrl
import com.zgdj.lib.extention.loadingDialog
import com.zgdj.lib.extention.messageDialog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.toast
import java.io.File
import java.util.*

object WPSCheckUtils {

    fun openFile(activity: Activity, url: String, fileSize: Long) {
        if (url.isUrl()) {//网络地址要先下载
            fileDownLoad(activity, url, fileSize)
        } else {
            checkWps(activity, url)
        }
    }


    @SuppressLint("CheckResult")
    fun fileDownLoad(activity: Activity, downLoadUrl: String, fileSize: Long) {
        GlobalScope.launch {
            try {
                val index = downLoadUrl.lastIndexOf("/")
                val name = downLoadUrl.substring(index, downLoadUrl.length)
                val file = File(FileUtils.getSDPath(FileUtils.FILE + File.separator) + name)
                if (file.exists()) {
                    if (file.length() == fileSize) { //直接查看
                        checkWps(activity, downLoadUrl)
                    } else { //加载未下载的部分
                        downLoadFromNet(activity, downLoadUrl)
                    }
                } else { //重新下载
                    downLoadFromNet(activity, downLoadUrl)
                }
            } catch (e: java.lang.Exception) {
                activity.toast("文件打开失败")
            }
        }
    }


    private fun downLoadFromNet(activity: Activity, url: String) {
        val index = url.lastIndexOf("/")
        val name = url.substring(index, url.length)
        val file = File(FileUtils.getSDPath("file" + File.separator) + name)
        val dialog = activity.loadingDialog()
        HttpCall.download(url)
            .setUseBaseUrl(false)
            .enqueue(object : DownloadHttpCallback(activity, file) {
                override fun onSuccess(file: File) {
                    checkWps(activity, url)
                }

                override fun onFailure(msg: String) {
                    if (file.exists()) {
                        file.delete()
                    }
                }

                override fun onFinish() {
                    super.onFinish()
                    dialog.dismiss()
                }
            })
    }


    /**
     * 查看是否安装WPS
     *
     * @param filePath 文件名称
     * @return 是否安装
     */
    fun checkWps(context: Context, filePath: String): Boolean {
        val dotIndex = filePath.lastIndexOf(".")
        if (dotIndex != -1) {
            val fileType = filePath.substring(dotIndex)
            val docTypes = Arrays.asList(".doc", ".docx", ".xls", ".xlsx", ".pdf", ".ppt", ".pptx")
            if (docTypes.contains(fileType) && !isInstalled(context, "cn.wps.moffice_eng")) {
                context.messageDialog(msg = "您手机上没有安装wps,可能无法正常打开该文件,是否下载？") { alertFragment, view ->
                    val it = Intent(Intent.ACTION_VIEW, Uri.parse("http://125.46.106.52/download/wps.apk"))
                    it.setClassName("com.android.browser", "com.android.browser.BrowserActivity")
                    val activity = context as Activity
                    activity.startActivity(it)
                }
                return false
            }
        }
        open(context, filePath)
        return true
    }

    private fun open(context: Context, filePath: String) {
        try {
            val file = File(filePath)
            val mineType = MimeTypeUtils.getFileType(filePath)
            val intent = Intent()
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addCategory("android.intent.category.DEFAULT")
            //设置intent的Action属性
            intent.action = Intent.ACTION_VIEW
            //设置intent的data和Type属性。
            //判断是否是AndroidN以及更高的版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION //历史授权
                val contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
                intent.setDataAndType(contentUri, mineType)
            } else {
                intent.setDataAndType(/*uri*/Uri.fromFile(file), mineType)
            }
            //跳转
            context.startActivity(intent)
        } catch (e: Exception) {//无法打开该文件时 检测下载WPS
            checkWps(context, filePath)
            e.printStackTrace()
        }
    }


    /**
     * 判断是否安装某个程序
     */
    private fun isInstalled(context: Context, pkg: String): Boolean {

        if (!isEmpty(pkg)) {
            val list = getInstalledPackages(context)
            if (list.isNotEmpty()) {
                for (aList in list) {
                    val pi = aList
                    if (pkg.equals(pi.packageName, ignoreCase = true)) {
                        return true
                    }
                }
            }
        }

        return false
    }

    fun isEmpty(str: CharSequence?): Boolean {
        return null == str || str.length == 0
    }

    fun getInstalledPackages(context: Context): List<PackageInfo> {
        return context.packageManager.getInstalledPackages(0)
    }


}