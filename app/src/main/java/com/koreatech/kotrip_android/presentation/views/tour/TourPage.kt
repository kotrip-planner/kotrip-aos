package com.koreatech.kotrip_android.presentation.views.tour

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.koreatech.kotrip_android.R
import com.koreatech.kotrip_android.model.home.TourInfo
import com.koreatech.kotrip_android.model.trip.CityInfo
import com.koreatech.kotrip_android.presentation.components.organisms.OnboardCard
import com.koreatech.kotrip_android.presentation.components.organisms.TourAddDialog
import com.koreatech.kotrip_android.presentation.components.organisms.TourDayAddDialog
import com.koreatech.kotrip_android.presentation.components.organisms.TourRemoveDialog
import com.koreatech.kotrip_android.presentation.components.parts.KotripTourRow
import com.koreatech.kotrip_android.presentation.theme.Orange_FF9800
import com.koreatech.kotrip_android.presentation.views.home.HomeState
import com.koreatech.kotrip_android.presentation.views.home.HomeViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TourPage(
    viewModel: HomeViewModel,
    context: Context,
    day: Int,
    isOneDay: Boolean,
    selectedTours: List<TourInfo>,
    cityInfo: CityInfo,
    tours: LazyPagingItems<TourInfo>,
    state: HomeState,
    homeTours: List<List<TourInfo>>,
    oneDayStartTourInfo: TourInfo?,
    dayTours: List<TourInfo?> = emptyList(),
    modifier: Modifier = Modifier,
    onPop: () -> Unit,
) {
    var dialogVisible by remember {
        mutableStateOf(false)
    }
    var dialogTourInfo by remember {
        mutableStateOf<TourInfo?>(null)
    }

    var dialogRemoveVisible by remember {
        mutableStateOf(false)
    }
    var dialogRemoveTourInfo by remember {
        mutableStateOf<TourInfo?>(null)
    }
    var dialogDayVisible by remember {
        mutableStateOf(false)
    }
    var dialogDayTourInfo by remember {
        mutableStateOf<TourInfo?>(null)
    }

    val searchText by viewModel.searchText.collectAsStateWithLifecycle()

    TourAddDialog(
        context = context,
        tourInfo = dialogTourInfo,
        visible = dialogVisible,
        onDismissRequest = { dialogVisible = false },
        onButtonClick = {
            dialogVisible = false
            viewModel.onSelectedTours(it)
            if (day - 1 == -1) {
                viewModel.addOneDayItemHomeTourList(it)
            } else {
                viewModel.addItemHomeTourList(day - 1, it)
            }
        }
    )

    TourRemoveDialog(
        context = context,
        tourInfo = dialogRemoveTourInfo,
        visible = dialogRemoveVisible,
        onDismissRequest = { dialogRemoveVisible = false },
        onButtonClick = {
            viewModel.onRemoveTours(it)
            dialogRemoveVisible = false
            if (day - 1 == -1) {
                viewModel.removeOneDayItemHomeTourList(it)
            } else {
                viewModel.removeItemHomeTourList(day - 1, it)
            }
        }
    )

    TourDayAddDialog(
        context = context,
        tourInfo = dialogDayTourInfo,
        visible = dialogDayVisible,
        onDismissRequest = { dialogDayVisible = false },
        onButtonClick = { it ->
            if (selectedTours.contains(viewModel.homeOneDayStartTourInfo)) {
                viewModel.onRemoveTours(viewModel.homeOneDayStartTourInfo!!)
            }
            viewModel.onSelectedTours(it)
            dialogDayVisible = false
            viewModel.homeOneDayStartTourInfo = it
            onPop()
        }
    )


    when (tours.loadState.refresh) {
        is LoadState.Error -> {
        }

        LoadState.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "로딩중...",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        else -> {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize()
            ) {
                OnboardCard(
                    title = if (day == 0) "당일치기 일정 추가" else if (day == -1) "당일치기 출발지 선정" else "${day}일차 일정 추가",
                    subTitle = "${cityInfo.title} 여행",
                    modifier = modifier.padding(16.dp)
                )

                TextField(
                    value = searchText,
                    onValueChange = {
                        viewModel.onSearchTextChange(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
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

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    if (oneDayStartTourInfo != null) {
                        item {
                            KotripTourRow(tourInfo = oneDayStartTourInfo, index = -1)
                        }
                    }
                    itemsIndexed(dayTours) { tourPosition, tour ->
                        KotripTourRow(
                            index = -2,
                            tourPosition = tourPosition,
                            tourInfo = tour!!
                        )
                    }
                    homeTours.forEachIndexed { index, tourInfos ->
                        itemsIndexed(tourInfos) { tourPosition, tour ->
                            KotripTourRow(
                                index = index + 1,
                                tourPosition = tourPosition,
                                tourInfo = tour
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2), modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Color.White
                        )
                ) {
                    items(tours.itemCount) {
                        Card(
                            modifier = Modifier
                                .background(Color.White)
                                .padding(5.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            onClick = {
                                if (isOneDay) {
                                    if (day == -1) {
                                        if (selectedTours.contains(tours[it])) {
                                            viewModel.showToast("이미 선택된 관광지입니다.")
                                        } else {
                                            dialogDayTourInfo = tours[it]
                                            dialogDayVisible = true
                                        }
                                    } else {
                                        if (selectedTours.contains(tours[it])) {
                                            if (tours[it] == oneDayStartTourInfo) {
                                                viewModel.showToast("해당 관광지는 출발지입니다.")
                                            } else {
                                                dialogRemoveVisible = true
                                                dialogRemoveTourInfo = tours[it]
                                            }
                                        } else {
                                            dialogTourInfo = tours[it]
                                            dialogVisible = true
                                        }
                                    }
                                } else {
                                    if (selectedTours.contains(tours[it])) {
                                        dialogRemoveVisible = true
                                        dialogRemoveTourInfo = tours[it]
                                    } else {
                                        dialogTourInfo = tours[it]
                                        dialogVisible = true
                                    }
                                }
                            }
                        ) {
                            Column(
                                modifier = Modifier
                                    .background(Color.White)
                                    .padding(3.dp)
                            ) {
                                Text(
                                    text = tours[it]?.title.toString(),
                                    fontStyle = FontStyle.Normal,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Box(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(tours[it]?.imageUrl)
                                            .build(),
                                        error = painterResource(id = R.drawable.img_empty_tour),
                                        contentScale = ContentScale.Fit,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .height(100.dp)
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(16.dp))
                                            .alpha(if (selectedTours.contains(tours[it])) 0.5f else 1f)
                                    )
                                    if (selectedTours.contains(tours[it])) {
                                        Image(
                                            painter = painterResource(id = R.drawable.img_check),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(50.dp)
                                        )
                                    }
                                    homeTours.forEachIndexed { index, tourInfos ->
                                        if (tours[it] in tourInfos) {
                                            Text(
                                                text = "${index + 1} 일차",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                color = Color.Black
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun LazyListScope.setUpTourTwoGrip(
    entities: List<TourInfo>,
    row: @Composable (one: TourInfo?, two: TourInfo?, oneIndex: Int, twoIndex: Int) -> Unit,
) {
    val rowData = if (entities.count() <= 2) {
        listOf(entities)
    } else {
        entities.windowed(size = 2, step = 2, true)
    }

    rowData.forEachIndexed { index, column ->
        item() {
            row(
                column.getOrNull(0),
                column.getOrNull(1),
                index * 2,
                index * 2 + 1
            )
        }
    }
}
