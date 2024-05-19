package com.koreatech.kotrip_android.presentation.views.optimal

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextPaint
import android.view.Gravity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.koreatech.kotrip_android.R
import com.koreatech.kotrip_android.data.model.response.HotelResponseDto
import com.koreatech.kotrip_android.data.model.response.OptimalScheduleResponseDto
import com.koreatech.kotrip_android.data.model.response.OptimalToursResponseDto
import com.koreatech.kotrip_android.presentation.common.UiState
import com.koreatech.kotrip_android.presentation.components.organisms.HotelDetailDialog
import com.koreatech.kotrip_android.presentation.components.organisms.KotripOptimalItem
import com.koreatech.kotrip_android.presentation.components.organisms.KotripOptimalScheduleItem
import com.koreatech.kotrip_android.presentation.components.organisms.TourDetailDialog
import com.koreatech.kotrip_android.presentation.components.parts.KotripOptimalTopBar
import com.koreatech.kotrip_android.presentation.theme.MarkerBlue
import com.koreatech.kotrip_android.presentation.theme.MarkerBlueBold
import com.koreatech.kotrip_android.presentation.theme.Orange4d
import com.koreatech.kotrip_android.presentation.theme.Orange_FFCD4C
import com.koreatech.kotrip_android.presentation.theme.Pink
import com.koreatech.kotrip_android.presentation.utils.BackHandler
import com.koreatech.kotrip_android.presentation.views.hotel.createCircleBitmapFromUrl
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
import kotlinx.coroutines.runBlocking
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class, ExperimentalNaverMapApi::class)
@Composable
fun OptimalPage(
    state: UiState,
    city: String,
    routes: List<OptimalScheduleResponseDto>,
    paths: List<List<LatLng>>,
    hotels: List<HotelResponseDto?>,
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

    var hotelDetailInfo by remember {
        mutableStateOf<HotelResponseDto?>(null)
    }

    var hotelDetailVisible by remember {
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
                HotelDetailDialog(
                    context = context,
                    hotelInfo = hotelDetailInfo,
                    visible = hotelDetailVisible,
                    onDismissRequest = {
                        hotelDetailVisible = false
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

                            item.tours.forEachIndexed { detailIndex, optimalToursResponseDto ->
                                KotripOptimalItem(
                                    position = if (detailIndex == 0) 1 else if (detailIndex == item.tours.size - 1) 2 else 0,
                                    tourItemPosition = index + 1 to detailIndex + 1,
                                    tourSize = item.tours.size,
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
                            if (hotels.isNotEmpty() && index < routes.size - 1) {
                                val hotelItem = hotels[index]
                                if (hotelItem != null) {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Color.White),
                                        elevation = CardDefaults.elevatedCardElevation(2.dp),
                                        onClick = {
                                            val tourPosition =
                                                LatLng(
                                                    hotelItem.latitude - 0.005,
                                                    hotelItem.longitude
                                                )
                                            cameraPositionState.position =
                                                CameraPosition(tourPosition, 14.0, 0.0, 0.0)
                                        }
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(Color.White)
                                                .padding(
                                                    vertical = 10.dp,
                                                    horizontal = 10.dp
                                                ),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = hotelItem.title,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Spacer(modifier = Modifier.height(10.dp))
                                            AsyncImage(
                                                model = ImageRequest.Builder(context)
                                                    .data(hotelItem.imageUrl1)
                                                    .crossfade(true)
                                                    .build(),
                                                contentDescription = null,
                                                contentScale = ContentScale.Fit,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(100.dp)
                                            )
                                        }
                                    }
                                }
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
                    logoMargin = PaddingValues(top = 15.dp, end = 15.dp),
                    rotateGesturesFriction = 1f
                ),
            ) {
                paths.forEachIndexed { index, latLngs ->
                    if (latLngs.isNotEmpty()){
                        if (index != pathPosition ) {
                            PathOverlay(
                                coords = latLngs,
                                width = 2.dp,
                                color = MarkerBlue,
                                outlineColor = Color.Black,
                                outlineWidth = 1.dp
                            )
                        } else {
                            PathOverlay(
                                coords = latLngs,
                                width = 4.dp,
                                color = MarkerBlueBold,
                                outlineColor = Color.Black,
                                outlineWidth = 1.dp,
                                zIndex = 10
                            )
                        }
                    }
                }
                if (hotels.isNotEmpty()) {
                    hotels.forEach { hotelItem ->
                        if (hotelItem != null) {
                            val markerPosition = LatLng(hotelItem.latitude, hotelItem.longitude)
                            val image = runBlocking {
                                createCircleBitmapFromUrl(hotelItem.imageUrl1, context)!!
                            }
                            Marker(
                                state = MarkerState(markerPosition),
                                icon = OverlayImage.fromBitmap(image),
                                onClick = {
                                    hotelDetailInfo = hotelItem
                                    hotelDetailVisible = true
                                    true
                                }
                            )
                        }
                    }
                }

                routes.forEachIndexed { index, markerTours ->
                    if (markerTours.tours.isNotEmpty()) {
                        if (index == pathPosition) {
                            markerTours.tours.forEachIndexed { positionIndex, item ->
                                val position = LatLng(item.latitude, item.longitude)
                                /** 주 일정 마커 **/
                                Marker(
                                    state = MarkerState(position),
                                    icon = OverlayImage.fromBitmap(
                                        createBitmap(
                                            "${index + 1}-${positionIndex + 1}",
                                            context,
                                            ContextCompat.getColor(
                                                context, R.color.marker_blue_bold
                                            )
                                        )
                                    ),
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
                                /** 주 일정 + 1 마커 **/
                                Marker(
                                    state = MarkerState(position),
                                    icon = OverlayImage.fromBitmap(
                                        createBitmap(
                                            "${index + 1}-${positionIndex + 1}", context,
                                            ContextCompat.getColor(
                                                context, R.color.marker_blue
                                            )
                                        )
                                    ),
                                    onClick = {
                                        tourDetailInfo = item
                                        tourDetailVisible = true
                                        true
                                    }
                                )
                            }
                        } else {
                            /** 나머지 일차 마커 **/
                            markerTours.tours.forEachIndexed { positionIndex, item ->
                                val position = LatLng(item.latitude, item.longitude)
                                Marker(
                                    state = MarkerState(position),
                                    icon = OverlayImage.fromBitmap(
                                        createBitmap(
                                            "${index + 1}-${positionIndex + 1}", context,
                                            ContextCompat.getColor(
                                                context, R.color.marker_blue
                                            )
                                        )
                                    ),
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

fun createBitmap(position: String, context: Context, fillColor: Int): Bitmap {
    val bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = Paint().apply {
        color = fillColor
        style = Paint.Style.FILL
    }
    val borderPaint = Paint().apply {
        color = android.graphics.Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 1f
    }

    canvas.drawCircle(25f, 25f, 25f, paint)
    canvas.drawCircle(25f, 25f, 25f, borderPaint)

    val textPaint = Paint().apply {
        color = android.graphics.Color.BLACK
        textSize = 30f
        textAlign = Paint.Align.CENTER
    }
    val text = position
    val textHeight = textPaint.descent() + textPaint.ascent()
    canvas.drawText(text, 25f, 25f - textHeight / 2, textPaint)

    return bitmap
}

@Preview
@Composable
fun OptimalPagePreview() {
    OptimalPage(
        state = UiState.Loading,
        city = "",
        routes = listOf(),
        paths = listOf(),
        hotels = emptyList(),
        onBackPressed = {},
        onLoadHotel = { _, _, _, _, _ -> }
    )
}
