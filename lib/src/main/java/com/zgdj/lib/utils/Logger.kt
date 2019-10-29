package com.zgdj.lib.utils

import android.util.Log
import com.zgdj.lib.BuildConfig

object Logger {
    private const val TAG = "quality"
    private const val index = 5
    private val isDebug = BuildConfig.DEBUG

    fun e(msg: String) {
        if (isDebug) {
            getLoggerList(msg).forEach { Log.e(it.first, it.second) }
        }
    }

    fun w(msg: String) {
        if (isDebug) {
            getLoggerList(msg).forEach { Log.w(it.first, it.second) }
        }
    }

    fun d(msg: String) {
        if (isDebug) {
            getLoggerList(msg).forEach { Log.d(it.first, it.second) }
        }
    }

    fun i(msg: String) {
        if (isDebug) {
            getLoggerList(msg).forEach { Log.i(it.first, it.second) }
        }
    }


    private fun getLoggerList(msg: String): List<Pair<String, String>> {
        val stackTraceElement = getStackTraceElement()
        return listOf(
                Pair(TAG, "||========================================================"),
                Pair(TAG, "|| className ： ${stackTraceElement.className}"),
                Pair(TAG, "|| fileName  ： ${stackTraceElement.fileName}"),
                Pair(TAG, "|| methodName： ${stackTraceElement.methodName}"),
                Pair(TAG, "|| lineNumber： ${stackTraceElement.lineNumber}"),
                Pair(TAG, "|| message   ： $msg"),
                Pair(TAG, "||========================================================")

        )
    }


    private fun getStackTraceElement(): StackTraceElement {
        val stackTraceElementList = Thread.currentThread().stackTrace
        return stackTraceElementList[index]
    }
}