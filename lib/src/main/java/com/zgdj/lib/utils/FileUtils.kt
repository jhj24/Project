package com.zgdj.lib.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.widget.ImageView
import com.zgdj.lib.R
import com.zgdj.lib.extention.glide
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.doAsyncResult
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.text.DecimalFormat


object FileUtils {

    private val PROJECT_DIR = "zgdj"
    const val FILE = "file"
    const val CACHE = "cache"
    const val IMAGE = "image"


    fun getSDPath(subDir: String?): String? {

        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {

            var path = Environment.getExternalStorageDirectory().absolutePath

            if (!path.endsWith("/"))
                path += "/"

            path += "$PROJECT_DIR/"

            if (subDir != null && subDir.trim { it <= ' ' }.isNotEmpty())
                path += "$subDir/"

            val f = File(path)

            return if (!f.exists()) {
                if (f.mkdirs())
                    path
                else
                    null
            } else {
                if (f.isFile) {
                    if (f.delete()) {
                        if (f.mkdir())
                            path
                        else
                            null
                    } else
                        null
                } else
                    path
            }
        }
        return null
    }


    fun fileTypeIcon(context: Context, filePath: String, imageView: ImageView) {
        val dotIndex = filePath.lastIndexOf(".")
        if (dotIndex != -1) {
            /* 获取文件的后缀名*/
            val end = filePath.substring(dotIndex).toLowerCase()
            if (end == ".doc" || end == ".docx") {
                imageView.glide(R.mipmap.ic_file_word)
            } else if (end == ".xls" || end == ".xlsx") {
                imageView.glide(R.mipmap.ic_file_excel)
            } else if (end == ".ppt" || end == ".pptx") {
                imageView.glide(R.mipmap.ic_file_ppt)
            } else if (end == ".pdf") {
                imageView.glide(R.mipmap.ic_file_pdf)
            } else {
                imageView.glide(R.mipmap.ic_file_unknow)
            }
        }
    }


    /**
     * 文件大小
     */
    fun formatFileSize(fileS: Long): String {
        val df = DecimalFormat("#.00")
        var fileSizeString = ""
        val wrongSize = "0B"
        if (fileS == 0L) {
            return wrongSize
        }
        if (fileS < 1024) {
            fileSizeString = df.format(fileS.toDouble()) + "B"
        } else if (fileS < 1048576) {
            fileSizeString = df.format(fileS.toDouble() / 1024) + "KB"
        } else if (fileS < 1073741824) {
            fileSizeString = df.format(fileS.toDouble() / 1048576) + "MB"
        } else {
            fileSizeString = df.format(fileS.toDouble() / 1073741824) + "GB"
        }
        return fileSizeString
    }

    @Throws(Exception::class)
    fun getUrlToSteam(path: String): InputStream? {
        return doAsyncResult {
            val url = URL(path)
            val conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 5 * 1000
            conn.requestMethod = "GET"
            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                conn.inputStream
            } else {
                null
            }
        }.get()
    }

    @Throws(Exception::class)
    fun getUrlToBitmap(path: String): Bitmap? {
        return doAsyncResult {
            val url = URL(path)
            val conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 5 * 1000
            conn.requestMethod = "GET"
            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                val stream = conn.inputStream
                BitmapFactory.decodeStream(stream)
            } else {
                null
            }
        }.get()
    }


    /**
     * stream 转 bitmap
     */
    fun streamToBitmap(stream: InputStream?): Bitmap? {
        // 将所有InputStream写到byte数组当中
        var targetData: ByteArray = byteArrayOf()
        val bytePart = ByteArray(4096)
        if (stream == null) {
            return null
        }
        while (true) {
            try {
                val readLength = stream.read(bytePart)
                if (readLength == -1) {
                    break
                } else {
                    val temp = ByteArray(readLength + targetData.size)
                    if (targetData.isNotEmpty()) {
                        System.arraycopy(targetData, 0, temp, 0, targetData.size)
                        System.arraycopy(bytePart, 0, temp, targetData.size, readLength)
                    } else {
                        System.arraycopy(bytePart, 0, temp, 0, readLength)
                    }
                    targetData = temp
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        // 指使Bitmap通过byte数组获取数据
        return BitmapFactory.decodeByteArray(targetData, 0, targetData.size)
    }


    /**
     * 保存文件
     */
    @Throws(IOException::class)
    fun saveFile(bm: Bitmap?, path: String) {
        GlobalScope.launch {
            try {
                if (bm != null) {
                    val myCaptureFile = File(path)
                    val bos = BufferedOutputStream(FileOutputStream(myCaptureFile))
                    bm.compress(Bitmap.CompressFormat.PNG, 80, bos)
                    bos.flush()
                    bos.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Stream 转 File
     */
    fun streamToFile(stream: InputStream, path: String): File {
        val file = File(path)
        val os = FileOutputStream(file)
        val buffer = ByteArray(8192)
        var bytesRead = stream.read(buffer, 0, 8192)
        while (bytesRead != -1) {
            os.write(buffer, 0, bytesRead)
            bytesRead = stream.read(buffer, 0, 8192)
        }
        os.close()
        stream.close()
        return file
    }
}