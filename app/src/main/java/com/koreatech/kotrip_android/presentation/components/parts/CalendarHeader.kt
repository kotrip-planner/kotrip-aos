package com.koreatech.kotrip_android.presentation.components.parts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CalendarHeader(
    year: Int,
    month: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CalendarText(
            year = if (month == 1) year - 1 else year,
            month = if (month == 1) 12 else month - 1
        )

        CalendarText(
            year = year,
            month = month,
            isCenter = true
        )

        CalendarText(
            year = if (month == 12) year + 1 else year,
            month = if (month == 12) 1 else month + 1
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarHeaderPreview() {
    CalendarHeader(2024, 12)
}
