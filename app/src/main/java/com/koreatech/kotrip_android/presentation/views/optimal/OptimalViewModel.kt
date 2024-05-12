package com.koreatech.kotrip_android.presentation.views.optimal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koreatech.kotrip_android.api.KotripNaverApi
import com.koreatech.kotrip_android.data.model.response.HotelResponseDto
import com.koreatech.kotrip_android.data.model.response.OptimalRouteResponseDto
import com.koreatech.kotrip_android.data.model.response.OptimalScheduleResponseDto
import com.koreatech.kotrip_android.presentation.common.UiState
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

class OptimalViewModel(
    private val kotripNaverApi: KotripNaverApi,
) : ContainerHost<OptimalState, OptimalSideEffect>, ViewModel() {
    override val container: Container<OptimalState, OptimalSideEffect> = container(OptimalState())

    val naverPaths: MutableList<MutableList<LatLng>> = mutableListOf()
    val optimalTours = mutableListOf<OptimalScheduleResponseDto>()
    var city: String = ""

    private val _hotels = MutableStateFlow<List<HotelResponseDto?>>(emptyList())
    val hotels = _hotels.asStateFlow()


    fun initHotels(size: Int) {
        if (!_hotels.value.contains(null)) {
            _hotels.value = List<HotelResponseDto?>(size) { null }
        }
    }

    fun setHotels(hotelResponseDto: HotelResponseDto, position: Int) {
        val updatedHotels = _hotels.value.toMutableList()
        updatedHotels[position] = hotelResponseDto
        _hotels.value = updatedHotels
    }

    fun setOptimalTours(tours: OptimalRouteResponseDto) {
        optimalTours.addAll(tours.schedule)
        city = tours.city
    }

    fun getNaverDriving5(
        clientId: String,
        clientSecret: String,
        initLocation: List<NaverLocation>,
    ) =
        intent {
            reduce { state.copy(status = UiState.Loading) }
            viewModelScope.launch {
                initLocation.forEachIndexed { index, naverLocation ->
                    val newList = mutableListOf<LatLng>()
                    if (naverLocation.start.isNotEmpty()) {
                        runCatching {
                            kotripNaverApi.getDriving5(
                                clientId,
                                clientSecret,
                                naverLocation.start,
                                naverLocation.goal,
                                naverLocation.waypoints
                            )
                        }.onSuccess {
                            try {
                                it.route.traoptimal.forEach {
                                    newList.add(it.summary.start.location.toLatLng())
                                    it.path.map { it.toLatLng() }.forEach { item ->
                                        newList.add(item)
                                    }
                                    newList.add(it.summary.goal.location.toLatLng())
                                }
                            } catch (e: Exception) {
                                e.message
                            }
                            reduce { state.copy(status = UiState.Success) }
                        }.onFailure {
                            postSideEffect(OptimalSideEffect.Toast(it.message ?: ""))
                        }
                        naverPaths.add(newList)
                    }
                }
                reduce {
                    state.copy(
                        paths = naverPaths
                    )
                }
            }
        }

}

fun List<Double>.toLatLng(): LatLng = LatLng(
    this[1], this[0]
)

data class NaverLocation(
    val start: String,
    val goal: String,
    val waypoints: String,
)