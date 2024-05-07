package com.koreatech.kotrip_android.data.model.request.home

import com.google.gson.annotations.SerializedName

data class GenerateScheduleRequestDto(
    @SerializedName("areaId")
    val areaId: Int,
    @SerializedName("kotrip")
    val kotrip: List<KotripRequestDto>
)

data class KotripRequestDto(
    @SerializedName("date")
    val date: String,
    @SerializedName("nodes")
    val nodes: List<NodeRequestDto>
)

data class NodeRequestDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("time")
    val time: Int
)
