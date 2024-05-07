package com.koreatech.kotrip_android.presentation.views.home

import android.content.Context
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
    cityInfo: CityInfo,
    startDate: String,
    endDate: String?,
    isOneDay: Boolean,
    dateList: List<LocalDate>,
    tourList: List<List<TourInfo>>,
    oneDayTourList: List<TourInfo>,
    oneDayTourInfo: TourInfo?,
    state: HomeState,
    modifier: Modifier = Modifier,
    onClick: (index: Int) -> Unit,
    onOneDayTourClick: () -> Unit,
    onCreateTour: () -> Unit,
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

    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.lottie_search))
    val lottieAnimatable = rememberLottieAnimatable()

    var optimalTitleVisible by remember {
        mutableStateOf(false)
    }

    KotripOptimalDialog(
        context = context,
        visible = optimalTitleVisible,
        onDismissRequest = {
            optimalTitleVisible = false
        },
        onCreate = { title ->
            showToast(context, title)
        }
    )

    Column {
        when (state.status) {
            UiState.Loading -> {
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
        }

        if (isOneDay) {
            KotripTopBar(
                cityTitle = "${cityInfo.title} 여행",
                scheduleContent = startDate,
                onClick = onCreateTour
            )
        } else {
            KotripTopBar(
                cityTitle = "${cityInfo.title} 여행",
                scheduleContent = "$startDate ~ $endDate",
//                onClick = onCreateTour
                onClick = {
                    optimalTitleVisible = true
                }
            )
        }

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
                                                position = -1
                                            )
                                        } else {
                                            KotripTripItem(context = context, tourInfo = tourInfo)
                                        }
                                    }
                                }
                            }
                        } else {
                            /**
                             * 2~5일 일정생성
                             */
                            itemsIndexed(tourList) { index, item ->
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
                                                position = -1
                                            )
                                            if (index == tourList.size - 1) {
                                                Spacer(modifier = Modifier.height(40.dp))
                                            }
                                        } else {
                                            KotripTripItem(context = context, tourInfo = tourInfo)
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(40.dp))
                            }
                            item {
                                Spacer(modifier = Modifier.height(60.dp))
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
                    tourList.forEachIndexed { index, markerTours ->
                        if (markerTours.isNotEmpty()) {
                            if (index == scrollState.firstVisibleItemIndex) {
                                val tourPosition =
                                    LatLng(markerTours[0].latitude - 0.1, markerTours[0].longitude)
                                cameraPositionState.position = CameraPosition(tourPosition, 9.0)
                                markerTours.forEach {
                                    val position = LatLng(it.latitude, it.longitude)
                                    Marker(
                                        state = MarkerState(position),
                                        captionText = it.title,
                                        onClick = {
                                            cameraPositionState.position =
                                                CameraPosition(position, 10.0)
                                            true
                                        }
                                    )
                                }
                            }

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