package com.koreatech.kotrip_android.presentation.views.hotel

import com.koreatech.kotrip_android.data.model.response.HotelResponseDto
import com.koreatech.kotrip_android.presentation.common.UiState

data class HotelState(
    val status: UiState = UiState.Loading,
)