package com.koreatech.kotrip_android.presentation.views.trip

import com.koreatech.kotrip_android.model.trip.CityInfo
import com.koreatech.kotrip_android.presentation.common.UiState

data class TripState(
    val status: UiState = UiState.Loading,
    val cities: List<CityInfo> = emptyList()
)