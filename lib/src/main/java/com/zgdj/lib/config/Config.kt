package com.zgdj.lib.config

object Config {

    const val KEY = "key"
    const val DATA = "data"
    const val DATE = "date"
    const val TITLE = "title"
    const val BUTTON = "button"
    const val TYPE = "type"
    const val TYPE_ENGINEERING = "type_engineering"
    const val UNIT_ENGINEERING = "unit_engineering"
    const val PART_ENGINEERING = "part_engineering"
    const val SN = "sn"
    const val PATH = "path"
    const val SIZE = "size"
    const val STATUS = "status"
    const val STAGE = "stage"
    const val IS_LAST_STEP = "is_last_step"
    const val START_TIME = "start_time"
    const val END_TIME = "end_time"
    const val IS_EDITABLE = "is_editable"
    const val DABA_KEY = "daba_key"
    const val NUM = "num"


    const val NET_ERROR = "获取信息失败"


    const val TYPE_READ = 0x00100000 // 新增
    const val TYPE_ADD = 0x00100001 // 新增
    const val TYPE_EDIT = 0x00100002 //编辑
    const val TYPE_DEFAULT = -0x00100003 //其他

    //表单状态
    const val BILL_STATUS_UNSUBMIT = 1
    const val BILL_STATUS_APPROVAL = 2
    const val BILL_STATUS_COMPLETE = 3
    const val BILL_STATUS_REJECTED = -1


    // 1超速报警，2路况报警，3围栏报警，4卸料匹配报警，5超载报警
    const val ALARM_SPEED = 1
    const val ALARM_ROAD = 2
    const val ALARM_FENCE = 3
    const val ALARM_UNLOADING = 4
    const val ALARM_WEIGHT = 5
    const val ALARM_STATISTIC = 6

    const val LOGIN_EXPIRED = "账号信息过期，请重新登录！"
}