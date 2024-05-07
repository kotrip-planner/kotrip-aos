package com.koreatech.kotrip_android.data.model.response

import com.google.gson.annotations.SerializedName

data class AuthResponseDto(
    @SerializedName("message")
    val message: String,
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
)
