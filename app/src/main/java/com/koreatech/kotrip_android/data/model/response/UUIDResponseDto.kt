package com.koreatech.kotrip_android.data.model.response

import com.google.gson.annotations.SerializedName

data class UUIDResponseDto(
    @SerializedName("uuid")
    val uuid: String
)