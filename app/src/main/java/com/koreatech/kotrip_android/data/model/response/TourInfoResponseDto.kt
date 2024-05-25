package com.koreatech.kotrip_android.data.model.response

import com.google.gson.annotations.SerializedName

data class TourBaseInfoResponseDto(
    @SerializedName("page")
    val page: Int,
    @SerializedName("list")
    val list: List<TourInfoResponseDto>
)

data class TourInfoResponseDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("addr1")
    val addr1: String,
    @SerializedName("mapX")
    val mapX: Double,
    @SerializedName("mapY")
    val mapY: Double,
)
