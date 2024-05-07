package com.koreatech.kotrip_android.model.trip

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

@Parcelize
data class TourDate(
    val start: LocalDate?,
    val end: LocalDate?,
    val total: Int = Period.between(start, end).days + 1
) : Parcelable {
    fun onPrintDateRange(): List<LocalDate?> {
        return if (end == null) {
            emptyList<LocalDate>()
        } else {
            val dates = mutableListOf<LocalDate?>()
            dates.add(start)

            var date = start
            while (date?.isBefore(end) == true) {
                date = date.plusDays(1)
                dates.add(date)
            }
            dates
        }
    }
}
