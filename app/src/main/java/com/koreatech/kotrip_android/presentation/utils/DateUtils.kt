package com.koreatech.kotrip_android.presentation.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun String.inRangeList(endDate: String): List<LocalDate> {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val startDateToLocalDate = LocalDate.parse(this, formatter)
    val endDateToLocalDate = LocalDate.parse(endDate, formatter)

    val datesInRange = generateSequence(startDateToLocalDate) { it.plusDays(1) }
        .takeWhile { !it.isAfter(endDateToLocalDate) }
        .toList()

    return datesInRange
}