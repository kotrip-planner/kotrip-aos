package com.koreatech.kotrip_android.presentation.views.optimal

sealed interface OptimalSideEffect {
    data class Toast(
        val message: String
    ): OptimalSideEffect
}