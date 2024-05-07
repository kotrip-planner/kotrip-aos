package com.koreatech.kotrip_android.data.model.request

import com.google.gson.annotations.SerializedName

data class RefreshRequestDto(
    @SerializedName("refreshToken")
    val refreshToken: String
)
