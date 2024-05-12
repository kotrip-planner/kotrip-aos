package com.koreatech.kotrip_android.presentation.views.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.koreatech.kotrip_android.R
import com.koreatech.kotrip_android.data.model.response.HistoryDataResponseDto
import com.koreatech.kotrip_android.presentation.common.UiState
import com.koreatech.kotrip_android.presentation.components.organisms.OnboardCard
import com.koreatech.kotrip_android.presentation.components.organisms.TourHistoryTwoCard
import com.koreatech.kotrip_android.presentation.views.trip.setUpTwoGrip

@Composable
fun HistoryPage(
    uiSate: UiState,
    tourHistories: List<HistoryDataResponseDto>,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                OnboardCard(
                    title = stringResource(id = R.string.tour_history),
                    subTitle = stringResource(id = R.string.tour_history_look_forward),
                    modifier = modifier.padding(bottom = 32.dp)
                )
            }


            if (tourHistories.isNotEmpty()) {
                setUpTwoGrip(tourHistories) { one, two, _, _ ->
                    TourHistoryTwoCard(
                        one = one,
                        onClickedOne = { one?.let { onClick(one.uuid) } },
                        two = two,
                        onClickedTwo = { two?.let { onClick(two.uuid) } },
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
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