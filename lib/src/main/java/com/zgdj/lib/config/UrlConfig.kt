package com.zgdj.lib.config

import com.zgdj.lib.BuildConfig


object UrlConfig {

    //const val BASE_URL = "http://192.168.1.77:99"
    const val BASE_URL = "http://zzbhidi.f3322.net:10092"
    const val PYTHON_URL = "http://192.168.1.77:8003" //Python 服务器
    const val MAP_URL = "http://192.168.1.77:96" //地图服务器
    const val BILL_URL = "http://192.168.1.91:8100" //bill表单


    //======文件图片======
    const val MEDIA_DELETE = "/project/Cellannex/fileDelete"
    const val MEDIA_UPLOAD = "/admin/Util/upload"

    //======google图层======
    const val GOOGLE_TILES = "/uploads/Tiles/%d/%d/%d.png"


    const val LOGIN = "/admin/Util/appLogin"
    //const val LOGIN = "/admin/Util/login"
    const val LOGOUT = "/admin/Util/logout"
    const val USER_DETAIL = "/admin/User/currentUserDetail"
    const val USER_MODIFY = "/admin/User/personal" //个人信息修改
    const val USER_AUTHORITY = "/admin/Message/appAuthorityIndex"
    const val BIM_INFO = "/modelmanage/Qualitymass/statisticsSection" //质量BIM

    //大坝管理
    const val DAM_MANAGER_LIST = "/project/Daba/dabaList"//大坝列表
    const val DAM_MANAGER_EDIT = "/project/Daba/editDaba"//大坝新增编辑
    const val DAM_MANAGER_INFO = "/project/Daba/getOne"//大坝信息
    const val DAM_MANAGER_JUDGE_NAME = "/project/Daba/judgeTitle"//判断大坝名称是否重复
    const val DAM_MANAGER_JUDGE_CODE = "/project/Daba/judgeNumber"//判断大坝编号是否重复
    const val DAM_MANAGER_LIST_COMMON = "/admin/Util/getDabaList"//其他位置大坝列表

    //单元工程
    const val DIVIDE = "/project/Division/nodeTree"  //6,筛选的到单元工程，5,到分部工程， 3，到单位工程
    const val ENGINEERING_TYPE = "/quality/Procedure/getEnType" //工程类型
    const val CELL_LIST = "/project/Division/listCell" //单元工程列表
    const val CELL_INFO = "/project/Division/getCellOne" //
    const val CELL_MANUAL = "/quality/Formthree/evaluate"//模式三的手动验评
    const val CELL_EDIT = "/project/Division/editCell"
    const val CELL_DELETE = "/project/Division/delCell"
    const val EXPERIMENT_DETECT = "/quality/Exam/getExamTree"
    const val EXPERIMENT_FILE = "/quality/Exam/reportList"
    const val SAVE_REPORT = "/project/Cellannex/saveReport" //关联检测报告
    const val CELL_MEDIA_UPLOAD = "/project/Cellannex/fileUpload"
    const val CELL_MEDIA_LIST = "/project/Cellannex/getFileList"
    const val FORM_REPORT = "/quality/Element/getCellFormApi"
    const val FORM_STATE = "/quality/Element/checkBox"
    const val FORM_APPROVAL_HISTORY = "/quality/Formone/approveHistory"
    const val FORM_APPROVAL = "/quality/Formoneapi/saveData"
    const val FORM_SCAN = "/quality/Qattachment/getFileList" //模式三扫描件
    const val FORM_INVALID = "/quality/Formtwo/cancel"
    const val FORM_INFO = "/quality/Formoneapi/fillForm"

    //单位工程
    const val UNIT_EVALUATE = "/quality/Unit/unitEvaluate"
    const val UNIT_CHANGE = "/quality/Unit/changeScore" //单位工程质量得分
    const val UNIT_IS_ACCIDENT = "/quality/Unit/changeAc"

    //分部工程
    const val PART_EVALUATE = "/quality/Segment/branchEvaluate"
    const val PART_IS_MAIN = "/quality/Segment/changePr"
    const val PART_IS_ACCIDENT = "/quality/Segment/changeAc"

    //组织架构
    const val ORGANIZATION_TREE = "/admin/Group/getGroupTree"
    const val ORGANIZATION_LIST = "/admin/Group/getGroupTable"

    //消息
    const val MESSAGE_LIST = "/admin/Message/message"
    const val MESSAGE_STATISTICS = "/admin/Message/getCount"

    //========================== 数字大坝 ===========================
    //试坑试验
    const val PIT_LIST = "/daba/pit/getList" //试坑实验列表
    const val PIT_DELETE = "/daba/pit/deleteOne"
    const val PIT_INFO = "/daba/pit/getOne"
    const val PIT_ADD_OR_EDIT = "/daba/pit/addOrEdit"
    const val PIT_CELL = "/daba/pit/getCell"
    const val PIT_MEMBER = "/daba/pit/getCateMember" //获取施工员、管理员
    const val PIT_IMAGE_LIST = "/daba/pit/getFileList" //试坑图片列表
    const val PIT_IMAGE_DELETE = "/daba/pit/deleteFile"
    const val PIT_IMAGE_ASSOCIATE = "/daba/pit/associate"
    const val PIT_REMARK_ADD_OR_EDIT = "/daba/pit/addOrEditRemark" //新增或编制图片备注
    const val PIT_REMARK_LIST = "/daba/pit/getRemark" //获取图片备注
    const val PIT_REMARK_DELETE = "/daba/pit/deleteRemark" //删除一条图片上的备注
    const val PIT_REMARK_DELETE_ALL = "/daba/pit/deleteAllRemark" //删除一个图片的全部备注

    //仓面管理
    const val SURFACE_LIST = "/daba/unit/getList" //获取仓面列表
    const val SURFACE_DELETE = "/daba/unit/deleteOne" //仓面删除
    const val SURFACE_MACHINE_LIST = "/daba/roller/getList"//碾压机信息列表
    const val SURFACE_MACHINE_WORKING = "/daba/device/getReadyRoller"//正在工作的碾压机
    const val SURFACE_INCREASE = "/daba/unit/addRoller" //增派
    const val SURFACE_CLOSE = "/daba/unit/endUnit" //收仓
    const val SURFACE_CLOSE_VERIFICATION = "/dict_dam/wareHouseReceipt"
    const val SURFACE_OPEN = "/daba/unit/startUnit" //开仓
    const val SURFACE_SERVER_TIME = "/dict_dam/serverTime" //服务器时间
    const val SURFACE_VERIFICATION = "/dict_dam/addRoller" //开仓、增派必发并验证通过方可开仓、增派
    const val SURFACE_INFO = "/daba/unit/getOne"//一条仓面信息


    //========================== 料源上坝 ===========================
    const val FENCE_TREE = "/liaoyuan/fence/getTree"
    const val FENCE_MAP = "/liaoyuan/fence/getFenceMaps"
    const val MONITOR_CAR_LIST = "/liaoyuan/Monitor/treeIndex"
    const val MONITOR_CAR_INFO = "/liaoyuan/Monitor/vehicleList"
    const val MONITOR_ROAD = "/liaoyuan/Monitor/getRoadTraffic"
    const val MONITOR_TRACK = "/liaoyuan/Monitor/playBack"
    const val ALARM_LIST = "/liaoyuan/Monitor/alarmList" //实时报警信息
    const val STATISTIC_FIELD = "/liaoyuan/damRecord/getAllStockGroundRecord"//用料统计
    const val STATISTIC_ALARM = "/liaoyuan/analysisWarning/getRecord" //报警统计
    const val ZONE_TYPE = "/liaoyuan/damRecord/getZoneList" //上坝分区type
    const val ZONE_STATISTIC = "/liaoyuan/damRecord/getRecord" //分区统计
    const val MATERIAL_TYPE = "/liaoyuan/damRecord/getStockGroundList"//上坝料场type


    //========================== 巡检 ===========================
    const val PATROL_LIST = "/patrol/patrol/listPatrol"
    const val PATROL_SAVE = "/patrol/patrol/savePatrol"
    const val PATROL_DELETE = "/patrol/patrol/delPatrol"
    const val PATROL_ADD_EMPTY = "/patrol/patrol/addEmptyPatrol"
    const val PATROL_INFO = "/patrol/patrol/detailPatrol" //巡检记录详情/问题整改详情-基本信息和流程信息
    const val PATROL_HISTORY = "/patrol/patrol/listHistory" //巡检记录和问题整改的处理历史
    const val PATROL_RECEIVER = "/patrol/patrol/getPatrolDealInfo" //处理信息处理人和下一步接收人信息

    const val PROBLEM_SAVE = "/patrol/problem/saveproblem"
    const val PROBLEM_INFO = "/patrol/problem/detailProblem"
    const val PROBLEM_LIST = "/patrol/problem/listProblem"
    const val PROBLEM_DELETE = "/patrol/problem/delProblem"

    const val RECTIFY_SAVE = "/patrol/Rectify/saveAllRectify"
    const val RECTIFY_LIST = "/patrol/Rectify/listRectify"
    const val RECTIFY_RECEIVER = "/patrol/rectify/getDealInfo"//当前处理人、下一个审批人信息

    const val SECTION_LIST = "/project/Section/listSection" //巡检部位列表
    const val LIB_LIST = "/patrol/lib/listLib"

}
