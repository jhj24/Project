package com.zgdj.lib.ui

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.zgdj.lib.R
import com.zgdj.lib.base.activity.BaseActivity
import com.zgdj.lib.config.Config
import com.zgdj.lib.extention.download
import com.zgdj.lib.extention.isNetworkConnected
import com.zgdj.lib.extention.isUrl
import com.zgdj.lib.net.callback.DownloadDialogHttpCallback
import com.zgdj.lib.utils.FileUtils
import kotlinx.android.synthetic.main.activity_file_display.*
import org.jetbrains.anko.toast
import java.io.File


class FileDisplayActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_display)
        //initTopBar("表单", View.VISIBLE)
        val fileUrl = intent.getStringExtra(Config.PATH) //文件路径
        val fileSize = intent.getLongExtra(Config.SIZE, 0L)

        requestPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE) {
            file_view.setOnGetFilePathListener(fileSize) {
                if (fileUrl.isUrl()) {//网络地址要先下载
                    fileDownLoad(fileUrl, fileSize)
                } else {
                    file_view.displayFile(File(fileUrl))
                }
            }
            file_view.show()
        }
    }

    @SuppressLint("CheckResult")
    fun fileDownLoad(downLoadUrl: String, fileSize: Long) {
        val index = downLoadUrl.lastIndexOf("/")
        val name = downLoadUrl.substring(index + 1, downLoadUrl.length)
        val file = File(FileUtils.getSDPath(FileUtils.FILE + File.separator) + name)


        if (isNetworkConnected() && fileSize != 0L) {
            if (file.exists()) {
                if (file.length() == fileSize) { //直接查看
                    file_view.displayFile(file)
                } else { //加载未下载的部分
                    downLoadFromNet(downLoadUrl, file)
                }
            } else { //重新下载
                downLoadFromNet(downLoadUrl, file)
            }
        } else if (file.exists()) {
            try {
                file_view.displayFile(file)
            } catch (e: Exception) {
                finish()
                toast("网络连接失败，请联网后重试")
            }
        } else {
            toast("文件打开失败")
            finish()
        }
    }


    private fun downLoadFromNet(url: String, file: File) {
        download(url)
            //.tag(this) todo 加上 TAG 后，不在该界面取消下载
            .enqueue(object : DownloadDialogHttpCallback(this, downloadFile = file) {

                override fun onSuccess(file: File) {
                    if (!isFinishing) {
                        file_view.displayFile(file)
                    }
                }

                override fun onFailure(msg: String) {
                    if (file.exists()) {
                        file.delete()
                    }
                }
            })
    }

    public override fun onDestroy() {
        super.onDestroy()
        file_view.onStopDisplay()
    }
}
