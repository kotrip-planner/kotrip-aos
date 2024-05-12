package com.koreatech.kotrip_android.presentation.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.koreatech.kotrip_android.R
import com.koreatech.kotrip_android.data.model.response.HotelResponseDto
import com.koreatech.kotrip_android.presentation.theme.Orange_FF9800

@Composable
fun HotelRow(
    context: Context,
    hotelResponseDto: HotelResponseDto,
    index: Int,
    selectedId: Int,
    onSelectedIdChanged: (idx: Int) -> Unit,
    modifier: Modifier = Modifier,
    onClick: (hotel: HotelResponseDto) -> Unit,
    onAddClick: (hotel: HotelResponseDto) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                selected = selectedId == index,
                onClick = {
                    onClick(hotelResponseDto)
                    if (selectedId == index) {
                        onSelectedIdChanged(-1)
                    } else onSelectedIdChanged(index)
                }
            )
            .padding(10.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text(
                    text = hotelResponseDto.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black
                )

                Text(
                    text = if (hotelResponseDto.addr1.isEmpty()) "" else {
                        if (hotelResponseDto.addr2.isNotEmpty()) "주소 : ${hotelResponseDto.addr1} / ${hotelResponseDto.addr2}"
                        else "주소 : ${hotelResponseDto.addr1}"
                    },
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black
                )

                Text(
                    text = if (hotelResponseDto.tel.isEmpty()) "" else "전화번호 : ${hotelResponseDto.tel}",
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black
                )
            }

            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(hotelResponseDto.imageUrl1)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp),
            )
        }
        if (selectedId == index) {
            Spacer(modifier = Modifier.height(5.dp))
            Card(
                colors = CardDefaults.cardColors(Orange_FF9800),
                onClick = {
                    onAddClick(hotelResponseDto)
                },
            ) {
                Text(
                    text = "호텔 선택하기",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(
                        horizontal = 6.dp,
                        vertical = 2.dp
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HotelRowPreview() {
    HotelRow(
        LocalContext.current,
        HotelResponseDto(
            addr1 = "주소1",
            addr2 = "주소2",
            title = "제목",
            longitude = 0.0,
            latitude = 0.0,
            imageUrl1 = "",
            imageUrl2 = "",
            tel = "전화번호",
            distance = 100
        ),
        selectedId = 1,
        onSelectedIdChanged = {},
        index = 1,
        onClick = {},
        onAddClick = {_ ->}
    )
}