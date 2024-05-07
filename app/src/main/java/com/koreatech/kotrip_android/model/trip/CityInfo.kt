package com.koreatech.kotrip_android.model.trip

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CityInfo(
    val areaId: Int = 0,
    val title: String = "",
    val imageUrl: String = "",
    val mapX: Double = 0.0,
    val mapY: Double = 0.0,
): Parcelable