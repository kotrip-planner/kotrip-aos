package com.koreatech.kotrip_android.presentation.views.tour

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.koreatech.kotrip_android.model.home.TourInfo
import com.koreatech.kotrip_android.model.trip.CityInfo
import com.koreatech.kotrip_android.presentation.components.organisms.OnboardCard
import com.koreatech.kotrip_android.presentation.components.organisms.TourTwoCard
import com.koreatech.kotrip_android.presentation.components.parts.KotripTourRow
import com.koreatech.kotrip_android.presentation.theme.Orange_FF9800
import com.koreatech.kotrip_android.presentation.theme.Orange_FFCD4C
import com.koreatech.kotrip_android.presentation.views.home.HomeState
import com.koreatech.kotrip_android.presentation.views.trip.setUpTwoGrip

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TourPage(
    day: Int,
    selectedId: Int,
    onSelectedIdChanged: (tourInfo: TourInfo, id: Int) -> Unit,
    selectedTours: List<TourInfo>,
    searchText: String,
    onSearchTextChanged: (text: String) -> Unit,
    cityInfo: CityInfo,
    state: HomeState,
    homeTours: List<List<TourInfo>>,
    oneDayStartTourInfo: TourInfo?,
    rememberTours: List<TourInfo>,
    tours: List<TourInfo>,
    modifier: Modifier = Modifier,
    onClick: (tourInfo: TourInfo) -> Unit,
    onClickTour: (List<TourInfo>) -> Unit,
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                OnboardCard(
                    title = if (day == 0) "당일치기 일정 추가" else if (day == -1) "당일치기 출발지 선정" else "${day}일차 일정 추가",
                    subTitle = "${cityInfo.title} 여행",
                    modifier = modifier.padding(bottom = 32.dp)
                )
            }

            stickyHeader {
                TextField(
                    value = searchText,
                    onValueChange = onSearchTextChanged,
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedIndicatorColor = Color.Black,
                        focusedIndicatorColor = Color.Gray,
                        cursorColor = Orange_FF9800
                    ),
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    },
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (oneDayStartTourInfo != null) {
                        item {
                            KotripTourRow(tourInfo = oneDayStartTourInfo)
                        }
                    }
                    homeTours.forEachIndexed { index, tourInfos ->
                        itemsIndexed(tourInfos) { tourPosition, tour ->
                            KotripTourRow(index = index + 1, tourPosition = tourPosition, tourInfo = tour)
                        }
                    }
//                    items(rememberTours) { it ->
//                        KotripTourRow(tourInfo = it)
//                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            setUpTwoGrip(tours) { one, two, _, _ ->
                TourTwoCard(
                    selectedId = selectedId,
                    onSelectedIdChanged = onSelectedIdChanged,
                    homeTours = homeTours,
                    selectedTours = selectedTours,
                    one = one,
                    onClickedOne = { tourInfo ->
                        one?.let { onClick(tourInfo) }
                    },
                    two = two,
                    onClickedTwo = { tourInfo ->
                        two?.let { onClick(tourInfo) }
                    },
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }
        }
    }
}
