package com.koreatech.kotrip_android.presentation.views.home

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.koreatech.kotrip_android.R
import com.koreatech.kotrip_android.model.home.TourInfo
import com.koreatech.kotrip_android.model.trip.CityInfo
import com.koreatech.kotrip_android.presentation.common.UiState
import com.koreatech.kotrip_android.presentation.components.organisms.KotripInputStartArea
import com.koreatech.kotrip_android.presentation.components.organisms.KotripOptimalDialog
import com.koreatech.kotrip_android.presentation.components.organisms.KotripScheduleItem
import com.koreatech.kotrip_android.presentation.components.organisms.KotripTripItem
import com.koreatech.kotrip_android.presentation.components.parts.KotripScheduleEmpty
import com.koreatech.kotrip_android.presentation.components.parts.KotripTopBar
import com.koreatech.kotrip_android.presentation.utils.BackHandler
import com.koreatech.kotrip_android.presentation.utils.showToast
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalNaverMapApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    context: Context,
    focusManager: FocusManager,
    optimalTitleVisible: Boolean,
    onDismissTitleVisible: () -> Unit,
    cityInfo: CityInfo,
    startDate: String,
    endDate: String?,
    isOneDay: Boolean,
    dateList: List<LocalDate>,
    tourList: List<List<TourInfo>>,
    selectedTours: List<TourInfo>,
    oneDayTourList: List<TourInfo>,
    oneDayTourInfo: TourInfo?,
    state: HomeState,
    modifier: Modifier = Modifier,
    onClick: (index: Int) -> Unit,
    onOneDayTourClick: () -> Unit,
    onCreateTour: (title: String) -> Unit,
    onBackPressed: () -> Unit,
    onTopBarButtonClick: () -> Unit,
) {
    val city = LatLng(cityInfo.mapY - 0.3, cityInfo.mapX)
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition(city, 8.0)
    }

    val scrollState = rememberLazyListState()

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            initialValue = SheetValue.Expanded,
            density = Density(0.6f),
            skipPartiallyExpanded = false,
        )
    )
    val coroutineScope = rememberCoroutineScope()
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.lottie_search))
    val lottieAnimatable = rememberLottieAnimatable()

    var bottomSheetVisible by remember {
        mutableStateOf(true)
    }
    BackHandler(enabled = true) {
        coroutineScope.launch {
            onBackPressed()
        }
    }

    KotripOptimalDialog(
        context = context,
        visible = optimalTitleVisible,
        onDismissRequest = onDismissTitleVisible,
        onCreate = { title ->
            if (title.isEmpty()) showToast(context, "제목을 입력하세요.")
            else {
                onCreateTour(title)
                onDismissTitleVisible()
            }
        }
    )

    Column {
        when (state.status) {
            UiState.Loading -> {
                bottomSheetVisible = false
                LaunchedEffect(key1 = composition) {
                    lottieAnimatable.animate(
                        composition = composition,
                        clipSpec = LottieClipSpec.Frame(0, 1200),
                        initialProgress = 0f
                    )
                }
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    LottieAnimation(
                        composition = composition,
                        progress = { lottieAnimatable.progress },
                        contentScale = ContentScale.Crop,
                    )
                }
            }

            UiState.Success -> Unit
            UiState.Failed -> Unit
            UiState.Idle -> Unit
            else -> {}
        }

        if (isOneDay) {
            KotripTopBar(
                cityTitle = "${cityInfo.title} 여행",
                scheduleContent = startDate,
//                onClick = onCreateTour,
                onClick = onTopBarButtonClick,
            )
        } else {
            KotripTopBar(
                cityTitle = "${cityInfo.title} 여행",
                scheduleContent = "$startDate ~ $endDate",
//                onClick = onCreateTour
                onClick = onTopBarButtonClick
            )
        }

        if (bottomSheetVisible) {
            BottomSheetScaffold(
                sheetContainerColor = Color.White,
                scaffoldState = scaffoldState,
                sheetContent = {
                    LazyColumn(
                        state = scrollState,
                        content = {
                            if (dateList.isEmpty()) {
                                /**
                                 * 당일치기
                                 */
                                item {
                                    KotripInputStartArea(
                                        text = oneDayTourInfo?.title ?: "출발지를 선정해주세요.",
                                        onClick = {
                                            onOneDayTourClick()
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                    KotripScheduleItem(onClick = { onClick(-1) })
                                    if (oneDayTourList.isEmpty()) {
                                        KotripScheduleEmpty()
                                    } else {
                                        oneDayTourList.forEachIndexed { index, tourInfo ->
                                            if (index == oneDayTourList.size - 1) {
                                                KotripTripItem(
                                                    context = context,
                                                    tourInfo = tourInfo,
                                                    position = -1,
                                                    onClick = {
                                                        val tourPosition =
                                                            LatLng(
                                                                it.latitude - 0.005,
                                                                it.longitude
                                                            )
                                                        cameraPositionState.position =
                                                            CameraPosition(
                                                                tourPosition,
                                                                14.0,
                                                                0.0,
                                                                0.0
                                                            )
                                                    }
                                                )
                                            } else {
                                                KotripTripItem(
                                                    context = context,
                                                    tourInfo = tourInfo,
                                                    onClick = {
                                                        val tourPosition =
                                                            LatLng(
                                                                it.latitude - 0.005,
                                                                it.longitude
                                                            )
                                                        cameraPositionState.position =
                                                            CameraPosition(
                                                                tourPosition,
                                                                14.0,
                                                                0.0,
                                                                0.0
                                                            )

                                                    })
                                            }
                                        }
                                    }
                                }
                            } else {
                                /**
                                 * 2~5일 일정생성
                                 */
                                itemsIndexed(tourList) { index, item ->
                                    Log.e("aaa", "item : $item")
                                    KotripScheduleItem(
                                        schedule = dateList[index],
                                        day = index,
                                        onClick = { onClick(index) }
                                    )
                                    if (item.isEmpty()) {
                                        KotripScheduleEmpty()
                                        if (index == tourList.size - 1) {
                                            Spacer(modifier = Modifier.height(120.dp))
                                        }
                                    } else {
                                        item.forEachIndexed { itemIndex, tourInfo ->
                                            if (itemIndex == item.size - 1) {
                                                KotripTripItem(
                                                    context = context,
                                                    tourInfo = tourInfo,
                                                    position = -1,
                                                    onClick = {
                                                        val tourPosition =
                                                            LatLng(
                                                                it.latitude - 0.005,
                                                                it.longitude
                                                            )
                                                        cameraPositionState.position =
                                                            CameraPosition(
                                                                tourPosition,
                                                                14.0,
                                                                0.0,
                                                                0.0
                                                            )
                                                    }
                                                )
                                                if (index == tourList.size - 1) {
                                                    Spacer(modifier = Modifier.height(10.dp))
                                                }
                                            } else {
                                                KotripTripItem(
                                                    context = context,
                                                    tourInfo = tourInfo,
                                                    onClick = {
                                                        val tourPosition =
                                                            LatLng(
                                                                it.latitude - 0.005,
                                                                it.longitude
                                                            )
                                                        cameraPositionState.position =
                                                            CameraPosition(
                                                                tourPosition,
                                                                14.0,
                                                                0.0,
                                                                0.0
                                                            )
                                                    })
                                            }
                                        }
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .heightIn(0.dp, 300.dp)
                            .padding(horizontal = 16.dp)
                    )
                },
                containerColor = Color.White,
                contentColor = Color.White
            ) {
                NaverMap(
                    cameraPositionState = cameraPositionState,
                    onMapClick = { _, _ ->
                        scope.launch {
                            scaffoldState.bottomSheetState.partialExpand()
                        }
                    }
                ) {
                    if (isOneDay) {
                        oneDayTourInfo?.let {
                            val position = LatLng(it.latitude - 0.1, it.longitude)
                            Marker(
                                state = MarkerState(position),
                                captionText = it.title,
                                onClick = {
                                    cameraPositionState.position = CameraPosition(position, 9.0)
                                    true
                                }
                            )
                        }
                        if (oneDayTourList.isNotEmpty()) {
                            oneDayTourList.forEach {
                                val position = LatLng(it.latitude - 0.1, it.longitude)
                                Marker(
                                    state = MarkerState(position),
                                    captionText = it.title,
                                    onClick = {
                                        cameraPositionState.position = CameraPosition(position, 9.0)
                                        true
                                    }
                                )
                            }
                        }
                    } else {
                        selectedTours.forEach {
                            val tourPosition =
                                LatLng(it.latitude, it.longitude)
                            Marker(
                                state = MarkerState(tourPosition),
                                captionText = it.title,
//                            onClick = {
//                                cameraPositionState.position =
//                                    CameraPosition(tourPosition, 10.0)
//                                true
//                            }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
}