package com.koreatech.kotrip_android.presentation.components.parts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.koreatech.kotrip_android.presentation.theme.Orange_FFCD4C
import com.koreatech.kotrip_android.presentation.views.trip.CalendarScheduleState
import java.time.LocalDate

@Composable
fun Day(
    today: LocalDate,
    day: CalendarDay,
    isSelected: Boolean,
    calendarScheduleState: CalendarScheduleState,
    onClick: (CalendarDay) -> Unit,
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f) // This is important for square-sizing!
            .testTag("MonthDay")
            .padding(6.dp)
            .clip(CircleShape)
            .background(
                color = if (calendarScheduleState.first != null && calendarScheduleState.second != null) {
                    if (calendarScheduleState.first.date.isBefore(calendarScheduleState.second.date)) { // first <=
                        if ((day.date.isBefore(calendarScheduleState.second.date) || day.date.isEqual(
                                calendarScheduleState.second.date
                            )) && (day.date.isAfter(calendarScheduleState.first.date) || day.date.isEqual(
                                calendarScheduleState.first.date
                            ))
                        ) {
                            Orange_FFCD4C
                        } else {
                            Color.Transparent
                            if (isSelected) {
                                Orange_FFCD4C
                            } else Color.Transparent
                        }
                    } else {
                        if ((day.date.isBefore(calendarScheduleState.first.date) || day.date.isEqual(
                                calendarScheduleState.first.date
                            )) && (day.date.isAfter(calendarScheduleState.second.date) || day.date.isEqual(
                                calendarScheduleState.second.date
                            ))
                        ) {
                            Orange_FFCD4C
                        } else {
                            Color.Transparent
                            if (isSelected) {
                                Orange_FFCD4C
                            } else Color.Transparent
                        }
                    }
                } else {
                    if (isSelected) {
                        Orange_FFCD4C
                    } else Color.Transparent
                }

            )
            // Disable clicks on inDates/outDates
            .clickable(
                enabled = day.date >= today,
                onClick = {
                    onClick(day)
                },
            ),
        contentAlignment = Alignment.Center,
    ) {
        val textColor = when (day.position) {
            // Color.Unspecified will use the default text color from the current theme
            DayPosition.MonthDate -> if (isSelected) Color.White else Color.Unspecified
            DayPosition.InDate, DayPosition.OutDate -> Color.Red
        }
        Text(
            text = day.date.dayOfMonth.toString(),
            color = if (day.date < today) Color.LightGray else textColor,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Normal else if (day.date == today) FontWeight.Bold else FontWeight.Normal
        )
    }
}
