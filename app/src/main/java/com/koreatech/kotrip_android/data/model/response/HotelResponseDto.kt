package com.koreatech.kotrip_android.data.model.response

import com.google.gson.annotations.SerializedName

data class HotelBasicResponseDto(
    @SerializedName("flag")
    val flag: String,
    @SerializedName("hotelSearchList")
    val hotelSearchList: List<HotelResponseDto>
)
data class HotelResponseDto(
    @SerializedName("addr1")
    val addr1: String,
    @SerializedName("addr2")
    val addr2: String,
    @SerializedName("imageUrl1")
    val imageUrl1: String,
    @SerializedName("imageUrl2")
    val imageUrl2: String,
    @SerializedName("mapX")
    val longitude: Double,
    @SerializedName("mapY")
    val latitude: Double,
    @SerializedName("tel")
    val tel: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("distance")
    val distance: Int,
)
