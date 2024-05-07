package com.koreatech.kotrip_android.presentation.components.organisms

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koreatech.kotrip_android.presentation.components.parts.KotripScheduleButton
import java.time.LocalDate

@Composable
fun KotripScheduleItem(
    schedule: LocalDate? = null,
    day: Int = 0,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (schedule != null) "${day + 1}일차" else "",
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = schedule?.toString() ?: "", fontSize = 12.sp, color = Color.Black)

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            KotripScheduleButton(onClick = onClick)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KotripScheduleItemPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        KotripScheduleItem(schedule = LocalDate.now(), day = 1) { }
    }
}