package com.koreatech.kotrip_android.presentation.views.home

import com.koreatech.kotrip_android.data.model.response.OptimalRouteResponseDto
import com.koreatech.kotrip_android.data.model.response.OptimalScheduleResponseDto

sealed class HomeSideEffect {
    data class MoveToTourStep(
        val day: Int,
        val isOneDay: Boolean
    ): HomeSideEffect()
    data class Toast(
        val message: String
    ): HomeSideEffect()
    data class GenerateOptimal(
        val uuid: String
    ): HomeSideEffect()
    data class GetOptimalData(
        val routes: OptimalRouteResponseDto
    ): HomeSideEffect()
    data object MoveToOptimalPage: HomeSideEffect()
}