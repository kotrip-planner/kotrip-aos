package com.koreatech.kotrip_android.presentation.views.optimal

import com.koreatech.kotrip_android.data.model.response.NaverDriving5ResponseDto
import com.koreatech.kotrip_android.presentation.common.UiState
import com.naver.maps.geometry.LatLng

data class OptimalState (
    val status: UiState = UiState.Loading,
    val paths: MutableList<MutableList<LatLng>> = mutableListOf()
)