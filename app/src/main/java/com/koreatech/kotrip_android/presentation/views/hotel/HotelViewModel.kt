package com.koreatech.kotrip_android.presentation.views.hotel

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koreatech.kotrip_android.Constants
import com.koreatech.kotrip_android.api.KotripAuthApi
import com.koreatech.kotrip_android.data.DataStoreImpl
import com.koreatech.kotrip_android.data.model.response.HotelResponseDto
import com.koreatech.kotrip_android.presentation.CustomMarker
import com.koreatech.kotrip_android.presentation.common.UiState
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

class HotelViewModel(
    private val kotripAuthApi: KotripAuthApi,
    private val dataStoreImpl: DataStoreImpl,
) : ContainerHost<HotelState, HotelSideEffect>, ViewModel() {
    override val container: Container<HotelState, HotelSideEffect> = container(HotelState())

    private val _hotels = MutableStateFlow<MutableList<HotelResponseDto>>(mutableListOf())
    val hotels = _hotels.asStateFlow()

    private val _hotelImages = MutableStateFlow<MutableList<Bitmap?>>(mutableListOf())
    val hotelImages = _hotelImages.asStateFlow()

    suspend fun getHotel(x: Double, y: Double, bx: Double, by: Double, context: Context) {
        intent {
            viewModelScope.launch {
                reduce { state.copy(status = UiState.Loading) }
                runCatching {
                    kotripAuthApi.getHotel(
                        token = "${Constants.BEARER_PREFIX} ${
                            dataStoreImpl.getAccessToken().first().toString()
                        }",
                        axLongitude = x,
                        ayLatitude = y,
                        bxLongitude = bx,
                        byLatitude = by
                    )
                }.onSuccess {
                    val updatedHotels = it.data
                    _hotels.value = updatedHotels.toMutableList()
                    val imageLoadJobs = it.data.map { hotelData ->
                        async { createCircleBitmapFromUrl(hotelData.imageUrl1, context) }
                    }
                    val loadedImages  = imageLoadJobs.awaitAll()
                    _hotelImages.value = loadedImages.toMutableList()
                    reduce { state.copy(status = UiState.Success) }
//                    it.data.map { createCircleBitmapFromUrl(it.imageUrl1, context) }.let {
//                        _hotelImages.value = it.toMutableList()
//                    }
                }.onFailure {
                    it.message
                }
            }
        }
    }
}