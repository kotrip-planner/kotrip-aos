package com.koreatech.kotrip_android.data.model.response

import com.google.gson.annotations.SerializedName

data class OptimalRouteResponseDto(
    @SerializedName("city")
    val city: String,
    @SerializedName("schedule")
    val schedule: List<OptimalScheduleResponseDto>,
)

data class OptimalScheduleResponseDto(
    @SerializedName("date")
    val date: String,
    @SerializedName("tours")
    val tours: List<OptimalToursResponseDto>,
)

data class OptimalToursResponseDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("mapY")
    val latitude: Double,
    @SerializedName("mapX")
    val longitude: Double,
)