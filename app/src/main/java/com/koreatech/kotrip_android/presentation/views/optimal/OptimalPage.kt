package com.koreatech.kotrip_android.presentation.views.optimal

import android.view.Gravity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koreatech.kotrip_android.R
import com.koreatech.kotrip_android.data.model.response.OptimalScheduleResponseDto
import com.koreatech.kotrip_android.data.model.response.OptimalToursResponseDto
import com.koreatech.kotrip_android.presentation.common.UiState
import com.koreatech.kotrip_android.presentation.components.organisms.KotripOptimalItem
import com.koreatech.kotrip_android.presentation.components.organisms.KotripOptimalScheduleItem
import com.koreatech.kotrip_android.presentation.components.organisms.TourDetailDialog
import com.koreatech.kotrip_android.presentation.components.parts.KotripOptimalTopBar
import com.koreatech.kotrip_android.presentation.theme.Orange_FFCD4C
import com.koreatech.kotrip_android.presentation.theme.Pink
import com.koreatech.kotrip_android.presentation.utils.BackHandler
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.PathOverlay
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalNaverMapApi::class)
@Composable
fun OptimalPage(
    state: UiState,
    city: String,
    routes: List<OptimalScheduleResponseDto>,
    paths: List<List<LatLng>>,
    modifier: Modifier = Modifier,
    onLoadHotel: (x: Double, y: Double, bx: Double, by: Double, pos: Int) -> Unit,
    onBackPressed: () -> Unit,
) {
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {}
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            initialValue = SheetValue.Expanded,
            density = Density(0.6f),
            skipPartiallyExpanded = false,
        )
    )
    val coroutineScope = rememberCoroutineScope()
    var pathPosition by remember {
        mutableStateOf(0)
    }
    var tourDetailInfo by remember {
        mutableStateOf<OptimalToursResponseDto?>(null)
    }

    var tourDetailVisible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        if (routes.isEmpty()) return@LaunchedEffect
        val tours = routes.firstOrNull()?.tours
        if (tours?.isNotEmpty() == true) {
            val initPosition = tours.get(tours.size / 2).let {
                LatLng(it.latitude - 0.1, it.longitude)
            }
            cameraPositionState.position =
                CameraPosition(initPosition, 9.0)
        }
    }


    BackHandler(enabled = true) {
        coroutineScope.launch {
            onBackPressed()
        }
    }

    if (state == UiState.Loading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
    Column {
        KotripOptimalTopBar(
            cityTitle = "$city 여행",
            scheduleContent = "${routes[0].date} ~ ${routes[routes.size - 1].date}"
        )
        BottomSheetScaffold(
            sheetContainerColor = Color.White,
            scaffoldState = scaffoldState,
            sheetContent = {
                TourDetailDialog(
                    context = context,
                    tourInfo = tourDetailInfo,
                    visible = tourDetailVisible,
                    onDismissRequest = {
                        tourDetailVisible = false
                    }
                )

                LazyColumn(
                    state = scrollState,
                    content = {
                        itemsIndexed(routes) { index, item ->
                            KotripOptimalScheduleItem(
                                schedule = item.date,
                                day = index,
                                onClick = { position ->
                                    val tourPosition =
                                        LatLng(
                                            item.tours[item.tours.size / 2].latitude - 0.1,
                                            item.tours[item.tours.size / 2].longitude,
                                        )
                                    cameraPositionState.position =
                                        CameraPosition(tourPosition, 9.0, 0.0, 0.0)
                                    pathPosition = position
                                }
                            )

                            item.tours.forEachIndexed { index, optimalToursResponseDto ->
                                KotripOptimalItem(
                                    position = if (index == 0) 1 else if (index == item.tours.size - 1) 2 else 0,
                                    context = context,
                                    tourInfo = optimalToursResponseDto,
                                    onClick = {
                                        val tourPosition =
                                            LatLng(it.latitude - 0.005, it.longitude)
                                        cameraPositionState.position =
                                            CameraPosition(tourPosition, 14.0, 0.0, 0.0)
                                    }
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            if (index != routes.size - 1) {
                                Button(
                                    onClick = {
                                        item.tours
                                        onLoadHotel(
                                            routes[index].tours.last().longitude,
                                            routes[index].tours.last().latitude,
                                            routes[index + 1].tours.first().longitude,
                                            routes[index + 1].tours.first().latitude,
                                            index
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 10.dp),
                                    colors = ButtonDefaults.buttonColors(Orange_FFCD4C),
                                ) {
                                    Text(
                                        text = "${index + 1}일차 숙소 목록 확인하기",
                                        color = Color.Black,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(20.dp))
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
                },
                uiSettings = MapUiSettings(
                    isZoomControlEnabled = false,
                    logoGravity = Gravity.TOP or Gravity.END,
                    logoMargin = PaddingValues(top = 30.dp),
                    rotateGesturesFriction = 1f
                ),
            ) {
                paths.forEachIndexed { index, latLngs ->
                    if (index == pathPosition) {
                        PathOverlay(
                            coords = latLngs,
                            width = 2.dp,
                            color = Color.Blue,
                            outlineWidth = 0.dp,
                            outlineColor = Color.Blue,
                        )
                    } else {
                        if (index == pathPosition + 1 && pathPosition < routes.size - 1) {
                            PathOverlay(
                                coords = latLngs,
                                width = 2.dp,
                                color = Pink,
                                outlineWidth = 0.dp,
                                outlineColor = Pink,

                                )
                        }
                    }
                }
                routes.forEachIndexed { index, markerTours ->
                    if (markerTours.tours.isNotEmpty()) {
                        if (index == pathPosition) {
                            markerTours.tours.forEachIndexed { positionIndex, item ->
                                val position = LatLng(item.latitude, item.longitude)
                                Marker(
                                    state = MarkerState(position),
                                    captionText = "${item.title}\n${index + 1}-${positionIndex + 1}",
                                    icon = OverlayImage.fromResource(R.drawable.ic_marker),
                                    onClick = {
                                        tourDetailInfo = item
                                        tourDetailVisible = true
                                        true
                                    }
                                )
                            }
                        } else if (index == pathPosition + 1) {
                            markerTours.tours.forEachIndexed { positionIndex, item ->
                                val position = LatLng(item.latitude, item.longitude)
                                Marker(
                                    state = MarkerState(position),
                                    captionText = "${item.title}\n${index + 1}-${positionIndex + 1}",
                                    icon = OverlayImage.fromResource(R.drawable.ic_marker),
                                    iconTintColor = Color.Red,
                                    onClick = {
                                        tourDetailInfo = item
                                        tourDetailVisible = true
                                        true
                                    }
                                )
                            }
                        } else {
                            markerTours.tours.forEachIndexed { positionIndex, item ->
                                val position = LatLng(item.latitude, item.longitude)
                                Marker(
                                    state = MarkerState(position),
                                    icon = OverlayImage.fromResource(R.drawable.ic_marker_gray),
//                                    captionText = "${it.title}\n${index + 1}-${positionIndex + 1}",
//                                    captionTextSize = 10.sp,
                                    onClick = {
                                        tourDetailInfo = item
                                        tourDetailVisible = true
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


@Preview
@Composable
fun OptimalPagePreview() {
    OptimalPage(
        state = UiState.Loading,
        city = "",
        routes = listOf(),
        paths = listOf(),
        onBackPressed = {},
        onLoadHotel = { _, _, _, _, _ -> }
    )
}
