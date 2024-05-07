package com.koreatech.kotrip_android.presentation.views.trip

import com.koreatech.kotrip_android.model.trip.CityInfo
import com.koreatech.kotrip_android.model.trip.TourDate

sealed class TripSideEffect {
    data class CompletedTrip(
        val isOneDay: Boolean,
        val cityInfo: CityInfo
    ) : TripSideEffect()
    data class CompletedSchedule(
        val isOneDay: Boolean,
        val tourDate: TourDate,
        val cityInfo: CityInfo?
    ): TripSideEffect()
}