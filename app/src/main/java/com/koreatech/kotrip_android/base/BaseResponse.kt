package com.koreatech.kotrip_android.base

import com.google.gson.annotations.SerializedName


data class BaseResponse <T> (
    @SerializedName("code")
    val code: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: T
)
