package com.koreatech.kotrip_android.presentation.views.trip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koreatech.kotrip_android.api.KotripApi
import com.koreatech.kotrip_android.data.mapper.toCityInfoList
import com.koreatech.kotrip_android.model.trip.CityInfo
import com.koreatech.kotrip_android.model.trip.TourDate
import com.koreatech.kotrip_android.presentation.common.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class TripViewModel(
    private val kotripApi: KotripApi,
) : ContainerHost<TripState, TripSideEffect>, ViewModel() {
    override val container: Container<TripState, TripSideEffect> = container(TripState())

    fun moveToScheduleStep(isOneDay: Boolean, cityInfo: CityInfo) {
        intent {
            reduce { state.copy(status = UiState.Success) }
            postSideEffect(TripSideEffect.CompletedTrip(isOneDay, cityInfo))
        }
    }

    fun moveToHomeStep(isOneDay: Boolean, tourDate: TourDate, cityInfo: CityInfo?) {
        intent {
            postSideEffect(TripSideEffect.CompletedSchedule(isOneDay, tourDate, cityInfo))
        }
    }

    fun getCity() = intent {
        withContext(Dispatchers.Default) {
            val cityInfoList = kotripApi.getCity().toCityInfoList()
            delay(2000)
            reduce { state.copy(cities = cityInfoList) }
            reduce { state.copy(status = UiState.Success) }
        }
    }
}