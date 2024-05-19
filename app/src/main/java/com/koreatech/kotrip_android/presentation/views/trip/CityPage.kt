package com.koreatech.kotrip_android.presentation.views.trip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.koreatech.kotrip_android.R
import com.koreatech.kotrip_android.model.trip.CityInfo
import com.koreatech.kotrip_android.presentation.common.UiState
import com.koreatech.kotrip_android.presentation.components.organisms.CityTwoCard
import com.koreatech.kotrip_android.presentation.components.organisms.OnboardCard
import com.koreatech.kotrip_android.presentation.components.parts.Loading
import com.koreatech.kotrip_android.presentation.theme.Orange4d
import com.koreatech.kotrip_android.presentation.utils.cities

@Composable
fun CityPage(
    state: TripState,
    modifier: Modifier = Modifier,
    onClick: (cityInfo: CityInfo) -> Unit,
) {
    Box(modifier = modifier.fillMaxSize().background(Color.White)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                OnboardCard(
                    title = stringResource(id = R.string.onboard_select_trip_title),
                    subTitle = stringResource(id = R.string.onboard_select_trip_sub_title),
                    modifier = modifier.padding(bottom = 32.dp).background(Color.White)
                )
            }

            setUpTwoGrip(state.cities) { one, two, oneIndex, twoIndex ->
                CityTwoCard(
                    one = one,
                    oneIndex = oneIndex,
                    cities = cities,
                    onClickedOne = { cityInfo ->
                        one?.let { onClick(cityInfo) }
                    },
                    two = two,
                    twoIndex = twoIndex,
                    onClickedTwo = { cityInfo ->
                        two?.let { onClick(cityInfo) }
                    },
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }
        }

        when (state.status) {
            is UiState.Loading -> Loading()
//            is UiState.Loading -> Unit
            is UiState.Success -> Unit
            is UiState.Failed -> Unit
            else -> {}
        }
    }
}


fun <T> LazyListScope.setUpTwoGrip(
    entities: List<T>,
    row: @Composable (one: T?, two: T?, oneIndex: Int, twoIndex: Int) -> Unit,
) {
    val rowData = if (entities.count() <= 2) {
        listOf(entities)
    } else {
        entities.windowed(size = 2, step = 2, true)
    }

    rowData.forEachIndexed { index, column ->
        item {
            row(
                column.getOrNull(0),
                column.getOrNull(1),
                index * 2,
                index * 2 + 1
            )
        }
    }
}

