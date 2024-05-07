package com.koreatech.kotrip_android.presentation.views.hotel

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koreatech.kotrip_android.R
import com.koreatech.kotrip_android.data.model.response.HotelResponseDto
import com.koreatech.kotrip_android.data.model.response.OptimalToursResponseDto
import com.koreatech.kotrip_android.presentation.components.HotelRow
import com.koreatech.kotrip_android.presentation.components.organisms.HotelDetailDialog
import com.koreatech.kotrip_android.presentation.theme.Pink
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

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun HotelPage(
    context: Context,
    position: Int,
    firstRoutes: List<OptimalToursResponseDto>,
    secondRoutes: List<OptimalToursResponseDto>,
    paths: List<List<LatLng>>,
    hotels: List<HotelResponseDto>,
    modifier: Modifier = Modifier,
) {
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {}

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

    Column {
        NaverMap(
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                isZoomControlEnabled = false,
                rotateGesturesFriction = 1f
            ),
            modifier = Modifier.height(300.dp)
        ) {
            hotels.forEachIndexed { index, hotel ->
                val markerPosition = LatLng(hotel.latitude, hotel.longitude)
                Marker(
                    state = MarkerState(markerPosition),
                    captionText = "${hotel.title}",
                    icon = OverlayImage.fromResource(R.drawable.ic_hotel),
                    onClick = {
                        hotelDetailInfo = hotel
                        hotelDetailVisible = true
                        true
                    }
                )
            }
            firstRoutes.forEachIndexed { index, tour ->
                val tourPosition = LatLng(tour.latitude, tour.longitude)
                Marker(
                    state = MarkerState(tourPosition),
                    captionText = "${tour.title}\n${position + 1} - ${index + 1}",
                    icon = OverlayImage.fromResource(R.drawable.ic_marker),
                )
                PathOverlay(
                    coords = paths[position],
                    width = 2.dp,
                    color = Color.Blue,
                    outlineWidth = 0.dp,
                    outlineColor = Color.Blue,
                )
            }
            secondRoutes.forEachIndexed { index, tour ->
                val tourPosition = LatLng(tour.latitude, tour.longitude)
                Marker(
                    state = MarkerState(tourPosition),
                    captionText = "${tour.title}\n${position + 2} - ${index + 1}",
                    icon = OverlayImage.fromResource(R.drawable.ic_marker),
                    iconTintColor = Color.Red,
                )
                PathOverlay(
                    coords = paths[position + 1],
                    width = 2.dp,
                    color = Pink,
                    outlineWidth = 0.dp,
                    outlineColor = Pink,
                )
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
            items(hotels) { hotel ->
                HotelRow(
                    context = context,
                    hotelResponseDto = hotel,
                    onClick = {
                        val clickPosition = LatLng(
                            it.latitude, it.longitude
                        )
                        cameraPositionState.position =
                            CameraPosition(clickPosition, 16.0)
                    }
                )
                Divider(color = Color.LightGray)
            }
        }

    }
}