package com.koreatech.kotrip_android.presentation.views.hotel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koreatech.kotrip_android.Constants
import com.koreatech.kotrip_android.api.KotripAuthApi
import com.koreatech.kotrip_android.data.DataStoreImpl
import com.koreatech.kotrip_android.data.model.response.HotelResponseDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

class HotelViewModel(
    private val kotripAuthApi: KotripAuthApi,
    private val dataStoreImpl: DataStoreImpl,
) : ContainerHost<HotelState, HotelSideEffect>, ViewModel() {
    override val container: Container<HotelState, HotelSideEffect> = container(HotelState())

    private val _hotels = MutableStateFlow<MutableList<HotelResponseDto>>(mutableListOf())
    val hotels = _hotels.asStateFlow()

    suspend fun getHotel(x: Double, y: Double) {
        viewModelScope.launch {
            runCatching {
                kotripAuthApi.getHotel(
                    "${Constants.BEARER_PREFIX} ${
                        dataStoreImpl.getAccessToken().first().toString()
                    }", x, y
                )
            }.onSuccess {
                val updatedHotels = it.data
                _hotels.value = updatedHotels.toMutableList()
            }.onFailure {
                it.message
            }
        }
    }
}