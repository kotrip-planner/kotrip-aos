package com.koreatech.kotrip_android.data.model.response

import com.google.gson.annotations.SerializedName

data class HistoryResponseDto(
    @SerializedName("history")
    val history: List<HistoryDataResponseDto>
)

data class HistoryDataResponseDto(
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("endDate")
    val endDate: String
)