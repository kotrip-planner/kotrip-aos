package com.koreatech.kotrip_android.presentation.views.history

import com.koreatech.kotrip_android.data.model.response.HistoryDataResponseDto
import com.koreatech.kotrip_android.data.model.response.HistoryResponseDto
import com.koreatech.kotrip_android.presentation.common.UiState

data class HistoryState(
    val state: UiState = UiState.Loading,
    val histories: List<HistoryDataResponseDto> = emptyList()
)
