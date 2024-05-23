package com.koreatech.kotrip_android.presentation.views.hotel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.koreatech.kotrip_android.R
import com.koreatech.kotrip_android.data.model.response.HotelResponseDto
import com.koreatech.kotrip_android.data.model.response.OptimalToursResponseDto
import com.koreatech.kotrip_android.presentation.CustomMarker
import com.koreatech.kotrip_android.presentation.common.UiState
import com.koreatech.kotrip_android.presentation.components.HotelRow
import com.koreatech.kotrip_android.presentation.components.organisms.HotelDetailDialog
import com.koreatech.kotrip_android.presentation.theme.MarkerBlue
import com.koreatech.kotrip_android.presentation.theme.MarkerBlueBold
import com.koreatech.kotrip_android.presentation.theme.Orange4d
import com.koreatech.kotrip_android.presentation.theme.Orange_FFCD4C
import com.koreatech.kotrip_android.presentation.views.optimal.createBitmap
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.CircleOverlay
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.PathOverlay
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Math.atan2
import java.lang.Math.cos
import java.lang.Math.sin
import java.lang.Math.sqrt

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun HotelPage(
    context: Context,
    state: HotelState,
    position: Int,
    firstRoutes: List<OptimalToursResponseDto>,
    secondRoutes: List<OptimalToursResponseDto>,
    paths: List<List<LatLng>>,
    hotels: List<HotelResponseDto>,
    hotelImageBitmaps: List<Bitmap?>,
    modifier: Modifier = Modifier,
    onAddClick: (hotel: HotelResponseDto, position: Int) -> Unit,
) {
    val ctx = context as ComponentActivity

    val cameraPositionState: CameraPositionState = rememberCameraPositionState {}
    var selectedId by remember {
        mutableStateOf(-1)
    }

    LaunchedEffect(key1 = Unit) {
        val initPosition = LatLng(
            firstRoutes[firstRoutes.size / 2].latitude,
            firstRoutes[firstRoutes.size / 2].longitude
        )
        cameraPositionState.position = CameraPosition(
            initPosition, 9.0
        )
    }
    var hotelDetailInfo by remember {
        mutableStateOf<HotelResponseDto?>(null)
    }

    var hotelDetailVisible by remember {
        mutableStateOf(false)
    }

    HotelDetailDialog(
        context = context,
        hotelInfo = hotelDetailInfo,
        visible = hotelDetailVisible,
        onDismissRequest = {
            hotelDetailVisible = false
        }
    )

    Column(
        modifier = Modifier.background(Color.White)
    ) {
        if (state.status == UiState.Loading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "호텔 찾는중...", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
        NaverMap(
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                isZoomControlEnabled = false,
                rotateGesturesFriction = 1f
            ),
            modifier = Modifier.height(300.dp)
        ) {
            CircleOverlay(
                center = LatLng(
                    (firstRoutes.last().latitude + secondRoutes.first().latitude) / 2,
                    (firstRoutes.last().longitude + secondRoutes.first().longitude) / 2
                ),
                radius = haversine(
                    firstRoutes.last().latitude,
                    firstRoutes.last().longitude,
                    secondRoutes.first().latitude,
                    secondRoutes.first().longitude
                ),
                color = Orange4d,
                outlineWidth = 1.dp,
                outlineColor = Orange_FFCD4C
            )
            /**
             * 테스트 중~ start
             */
//            hotels.forEachIndexed { index, hotel ->
//                val markerPosition = LatLng(hotels[index].latitude, hotels[index].longitude)
//                Marker(
//                    state = MarkerState(markerPosition),
//                    icon = OverlayImage.fromResource(R.drawable.ic_marker_circle),
//                    onClick = {
//                        hotelDetailInfo = hotels[index]
//                        hotelDetailVisible = true
//                        true
//                    }
//                )
//            }
            /**
             * 테스트 중~ end
             */

            hotelImageBitmaps.forEachIndexed { index, bitmap ->
                val markerPosition = LatLng(hotels[index].latitude, hotels[index].longitude)
                Marker(
                    state = MarkerState(markerPosition),
                    icon = if (bitmap != null) {
                        OverlayImage.fromBitmap(bitmap)
                    } else {
                        OverlayImage.fromResource(R.drawable.ic_marker_circle)
                    },
                    onClick = {
                        hotelDetailInfo = hotels[index]
                        hotelDetailVisible = true
                        true
                    }
                )
            }
            firstRoutes.forEachIndexed { index, tour ->
                val tourPosition = LatLng(tour.latitude, tour.longitude)
                Marker(
                    state = MarkerState(tourPosition),
                    captionText = tour.title,
                    icon = OverlayImage.fromBitmap(
                        createBitmap(
                            "${position + 1}-${index + 1}",
                            context,
                            ContextCompat.getColor(context, R.color.marker_blue_bold)
                        )
                    ),
                )
                if (paths[position].isNotEmpty()) {
                    PathOverlay(
                        coords = paths[position],
                        width = 4.dp,
                        color = MarkerBlueBold,
                        outlineWidth = 1.dp,
                        outlineColor = Color.Black,
                        zIndex = 10
                    )
                }
            }
            secondRoutes.forEachIndexed { index, tour ->
                val tourPosition = LatLng(tour.latitude, tour.longitude)
                Marker(
                    state = MarkerState(tourPosition),
                    captionText = tour.title,
                    icon = OverlayImage.fromBitmap(
                        createBitmap(
                            "${position + 2}-${index + 1}",
                            context,
                            ContextCompat.getColor(context, R.color.marker_blue)
                        )
                    ),
                )
                if (paths[position + 1].isNotEmpty()) {
                    PathOverlay(
                        coords = paths[position + 1],
                        width = 2.dp,
                        color = MarkerBlue,
                        outlineColor = Color.Black,
                        outlineWidth = 1.dp
                    )
                }
            }
        }
        if (hotels.isEmpty()) {
            Column(
                modifier = Modifier
                    .padding(top = 50.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_not_find),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "주위 호텔을 찾지 못했습니다.",
                    fontSize = 18.sp
                )
            }
        }
        LazyColumn {
            itemsIndexed(hotels) { index, hotel ->
                HotelRow(
                    context = context,
                    hotelResponseDto = hotel,
                    selectedId = selectedId,
                    onSelectedIdChanged = {
                        selectedId = it
                    },
                    onClick = {
                        val clickPosition = LatLng(
                            it.latitude, it.longitude
                        )
                        cameraPositionState.position =
                            CameraPosition(clickPosition, 16.0)
                    },
                    onAddClick = { addHotel ->
                        onAddClick(addHotel, position)

                    },
                    index = index,
                    modifier = Modifier
                        .background(
                            if (selectedId == index) Orange4d else Color.White
                        )
                )
                Divider(color = Color.LightGray)
            }
        }

    }
}

fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val R = 6371000.0 // 지구 반지름 (킬로미터)
    val latDistance = Math.toRadians(lat2 - lat1)
    val lonDistance = Math.toRadians(lon2 - lon1)
    val a = sin(latDistance / 2) * sin(latDistance / 2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(lonDistance / 2) * sin(lonDistance / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    val distance = R * c

    return distance / 2
}

suspend fun loadHotelImageBitmaps(hotels: List<HotelResponseDto>, context: Context): List<Bitmap> {
    val hotelImageBitmaps = mutableListOf<Bitmap>()

    coroutineScope {
        hotels.forEach { hotel ->
            launch {
                createCircleBitmapFromUrl(hotel.imageUrl1, context)?.let {
                    hotelImageBitmaps.add(it)
                }
            }
        }
    }
    return hotelImageBitmaps
}

//suspend fun createCircleBitmapFromUrl(url: String, context: Context): Bitmap? = withContext(
//    Dispatchers.Default
//) {
//    // Glide를 이용해 이미지를 비트맵으로 다운로드
//    try {
//        val originalBitmap = Glide.with(context)
//            .asBitmap()
//            .load(url)
//            .submit()
//            .get()
//
//        val outputBitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(outputBitmap)
//        val paint = Paint()
//
//        val rect = Rect(0, 0, 50, 50)
//        val rectF = RectF(rect)
//
//        // 이미지를 원형으로 가공하기 위해 Path를 사용
//        val path = Path().apply {
//            addCircle(25f, 25f, 24f, Path.Direction.CCW)
//        }
//
//        // 원형 클리핑 적용
//        canvas.clipPath(path)
//
//        // 원형 모양에 맞게 이미지를 캔버스에 그림
//        paint.isAntiAlias = true
//        canvas.drawBitmap(originalBitmap, null, rectF, paint)
//
//        // 가공된 비트맵 반환
//        outputBitmap
//    } catch (e: Exception) {
//        e.printStackTrace()
//        null
//    }
//}

suspend fun createCircleBitmapFromUrl(url: String, context: Context): Bitmap? = withContext(Dispatchers.IO) {
    try {
        // Glide로 이미지를 비트맵으로 다운로드하면서 크기 조정
        val originalBitmap = Glide.with(context)
            .asBitmap()
            .load(url)
            .submit(50, 50) // 크기를 미리 지정하여 메모리 사용 최적화
            .get()

        val outputBitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(outputBitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG) // 안티 앨리어싱 설정

        val rectF = RectF(0f, 0f, 50f, 50f)

        // 원형 클리핑 적용
        canvas.drawOval(rectF, paint)

        // 원형 모양에 맞게 이미지를 캔버스에 그림
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(originalBitmap, null, rectF, paint)

        outputBitmap
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}