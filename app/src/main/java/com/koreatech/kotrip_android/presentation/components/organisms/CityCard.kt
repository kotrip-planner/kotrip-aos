package com.koreatech.kotrip_android.presentation.components.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.koreatech.kotrip_android.R
import com.koreatech.kotrip_android.model.trip.CityInfo

@Composable
fun CityCard(
    cityInfo: CityInfo,
    index: Int,
    cities: List<Int>,
    onClick: (cityInfo: CityInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .background(Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = {
            onClick(cityInfo)
        }
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight(Alignment.CenterVertically)
                .wrapContentWidth(Alignment.CenterHorizontally)
                .background(Color.White)
                .padding(8.dp)
        ) {
            Text(
                text = cityInfo.title,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(cities[index])
                    .size(200, 100)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.ic_error),
                contentDescription = null,
                modifier = Modifier
                    .size(width = 200.dp, height = 100.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

