package com.koreatech.kotrip_android.model

data class Schedule(
    val time: String,
    val scheduleItems: List<ScheduleItem>
)

data class ScheduleItem(
    val title: String,
    val imageUrl: String
)
