package com.koreatech.kotrip_android.presentation.components.organisms

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.koreatech.kotrip_android.R
import com.koreatech.kotrip_android.model.home.TourInfo
import com.koreatech.kotrip_android.presentation.components.parts.KotripPointCard
import com.koreatech.kotrip_android.presentation.theme.Orange_FF9800

@Composable
fun KotripTripItem(
    context: Context,
    tourInfo: TourInfo,
    position: Int = 0, // 1 : 출발지점 , 2 : 도착지점, -1 : 출발,도착 x 마지막 부분
    modifier: Modifier = Modifier,
    onClick: (tourInfo: TourInfo) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable(
                onClick = {
                    onClick(tourInfo)
                }
            )
            .padding(horizontal = 4.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = Color.White)
    ) {
        KotripPointCard(
            pointText = when (position) {
                1 -> stringResource(id = R.string.home_start_point)
                2 -> stringResource(id = R.string.home_destination_point)
                else -> stringResource(id = R.string.home_start_point)
            },
            visible = position == 1 || position == 2,
            modifier = Modifier.padding(bottom = if (position != 2) 20.dp else 10.dp)
        )
        Spacer(modifier = Modifier.fillMaxWidth(0.05f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .wrapContentHeight()
        ) {
            Box(
                modifier = Modifier
                    .height(
                        if (position != 2) 60.dp else 40.dp
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.marker),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                )
                if (position == 0 || position == 1) {
                    Canvas(
                        modifier = Modifier
                            .size(width = 2.5.dp, height = 35.dp)
                            .background(Orange_FF9800)
                            .align(Alignment.BottomCenter),
                        onDraw = {
                            drawLine(
                                color = Orange_FF9800,
                                start = Offset(0f, 0f),
                                end = Offset(0f, 0f),
                                strokeWidth = 10f
                            )
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.fillMaxWidth(0.1f))
            Text(
                text = tourInfo.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(
                    bottom =
                    when (position) {
                        1 -> 20.dp
                        0 -> 30.dp
                        -1 -> 30.dp
                        else -> 10.dp
                    }
                )
            )
        }
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(tourInfo.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.ic_question_black),
            error = painterResource(id = R.drawable.ic_error),
            modifier = Modifier
                .padding(bottom = if (position == 0) 10.dp else 20.dp)
                .size(35.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KotripTripItemPreview() {
    Box(modifier = Modifier.fillMaxWidth()) {
    }
}

//@Preview(showBackground = true)
//@Composable
//fun KotripTripItemPreview2() {
//    Box(modifier = Modifier.fillMaxWidth()) {
//        KotripTripItem(
//            tourInfo = scheduleItemSample,
//            position = 2,
//            context = LocalContext.current
//        )
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun KotripTripItemPreview3() {
//    Box(modifier = Modifier.fillMaxWidth()) {
//        KotripTripItem(
//            tourInfo = scheduleItemSample,
//            context = LocalContext.current
//        )
//    }
//}