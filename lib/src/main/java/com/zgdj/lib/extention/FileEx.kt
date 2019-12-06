package com.zgdj.lib.extention

import com.jhj.httplibrary.HttpCall
import com.zgdj.lib.R
import java.io.File
import java.text.DecimalFormat

/**
 * 文件名
 */
val String.fileName: String
    get() {
        val index = this.lastIndexOf("/")
        if (index != -1) {
            return substring(index + 1, this.length)
        }
        return this
    }

/**
 * 文件后缀（eg、".png",".doc"）
 */
val String.fileSuffix: String
    get() {
        val index = this.lastIndexOf(".")
        if (index != -1) {
            return substring(index, this.length)
        }
        return ""
    }

/**
 * 格式化文件路径
 */
val String?.filePath: String
    get() {
        if (this != null) {
            return if (isUrl()) {
                this.replace("\\", "/")
            } else {
                HttpCall.baseUrl + this.replace("\\", "/")
            }
        }
        return ""
    }

/**
 * 文件类型对应的图标
 */
val String.fileTypeIcon: Int
    get() = when (this.fileSuffix) {
        ".doc", ".docx" -> R.mipmap.ic_file_word
        ".xls", ".xlsx" -> R.mipmap.ic_file_excel
        ".ppt", ".pptx" -> R.mipmap.ic_file_ppt
        ".pdf" -> R.mipmap.ic_file_pdf
        else -> R.mipmap.ic_file_unknow
    }


val File.fileSize: String
    get() {
        val length = this.length()
        val df = DecimalFormat("#.00")
        var fileSizeString = ""
        val wrongSize = "0B"
        if (length == 0L) {
            return wrongSize
        }
        if (length < 1024) {
            fileSizeString = df.format(length.toDouble()) + "B"
        } else if (length < 1024 * 1024) {
            fileSizeString = df.format(length.toDouble() / 1024) + "KB"
        } else if (length < 1024*1024*1024) {
            fileSizeString = df.format(length.toDouble() / (1024*1024)) + "MB"
        } else {
            fileSizeString = df.format(length.toDouble() / 1073741824) + "GB"
        }
        return fileSizeString
    }
