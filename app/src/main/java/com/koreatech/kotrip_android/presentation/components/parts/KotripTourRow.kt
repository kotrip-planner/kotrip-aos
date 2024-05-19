package com.koreatech.kotrip_android.presentation.components.parts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.koreatech.kotrip_android.R
import com.koreatech.kotrip_android.model.home.TourInfo

@Composable
fun KotripTourRow(
    index: Int = 0,
    tourPosition: Int = 0,
    tourInfo: TourInfo,
    modifier: Modifier = Modifier,
) {
    Row {
        if (tourPosition == 0) {
            Text(
                text = "$index 일차",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 5.dp)
            )
        }
        Column(
            modifier = Modifier.padding(end = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(tourInfo.imageUrl)
                    .crossfade(true)
                    .build(),
                error = painterResource(id = R.drawable.img_empty_tour),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = tourInfo.title,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.width(50.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KotripTourRowPreview() {
    KotripTourRow(
        index = 1,
        tourPosition = 0,
        TourInfo(
            1,
            "해운대",
            "",
            "ㅁㄴㅇㄹ",
            1.2,
            1.2,
            true
        )
    )
}