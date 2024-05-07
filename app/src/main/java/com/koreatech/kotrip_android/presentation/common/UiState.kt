package com.koreatech.kotrip_android.presentation.common

sealed class UiState {
    object Idle: UiState()
    object Loading: UiState()
    object Success: UiState()
    object Failed: UiState()
}