package com.koreatech.kotrip_android.data.model.request

import com.google.gson.annotations.SerializedName

data class LoginRequestDto (
    @SerializedName("code")
    val code: String
)
