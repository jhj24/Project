package com.zgdj.lib.utils.alive

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.zgdj.lib.utils.Logger

class KeppReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        /** 锁屏启动一像素act */
        if (TextUtils.equals(action, Intent.ACTION_SCREEN_OFF)) {
            KeepManger.getInstance().startOnePxActivty(context)
            Logger.w("监听到关屏广播")
            /** 开屏结束一像素act */
        } else if (TextUtils.equals(action, Intent.ACTION_SCREEN_ON)) {
            KeepManger.getInstance().finishOnePxActivty()
            Logger.w("监听到开屏广播")
        }
    }
}