package com.koreatech.kotrip_android.presentation.components.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koreatech.kotrip_android.presentation.theme.Orange_FFCD4C

@Composable
fun KotripOptimalScheduleItem(
    schedule: String,
    day: Int = 0,
    modifier: Modifier = Modifier,
    onClick: (position: Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${day + 1}일차",
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = schedule, fontSize = 12.sp, color = Color.Black)

        Spacer(modifier = Modifier.width(10.dp))
        Button(
            onClick = { onClick(day) },
            colors = ButtonDefaults.buttonColors(Orange_FFCD4C)
        ) {
            Text(text = "일정 보기", color = Color.Black)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KotripOptimalScheduleItemPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        KotripOptimalScheduleItem(schedule = "2024-1-1", day = 1, onClick = {})
    }
}