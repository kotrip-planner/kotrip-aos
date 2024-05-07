package com.koreatech.kotrip_android.presentation.views.history

import com.koreatech.kotrip_android.data.model.response.OptimalRouteResponseDto

sealed interface HistorySideEffect {
    data class GetHistoryTours(
        val routes : OptimalRouteResponseDto
    ): HistorySideEffect
    data object MoveToOptimalStep: HistorySideEffect
    data class Toast(
        val message: String
    ): HistorySideEffect
}