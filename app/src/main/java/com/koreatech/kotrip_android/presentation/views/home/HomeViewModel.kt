package com.koreatech.kotrip_android.presentation.views.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koreatech.kotrip_android.Constants.BEARER_PREFIX
import com.koreatech.kotrip_android.api.KotripApi
import com.koreatech.kotrip_android.api.KotripAuthApi
import com.koreatech.kotrip_android.api.ScheduleRequest
import com.koreatech.kotrip_android.data.DataStoreImpl
import com.koreatech.kotrip_android.data.mapper.toTourInfoList
import com.koreatech.kotrip_android.data.model.request.home.GenerateScheduleRequestDto
import com.koreatech.kotrip_android.data.model.request.home.KotripRequestDto
import com.koreatech.kotrip_android.model.home.TourInfo
import com.koreatech.kotrip_android.model.trip.CityInfo
import com.koreatech.kotrip_android.model.trip.TourDate
import com.koreatech.kotrip_android.presentation.common.UiState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class HomeViewModel(
    private val kotripApi: KotripApi,
    private val kotripAuthApi: KotripAuthApi,
    private val dataStoreImpl: DataStoreImpl,
) : ContainerHost<HomeState, HomeSideEffect>, ViewModel() {
    override val container: Container<HomeState, HomeSideEffect> = container(HomeState())

    var cityInfo: CityInfo? = null
    var tourDate: TourDate? = null
    var startPositionChecked: Boolean = false

    var homeTourList: List<MutableList<TourInfo>> = emptyList()
    var homeOneDayStartTourInfo: TourInfo? = null
    var homeOneDayTourList: MutableList<TourInfo> = mutableListOf()


    fun setData(cityInfo: CityInfo?, tourDate: TourDate) {
        this.cityInfo = cityInfo
        this.tourDate = tourDate
    }

    fun moveToTourStep(day: Int, isOneDay: Boolean) = intent {
        postSideEffect(HomeSideEffect.MoveToTourStep(day, isOneDay))
    }

    fun setHomeTourListSize(homeTourList: List<MutableList<TourInfo>>) {
        this.homeTourList = homeTourList
    }

    fun setHomeOneDayTourListSize(homeOneDayTourList: MutableList<TourInfo>) {
        this.homeOneDayTourList = homeOneDayTourList
    }

    fun addItemHomeTourList(position: Int, tourInfo: TourInfo) {
        homeTourList[position].add(tourInfo)
    }

    fun addItemHomeOneDayTourList(tourInfo: TourInfo) {
        homeOneDayTourList.add(tourInfo)
    }

    fun showToast(message: String) = intent {
        postSideEffect(HomeSideEffect.Toast(message))
    }


    fun getTour() = intent {
        viewModelScope.launch {
            val tours = kotripApi.getTour(cityInfo?.areaId ?: 0).toTourInfoList().toMutableList()
            reduce { state.copy(tours = tours) }
            reduce { state.copy(status = UiState.Success) }
        }
    }

    fun moveToOptimalPage() = intent {
        postSideEffect(HomeSideEffect.MoveToOptimalPage)
    }

    fun setOptimalRoute(
        areaId: Int,
        optimalRoutes: List<KotripRequestDto>,
    ) = intent {
        viewModelScope.launch {
            reduce { state.copy(status = UiState.Loading) }
            val response =
                kotripAuthApi.postOptimalRoute(
                    "$BEARER_PREFIX ${
                        dataStoreImpl.getAccessToken().first().toString()
                    }", GenerateScheduleRequestDto(areaId, optimalRoutes)
                )
            when (response.code) {
                200 -> postSideEffect(HomeSideEffect.GenerateOptimal(response.data.uuid))
                else -> postSideEffect(HomeSideEffect.Toast(response.message))
            }
        }
    }

    fun getOptimalRoute(
        uuid: String,
    ) = intent {
        viewModelScope.launch {
            val optimalRoutes = kotripAuthApi.getSchedule(
                "$BEARER_PREFIX ${
                    dataStoreImpl.getAccessToken().first().toString()
                }", ScheduleRequest(uuid)
            )
            reduce { state.copy(status = UiState.Success) }
            when (optimalRoutes.code) {
                200 -> postSideEffect(HomeSideEffect.GetOptimalData(optimalRoutes.data))
                else -> postSideEffect(HomeSideEffect.Toast(optimalRoutes.message))
            }
        }
    }
}