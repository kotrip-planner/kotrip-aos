package com.koreatech.kotrip_android.presentation.views.trip

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.daysOfWeek
import com.koreatech.kotrip_android.R
import com.koreatech.kotrip_android.presentation.components.organisms.OnboardCard
import com.koreatech.kotrip_android.presentation.components.parts.CalendarHeader
import com.koreatech.kotrip_android.presentation.components.parts.Day
import com.koreatech.kotrip_android.presentation.components.parts.KotripButton
import com.koreatech.kotrip_android.presentation.components.parts.MonthHeader
import com.koreatech.kotrip_android.presentation.utils.showToast
import java.time.LocalDate
import java.time.Period
import java.time.YearMonth
import java.time.ZoneId
import kotlin.math.abs

@Composable
fun SchedulePage(
    context: Context,
    tripState: TripState,
    isOneDay: Boolean,
    adjacentMonths: Long = 500,
    modifier: Modifier = Modifier,
    onClick: (first: CalendarDay?, second: CalendarDay?) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OnboardCard(
            title = stringResource(id = R.string.onboard_select_schedule_title),
            subTitle = stringResource(id = R.string.onboard_select_schedule_sub_title),
            warningText = if (isOneDay) stringResource(id = R.string.onboard_select_schedule_oneday_warning_text) else stringResource(
                id = R.string.onboard_select_schedule_warning_text
            ),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        val currentMonth = remember { YearMonth.now() }
        val startMonth = remember { currentMonth.minusMonths(adjacentMonths) }
        val endMonth = remember { currentMonth.plusMonths(adjacentMonths) }
        val selections = remember { mutableStateListOf<CalendarDay>() }
        val daysOfWeek = remember { daysOfWeek() }
        val calendarScheduleState = remember {
            mutableStateOf(CalendarScheduleState())
        }

        val state = rememberCalendarState(
            startMonth = startMonth,
            endMonth = endMonth,
            firstVisibleMonth = currentMonth,
            firstDayOfWeek = daysOfWeek.first(),
        )

        CalendarHeader(
            year = state.firstVisibleMonth.yearMonth.year,
            month = state.firstVisibleMonth.yearMonth.monthValue
        )

        Spacer(modifier = Modifier.height(10.dp))


        HorizontalCalendar(
            state = state,
            dayContent = { day ->
                Day(
                    LocalDate.now(ZoneId.of("Asia/Seoul")),
                    day = day,
                    calendarScheduleState = calendarScheduleState.value,
                    isSelected = selections.contains(day)
                ) { clicked ->
                    if (calendarScheduleState.value.first == null && calendarScheduleState.value.second == null) {
                        calendarScheduleState.value =
                            calendarScheduleState.value.copy(first = clicked)
                        selections.add(clicked)
                    } else if (calendarScheduleState.value.first != null && calendarScheduleState.value.second == null) {
                        if (calendarScheduleState.value.first == clicked) {
                            calendarScheduleState.value =
                                calendarScheduleState.value.copy(first = null)
                            selections.remove(clicked)
                        } else {
                            if (abs(
                                    Period.between(
                                        calendarScheduleState.value.first?.date,
                                        clicked.date
                                    ).days
                                ) >= 5 && !isOneDay
                            ) {
                                showToast(context = context, msg = "여행일정은 최대 5일 선정할 수 있습니다.")
                            } else if (abs(
                                    Period.between(
                                        calendarScheduleState.value.first?.date,
                                        clicked.date
                                    ).days
                                ) >= 1 && isOneDay
                            ) {
                                showToast(context = context, msg = "당일치기는 하루 일정을 선정해야합니다.")
                            } else {
                                calendarScheduleState.value =
                                    calendarScheduleState.value.copy(second = clicked)
                                selections.add(clicked)
                            }
                        }
                    } else {
                        if (calendarScheduleState.value.first == clicked) {
                            calendarScheduleState.value =
                                calendarScheduleState.value.copy(first = calendarScheduleState.value.second)
                            calendarScheduleState.value =
                                calendarScheduleState.value.copy(second = null)
                            selections.remove(clicked)
                        } else if (calendarScheduleState.value.second == clicked) {
                            calendarScheduleState.value =
                                calendarScheduleState.value.copy(second = null)
                            selections.remove(clicked)
                        }
                    }

                }
            },
            monthHeader = {
                MonthHeader(daysOfWeek = daysOfWeek)
            }
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            KotripButton(
                onClick = {
                    if (calendarScheduleState.value.first != null && calendarScheduleState.value.second != null) {
                        if (calendarScheduleState.value.first?.date?.isBefore(calendarScheduleState.value.second?.date) == true) {
                            onClick(
                                calendarScheduleState.value.first,
                                calendarScheduleState.value.second
                            )
                        } else {
                            onClick(
                                calendarScheduleState.value.second,
                                calendarScheduleState.value.first
                            )
                        }
                    } else {
                        onClick(calendarScheduleState.value.first, null)
                    }
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SelectSchedulePagePreview() {
    SchedulePage(context = LocalContext.current, isOneDay = false, onClick = { a, b ->
    }, tripState = TripState())
}

data class CalendarScheduleState(
    val first: CalendarDay? = null,
    val second: CalendarDay? = null,
)