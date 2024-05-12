package com.koreatech.kotrip_android.presentation.components.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koreatech.kotrip_android.data.model.response.HistoryDataResponseDto
import com.koreatech.kotrip_android.presentation.theme.Orange4d

@Composable
fun TourHistoryRow(
    title: String,
    history: HistoryDataResponseDto,
    modifier: Modifier = Modifier,
    onClick: (uuid: String) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White),
        elevation = CardDefaults.elevatedCardElevation(2.dp),
        onClick = {
            onClick(history.uuid)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(
                    vertical = 6.dp,
                    horizontal = 6.dp
                ),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = history.title,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${history.startDate} ~ ${history.endDate}",
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TourHistoryRowPreview() {
    TourHistoryRow(
        title = "제목",
        HistoryDataResponseDto(
            uuid = "",
            title = "서울 여행",
            imageUrl = "",
            startDate = "2024.05.06",
            endDate = "2024.05.08"
        ),
        onClick = {}
    )
}