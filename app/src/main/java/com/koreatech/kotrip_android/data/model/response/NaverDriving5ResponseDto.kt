package com.koreatech.kotrip_android.data.model.response

import com.google.gson.annotations.SerializedName

data class NaverDriving5ResponseDto(
    @SerializedName("route")
    val route: RouteResponseDto
)

data class RouteResponseDto(
    @SerializedName("traoptimal")
    val traoptimal: List<TraoptimalResponseDto>,
)

data class TraoptimalResponseDto(
    @SerializedName("summary")
    val summary: SummaryResponseDto,
    @SerializedName("path")
    val path: List<List<Double>>
)


data class SummaryResponseDto(
    @SerializedName("start")
    val start: LocationResponseDto,
    @SerializedName("goal")
    val goal: LocationResponseDto
)

data class LocationResponseDto(
    @SerializedName("location")
    val location: List<Double>,
    @SerializedName("dir")
    val dir: Int
)


