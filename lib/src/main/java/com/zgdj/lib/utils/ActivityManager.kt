package com.zgdj.lib.utils

import android.app.Activity
import java.util.*

/**
 * Activity管理器
 */
object ActivityManager {

    val activityStack = Stack<Activity>()

    val topActivity: Activity
        get() = activityStack.peek()

    /**
     * @return 已经打开activity的数量
     */
    val activityCount: Int
        get() = activityStack.size

    fun addActivity(activity: Activity) {
        activityStack.push(activity)
    }

    fun removeActivity(activity: Activity) {
        activityStack.remove(activity)
    }

    /**
     * finish对应class的所有activity
     *
     * @param cls 要关闭的Activity Class
     */
    fun finishActivity(vararg cls: Class<out Activity>) {
        for (i in activityStack.indices.reversed()) {
            val activity = activityStack[i]
            for (c in cls) {
                if (c.name == activity.javaClass.name) {
                    finishActivity(activity)
                }
            }
        }
    }


    /**
     * 关闭栈顶的Activity
     */
    fun finishTopActivity() {
        val pop = activityStack.pop()
        if (!pop.isFinishing) {
            pop.finish()
        }
    }


    /**
     * finish除白名单以外的所有activity
     *
     * @param activityWhitelist 要保留的activity
     */
    fun finishAllActivityByWhitelist(vararg activityWhitelist: Class<out Activity>) {
        for (i in activityStack.indices.reversed()) {
            val activity = activityStack[i]
            for (c in activityWhitelist) {
                if (c.name == activity.javaClass.name) {
                    break
                } else {
                    finishActivity(activity)
                }
            }
        }
    }


    /**
     * finish所有activity
     */
    fun finishAllActivity() {
        for (i in activityStack.indices.reversed()) {
            val activity = activityStack[i]
            finishActivity(activity)
        }
    }


    private fun finishActivity(activity: Activity?) {
        if (activity != null) {
            if (!activity.isFinishing) {
                activity.finish()
                activityStack.remove(activity)
            }
        }
    }

    /**
     * @param activity
     * @return 是否存在此activity
     */
    fun isContainsActivity(activity: Activity): Boolean {
        return activityStack.contains(activity)
    }


    fun isContainsActivity(activityClass: Class<out Activity>): Boolean {
        for (i in activityStack.indices.reversed()) {
            val activity = activityStack[i]
            if (activityClass.name == activity.javaClass.name) {
                return true
            }
        }
        return false
    }
}