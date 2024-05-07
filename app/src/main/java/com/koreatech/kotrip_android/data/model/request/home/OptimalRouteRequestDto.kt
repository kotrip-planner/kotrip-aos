package com.koreatech.kotrip_android.data.model.request.home

import com.google.gson.annotations.SerializedName

data class OptimalRouteRequestDto(
    @SerializedName("areaId")
    val areaId: Int
)
