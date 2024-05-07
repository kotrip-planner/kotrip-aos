package com.koreatech.kotrip_android.presentation.views.home

import com.koreatech.kotrip_android.model.home.TourInfo
import com.koreatech.kotrip_android.presentation.common.UiState

data class HomeState(
    val status: UiState = UiState.Idle,
    val tours: List<TourInfo> = emptyList(),
    val initData: Boolean = false,
    val optimalState: Boolean = false
)