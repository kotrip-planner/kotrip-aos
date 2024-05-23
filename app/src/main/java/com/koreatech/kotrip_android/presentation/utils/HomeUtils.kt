package com.koreatech.kotrip_android.presentation.utils

import com.koreatech.kotrip_android.data.model.request.home.DayKotripRequestDto
import com.koreatech.kotrip_android.data.model.request.home.DayNodeRequestDto
import com.koreatech.kotrip_android.data.model.request.home.KotripRequestDto
import com.koreatech.kotrip_android.data.model.request.home.NodeRequestDto
import com.koreatech.kotrip_android.data.model.request.home.GenerateScheduleRequestDto
import com.koreatech.kotrip_android.model.home.TourInfo
import java.time.LocalDate


fun List<TourInfo?>.toKotripRequestDto(date: String, position: Int) =
    KotripRequestDto(
        date = date,
        nodes = this.map { it.toNodeRequestDto(position) }
    )


fun TourInfo?.toNodeRequestDto(position: Int) =
    NodeRequestDto(
        id = this?.id ?: 0,
        name = this?.title ?: "",
        latitude = this?.latitude ?: 0.0,
        longitude = this?.longitude ?: 0.0,
        time = position
    )

fun List<TourInfo?>.toDayKotripRequestDto(date: String, position: Int) =
    DayKotripRequestDto(
        date = date,
        nodes = this.map { it.toDayNodeRequestDto(position) }
    )

fun TourInfo?.toDayNodeRequestDto(position: Int) =
    DayNodeRequestDto(
        id = this?.id ?: 0,
        name = this?.title ?: "",
        latitude = this?.latitude ?: 0.0,
        longitude = this?.longitude ?: 0.0,
        imageUrl = this?.imageUrl ?: "",
        time = position
    )

fun getOptimalRouteRequestDto(
    dates: List<LocalDate?>?,
    tours: List<List<TourInfo?>>,
): List<KotripRequestDto> {
    val newList = mutableListOf<KotripRequestDto>()
    repeat(dates?.size ?: 0) {
        val date = dates?.get(it)?.toString() ?: ""
        newList.add(
            tours[it].toKotripRequestDto(date, it + 1)
        )
    }
    return newList
}

fun getOptimalDayRouteRequestDto(
    dates: LocalDate?,
    tours: List<TourInfo?>,
): List<DayKotripRequestDto> {
    val newList = mutableListOf<DayKotripRequestDto>()
    val date = dates?.toString() ?: ""
    newList.add(
        tours.toDayKotripRequestDto(date, 1)
    )
    return newList
}