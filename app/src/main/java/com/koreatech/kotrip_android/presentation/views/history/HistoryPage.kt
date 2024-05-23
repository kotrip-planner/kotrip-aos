package com.koreatech.kotrip_android.presentation.views.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.koreatech.kotrip_android.R
import com.koreatech.kotrip_android.data.model.response.HistoryDataResponseDto
import com.koreatech.kotrip_android.presentation.common.UiState
import com.koreatech.kotrip_android.presentation.components.organisms.OnboardCard
import com.koreatech.kotrip_android.presentation.components.organisms.TourHistoryRow
import com.koreatech.kotrip_android.presentation.theme.Orange_FF9800
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HistoryPage(
    uiSate: UiState,
    tourHistories: List<HistoryDataResponseDto>,
    modifier: Modifier = Modifier,
    onClick: (uuid: String) -> Unit,
) {
    val pages = listOf("당일치기 일정", "여행일정")
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        OnboardCard(
            title = stringResource(id = R.string.tour_history),
            subTitle = stringResource(id = R.string.tour_history_look_forward),
            modifier = modifier.padding(16.dp)
        )
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                )
            },
            backgroundColor = Orange_FF9800,
            contentColor = Color.White
        ) {
            pages.forEachIndexed { index, title ->
                Tab(
                    text = {
                        Text(text = title)
                    },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.scrollToPage(index)
                        }
                    }
                )
            }
        }
        HorizontalPager(count = pages.size, state = pagerState) { page ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                if (page == 0) {
                    val newHistories = tourHistories.filter { it.startDate == it.endDate }
                    if (newHistories.isEmpty()) {
                        item {
                            Column(
                                modifier = Modifier
                                    .padding(top = 50.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_not_find),
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                androidx.compose.material3.Text(
                                    text = "당일차기 히스토리가 없습니다.",
                                    fontSize = 18.sp
                                )
                            }
                        }
                    } else {
                        items(newHistories) {
                            TourHistoryRow(
                                title = it.title,
                                history = it,
                                onClick = onClick
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                } else {
                    val newHistories = tourHistories.filter { it.startDate != it.endDate }

                    if (newHistories.isEmpty()) {
                        item {
                            Column(
                                modifier = Modifier
                                    .padding(top = 50.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_not_find),
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                androidx.compose.material3.Text(
                                    text = "여행일정 히스토리가 없습니다.",
                                    fontSize = 18.sp
                                )
                            }
                        }
                    } else {
                        items(newHistories) {
                            TourHistoryRow(
                                title = it.title,
                                history = it,
                                onClick = onClick
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
            if (uiSate == UiState.Loading) {
                Column(
                    modifier = Modifier.fillMaxSize()
                        .background(Color.White),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(10.dp))
                    androidx.compose.material3.Text(
                        text = "일정 찾는중...",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryPagePreview() {
    val list = listOf<HistoryDataResponseDto>(
        HistoryDataResponseDto(
            uuid = "151512",
            title = "해운대",
            city = "서울 여행",
            imageUrl = "adfa",
            startDate = "2024-04-01",
            endDate = "2024-04-03"
        )
    )
    HistoryPage(
        uiSate = UiState.Success,
        tourHistories = list
    ) {

    }
}