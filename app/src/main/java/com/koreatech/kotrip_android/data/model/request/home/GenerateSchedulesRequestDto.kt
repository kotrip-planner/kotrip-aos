package com.koreatech.kotrip_android.data.model.request.home

import com.google.gson.annotations.SerializedName

data class DayGenerateScheduleRequestDto(
    @SerializedName("title")
    val title: String,
    @SerializedName("areaId")
    val areaId: Int,
    @SerializedName("kotrip")
    val kotrip: List<DayKotripRequestDto>
)

data class DayKotripRequestDto(
    @SerializedName("date")
    val date: String,
    @SerializedName("nodes")
    val nodes: List<DayNodeRequestDto>
)


data class DayNodeRequestDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("time")
    val time: Int
)
