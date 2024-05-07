package com.koreatech.kotrip_android.presentation.components.organisms

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.koreatech.kotrip_android.R
import com.koreatech.kotrip_android.data.model.response.OptimalToursResponseDto


@Composable
fun TourDetailDialog(
    context: Context,
    tourInfo: OptimalToursResponseDto?,
    visible: Boolean,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
) {
    if (visible) {
        CustomAlertDialog(onDismissRequest = onDismissRequest) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(tourInfo?.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    placeholder = painterResource(id = R.drawable.ic_question_black),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .fillMaxWidth()
                        .height(150.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "관광지 이름 : ${tourInfo?.title}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                Text(
                    text = "위치\n위도 : ${tourInfo?.latitude} / 경도 : ${tourInfo?.longitude}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun CustomAlertDialog(
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties,
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun TourDetailDialogPreview() {
    TourDetailDialog(
        context = LocalContext.current,
        tourInfo = OptimalToursResponseDto(
            id = 1,
            title = "해운대",
            imageUrl = "",
            latitude = 0.0,
            longitude = 0.0
        ),
        visible = true
    ) {

    }
}