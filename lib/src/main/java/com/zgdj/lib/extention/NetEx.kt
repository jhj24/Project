package com.zgdj.lib.extention

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

@Throws(Exception::class)
suspend fun String.fileSize(): Long {
    return withContext(Dispatchers.Default) {
        if (isUrl()) {
            val url = URL(this@fileSize)
            val urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.connectTimeout = 5 * 1000
            urlConnection.requestMethod = "GET"
            urlConnection.connect()
            urlConnection.contentLength.toLong()
        } else {
            val file = File(this@fileSize)
            file.length()
        }
    }
}


