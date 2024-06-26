package com.koreatech.kotrip_android.presentation.views.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.koreatech.kotrip_android.Constants.BEARER_PREFIX
import com.koreatech.kotrip_android.api.KotripApi
import com.koreatech.kotrip_android.api.KotripAuthApi
import com.koreatech.kotrip_android.api.ScheduleRequest
import com.koreatech.kotrip_android.data.DataStoreImpl
import com.koreatech.kotrip_android.data.mapper.toTourInfo
import com.koreatech.kotrip_android.data.model.request.home.DayGenerateScheduleRequestDto
import com.koreatech.kotrip_android.data.model.request.home.DayKotripRequestDto
import com.koreatech.kotrip_android.data.model.request.home.GenerateScheduleRequestDto
import com.koreatech.kotrip_android.data.model.request.home.KotripRequestDto
import com.koreatech.kotrip_android.data.repository.TourRepositoryImpl
import com.koreatech.kotrip_android.model.home.TourInfo
import com.koreatech.kotrip_android.model.trip.CityInfo
import com.koreatech.kotrip_android.model.trip.TourDate
import com.koreatech.kotrip_android.presentation.common.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    private val tourRepositoryImpl: TourRepositoryImpl,
) : ContainerHost<HomeState, HomeSideEffect>, ViewModel() {
    override val container: Container<HomeState, HomeSideEffect> = container(HomeState())

    var cityInfo: CityInfo? = null
    var tourDate: TourDate? = null
    var startPositionChecked: Boolean = false

    var homeOneDayStartTourInfo: TourInfo? = null
    var homeOneDayTourList: MutableList<TourInfo> = mutableListOf()

    private val _page = MutableStateFlow(0)
    val page = _page.asStateFlow()

    private val _homeTours = MutableStateFlow(listOf(mutableListOf<TourInfo>()))
    val homeTours = _homeTours.asStateFlow()

    private val _oneDayHomeTours = MutableStateFlow(mutableListOf<TourInfo>())
    val oneDayHomeTours = _oneDayHomeTours.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _tours = MutableStateFlow(mutableListOf<TourInfo>())
    val tours = searchText
        .combine(_tours) { text, tours ->
            if (text.isBlank()) {
                tours
            } else {
                tours.filter {
                    it.matchQuery(text)
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _tours.value
        )


    private val _testTours: MutableStateFlow<PagingData<TourInfo>> =
        MutableStateFlow(PagingData.empty())

    fun getTestTours(): Flow<PagingData<TourInfo>> {
        val toursFlow = tourRepositoryImpl.tours(cityInfo?.areaId ?: 0, 20).cachedIn(viewModelScope)

        return searchText
            .combine(toursFlow) { text, tours ->
                if (text.isBlank()) {
                    tours
                } else {
                    tours.filter { it.matchQuery(text) }
                }
            }
    }
    fun clearSearchText() {
        _searchText.value = ""
    }


    private val _selectedTours = MutableStateFlow(mutableListOf<TourInfo>())
    val selectedTours = _selectedTours.asStateFlow()

    fun clear() {
        homeOneDayStartTourInfo = null
        homeOneDayTourList.clear()

        _homeTours.value = emptyList()
        _oneDayHomeTours.value = mutableListOf()
        _searchText.value = ""
        _tours.value = mutableListOf()
        _selectedTours.value = mutableListOf()
        _page.value = 0
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun updateToursSelection(position: Int, tourInfo: TourInfo) {
        val updateTours = _tours.value
        updateTours[position] = tourInfo
        _tours.value = updateTours
    }

    fun onSelectedTours(tourInfo: TourInfo) {
        val selectedTours = _selectedTours.value
        selectedTours.add(tourInfo)
        _selectedTours.value = selectedTours
    }

    fun onRemoveTours(tourInfo: TourInfo) {
        val selectedTours = _selectedTours.value
        selectedTours.remove(tourInfo)
        _selectedTours.value = selectedTours
    }

    fun setData(cityInfo: CityInfo?, tourDate: TourDate) {
        this.cityInfo = cityInfo
        this.tourDate = tourDate
    }

    fun moveToTourStep(day: Int, isOneDay: Boolean) = intent {
        postSideEffect(HomeSideEffect.MoveToTourStep(day, isOneDay))
    }

    fun setHomeTourListSize(homeTourList: List<MutableList<TourInfo>>) {
//        this.homeTourList = homeTourList
        _homeTours.value = homeTourList
    }

    fun setHomeOneDayTourListSize(homeOneDayTourList: MutableList<TourInfo>) {
        this.homeOneDayTourList = homeOneDayTourList
    }

    fun addItemHomeTourList(position: Int, tourInfo: TourInfo) {
        val updatedTours = _homeTours.value
        updatedTours[position].add(tourInfo)
        _homeTours.value = updatedTours
    }

    fun addOneDayItemHomeTourList(tourInfo: TourInfo) {
        val updatedTours = _oneDayHomeTours.value
        updatedTours.add(tourInfo)
        _oneDayHomeTours.value = updatedTours
    }

    fun removeItemHomeTourList(position: Int, tourInfo: TourInfo) {
        val updatedTours = _homeTours.value
        updatedTours.forEach { it.removeAll { element -> element == tourInfo } }
        _homeTours.value = updatedTours
    }

    fun removeOneDayItemHomeTourList(tourInfo: TourInfo) {
        val updatedTours = _oneDayHomeTours.value
        updatedTours.removeAll { element -> element == tourInfo }
        _oneDayHomeTours.value = updatedTours
    }


    fun addItemHomeOneDayTourList(tourInfo: TourInfo) {
        homeOneDayTourList.add(tourInfo)
    }

    fun showToast(message: String) = intent {
        postSideEffect(HomeSideEffect.Toast(message))
    }


    fun moveToOptimalPage() = intent {
        postSideEffect(HomeSideEffect.MoveToOptimalPage)
    }

    fun setOptimalRoute(
        title: String,
        areaId: Int,
        optimalRoutes: List<KotripRequestDto>,
    ) = intent {
        viewModelScope.launch {
            reduce { state.copy(status = UiState.Loading) }
            try {
                val response =
                    kotripAuthApi.postOptimalRoutes(
                        "$BEARER_PREFIX ${
                            dataStoreImpl.getAccessToken().first().toString()
                        }", GenerateScheduleRequestDto(title, areaId, optimalRoutes)
                    )
                when (response.code) {
                    200 -> postSideEffect(HomeSideEffect.GenerateOptimal(response.data.uuid))
                    else -> postSideEffect(HomeSideEffect.Toast(response.message))
                }
            } catch (e: Exception) {

            }
        }
    }

    fun setOptimalRouteDay(
        title: String,
        areaId: Int,
        optimalRoutes: List<DayKotripRequestDto>,
    ) = intent {
        viewModelScope.launch {
            reduce { state.copy(status = UiState.Loading) }
            try {
                val response =
                    kotripAuthApi.postOptimalRouteDay(
                        "$BEARER_PREFIX ${
                            dataStoreImpl.getAccessToken().first().toString()
                        }", DayGenerateScheduleRequestDto(title, areaId, optimalRoutes)
                    )
                when (response.code) {
                    200 -> postSideEffect(HomeSideEffect.GenerateOptimal(response.data.uuid))
                    else -> postSideEffect(HomeSideEffect.Toast(response.message))
                }
            } catch (e: Exception) {
                e.message
            }
        }
    }

    fun getOptimalRoute(
        uuid: String,
    ) = intent {
        viewModelScope.launch {
            try {
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
            } catch (e: Exception) {
                e.message
            }
        }
    }
}