package com.zgdj.project

import android.os.Parcel
import android.os.Parcelable

data class DeviceBean(
        val title: String,
        val code: String,
        val factory: String
)

data class StockManagerInfoBean(
        val title: String,
        val code: String,
        val unit: String,
        val secure_sum: Int,
        val exit_sum: Int
)

data class InspectionBean(
        val title: String,
        val time: String,
        val status: String
)

data class InspectionEditBean(
        val title: String,
        val isNormal: Boolean,
        var isPunch: Boolean
)

data class WorkTicketBean(
        val title: String,
        val time: String,
        val status: String,
        val code: String,
        val principal: String
)

data class OperateTicketBean(
        val title: String,
        val status: String,
        val time: String,
        val site_name: String,
        val machine_name: String,
        val code: String
)


data class DataDetectBean(
        val title: String,
        val data: List<DetectInfoBean>
)

data class DetectInfoBean(
        val name: String,
        val value: Float,
        val unit: String
)

data class FaultWarningBean(
        val time: String,
        val name: String,
        val status: String,
        val group: String,
        val remark: String,
        val value: String,
        val type: String,
        val alarm: String,
        val isDeal: Boolean
)

data class InvertoryWarningBean(
        val type: String,
        val code: Int,
        val name: String,
        val model: String,
        val unit: String,
        val all_sum: Int,
        val exist_sum: Int,
        val principal: String
)

data class KnowledgeBaseBean(
        val name: String,
        val level: Int,
        val children: List<KnowledgeBaseBean>?) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readInt(),
            parcel.createTypedArrayList(CREATOR)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(level)
        parcel.writeTypedList(children)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<KnowledgeBaseBean> {
        override fun createFromParcel(parcel: Parcel): KnowledgeBaseBean {
            return KnowledgeBaseBean(parcel)
        }

        override fun newArray(size: Int): Array<KnowledgeBaseBean?> {
            return arrayOfNulls(size)
        }
    }
}

data class KnowledgeBaseInfoBean(
        val title: String,
        val time: String,
        val create: String
)

data class SchedulingDecisionBean(
        val message: String,
        val work: String,
        val condition: List<String>
)

data class WeatherBean(
        val weather: String,
        val temperature: String,
        val time: String
)

data class MessageBean(
        val title: String,
        val time: String,
        val status: Int,
        val from: String
)