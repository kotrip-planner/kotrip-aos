package com.koreatech.kotrip_android.presentation.views.entry

sealed class EntrySideEffect {
    data object MoveToHistoryPage : EntrySideEffect()
    data class MoveToTripPage(
        val isOneDay: Boolean,
    ) : EntrySideEffect()
}