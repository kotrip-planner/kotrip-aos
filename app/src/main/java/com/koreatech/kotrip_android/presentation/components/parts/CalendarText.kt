package com.koreatech.kotrip_android.presentation.components.parts

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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

@Composable
fun CalendarText(
    year: Int,
    month: Int,
    isCenter: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Row {
        Text(
            text = year.toString(),
            fontSize = if (isCenter) 20.sp else 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = if (month < 10) ".0$month" else ".$month",
            fontSize = if (isCenter) 18.sp else 12.sp,
            color = Color.Black,
            modifier = Modifier.align(Alignment.Bottom)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarTextPreview() {
    CalendarText(2024, 1)
}