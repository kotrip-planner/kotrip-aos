package com.koreatech.kotrip_android.data.model.response

import com.google.gson.annotations.SerializedName

data class CityInfoResponseDto(
    @SerializedName("areaId")
    val areaId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("mapX")
    val mapX: Double,
    @SerializedName("mapY")
    val mapY: Double,
)
