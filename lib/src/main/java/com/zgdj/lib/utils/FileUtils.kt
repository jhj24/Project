package com.zgdj.lib.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.widget.ImageView
import com.zgdj.lib.R
import com.zgdj.lib.extention.image
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

        if (Environment.MEDIA_MOUNTED == Environment
                .getExternalStorageState()
        ) {

            var path = Environment.getExternalStorageDirectory()
                .absolutePath

            if (!path.endsWith("/"))
                path += "/"

            path += PROJECT_DIR + "/"

            if (subDir != null && subDir.trim { it <= ' ' }.length > 0)
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
                context.image(R.mipmap.ic_file_word, imageView)
            } else if (end == ".xls" || end == ".xlsx") {
                context.image(R.mipmap.ic_file_excel, imageView)
            } else if (end == ".ppt" || end == ".pptx") {
                context.image(R.mipmap.ic_file_ppt, imageView)
            } else if (end == ".pdf") {
                context.image(R.mipmap.ic_file_pdf, imageView)
            } else {
                context.image(R.mipmap.ic_file_unknow, imageView)
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
     * 获取文件的MIME类型
     *
     * @param name 文件名
     * @return 文件类型
     */
    fun getFileType(name: String): String {
        var fileType = "*/*"
        val dotIndex = name.lastIndexOf(".")
        if (dotIndex != -1) {
            /* 获取文件的后缀名*/
            val end = name.substring(dotIndex).toLowerCase()
            //在MIME和文件类型的匹配表中找到对应的MIME类型。
            for (aMIME_MapTable in MIME_MapTable) { //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
                if (end == aMIME_MapTable[0])
                    fileType = aMIME_MapTable[1]
            }
        }
        return fileType
    }

    private val MIME_MapTable = arrayOf(
        //{后缀名，MIME类型}
        arrayOf(".3gp", "video/3gpp"),
        arrayOf(".apk", "application/vnd.android.package-archive"),
        arrayOf(".asf", "video/x-ms-asf"),
        arrayOf(".avi", "video/x-msvideo"),
        arrayOf(".bin", "application/octet-stream"),
        arrayOf(".bmp", "image/bmp"),
        arrayOf(".c", "text/plain"),
        arrayOf(".class", "application/octet-stream"),
        arrayOf(".conf", "text/plain"),
        arrayOf(".cpp", "text/plain"),
        arrayOf(".doc", "application/msword"),
        arrayOf(".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
        arrayOf(".xls", "application/vnd.ms-excel"),
        arrayOf(".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
        arrayOf(".exe", "application/octet-stream"),
        arrayOf(".gif", "image/gif"),
        arrayOf(".gtar", "application/x-gtar"),
        arrayOf(".gz", "application/x-gzip"),
        arrayOf(".h", "text/plain"),
        arrayOf(".htm", "text/html"),
        arrayOf(".html", "text/html"),
        arrayOf(".jar", "application/java-archive"),
        arrayOf(".java", "text/plain"),
        arrayOf(".jpeg", "image/jpeg"),
        arrayOf(".jpg", "image/jpeg"),
        arrayOf(".js", "application/x-javascript"),
        arrayOf(".log", "text/plain"),
        arrayOf(".m3u", "audio/x-mpegurl"),
        arrayOf(".m4a", "audio/mp4a-latm"),
        arrayOf(".m4b", "audio/mp4a-latm"),
        arrayOf(".m4p", "audio/mp4a-latm"),
        arrayOf(".m4u", "video/vnd.mpegurl"),
        arrayOf(".m4v", "video/x-m4v"),
        arrayOf(".mov", "video/quicktime"),
        arrayOf(".mp2", "audio/x-mpeg"),
        arrayOf(".mp3", "audio/x-mpeg"),
        arrayOf(".mp4", "video/mp4"),
        arrayOf(".mpc", "application/vnd.mpohun.certificate"),
        arrayOf(".mpe", "video/mpeg"),
        arrayOf(".mpeg", "video/mpeg"),
        arrayOf(".mpg", "video/mpeg"),
        arrayOf(".mpg4", "video/mp4"),
        arrayOf(".mpga", "audio/mpeg"),
        arrayOf(".msg", "application/vnd.ms-outlook"),
        arrayOf(".ogg", "audio/ogg"),
        arrayOf(".pdf", "application/pdf"),
        arrayOf(".png", "image/png"),
        arrayOf(".pps", "application/vnd.ms-powerpoint"),
        arrayOf(".ppt", "application/vnd.ms-powerpoint"),
        arrayOf(".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"),
        arrayOf(".prop", "text/plain"),
        arrayOf(".rc", "text/plain"),
        arrayOf(".rmvb", "audio/x-pn-realaudio"),
        arrayOf(".rtf", "application/rtf"),
        arrayOf(".sh", "text/plain"),
        arrayOf(".tar", "application/x-tar"),
        arrayOf(".tgz", "application/x-compressed"),
        arrayOf(".txt", "text/plain"),
        arrayOf(".wav", "audio/x-wav"),
        arrayOf(".wma", "audio/x-ms-wma"),
        arrayOf(".wmv", "audio/x-ms-wmv"),
        arrayOf(".wps", "application/vnd.ms-works"),
        arrayOf(".xml", "text/plain"),
        arrayOf(".z", "application/x-compress"),
        arrayOf(".zip", "application/x-zip-compressed"),
        arrayOf("", "*/*")
    )


}