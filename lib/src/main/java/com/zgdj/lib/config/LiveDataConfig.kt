package com.zgdj.lib.config

object LiveDataConfig {


    /**
     * 刷新，用于LiveDataBus事件信息传递参数
     *
     * 1、消息个数
     */
    const val EVENT_REFRESH = "event_refresh"

    /**
     * 单元工程Key传递，用户选择检测报告
     */

    const val CELL_KEY = "cell_key" //
    /**
     * 刷新小红点
     */
    const val MESSAGE_NUM_STATISTIC = "message_num_statistic"


    /**
     * 个人资料头像修改
     */
    const val AVATAR_CHANGE = "avatar_change" //头像
    const val PIT = "pit"

    const val APPROVAL_REFRESH = "approval_refresh" //审批流程结束，刷新数据

    const val PATROL_PROBLEM_REFRESH = "patrol_problem_refresh"

}