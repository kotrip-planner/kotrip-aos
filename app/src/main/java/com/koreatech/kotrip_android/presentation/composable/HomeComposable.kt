package com.koreatech.kotrip_android.presentation.composable

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.koreatech.kotrip_android.di.getActivityComposeViewModel
import com.koreatech.kotrip_android.model.home.TourInfo
import com.koreatech.kotrip_android.model.trip.CityInfo
import com.koreatech.kotrip_android.presentation.components.organisms.TourAddDialog
import com.koreatech.kotrip_android.presentation.components.organisms.TourRemoveDialog
import com.koreatech.kotrip_android.presentation.screen.Screen
import com.koreatech.kotrip_android.presentation.utils.getOptimalRouteRequestDto
import com.koreatech.kotrip_android.presentation.utils.inRangeList
import com.koreatech.kotrip_android.presentation.utils.showToast
import com.koreatech.kotrip_android.presentation.views.home.HomePage
import com.koreatech.kotrip_android.presentation.views.home.HomeSideEffect
import com.koreatech.kotrip_android.presentation.views.home.HomeViewModel
import com.koreatech.kotrip_android.presentation.views.optimal.OptimalViewModel
import com.koreatech.kotrip_android.presentation.views.tour.TourPage
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import timber.log.Timber

fun NavGraphBuilder.homeComposable(navController: NavController) {
    composable(
        route = Screen.Home.route,
        arguments = Screen.Home.navArgument
    ) { backEntry ->
        var originBackPressedTime: Long = System.currentTimeMillis()
        val context = LocalContext.current
        val focusManager = LocalFocusManager.current
        val viewModel = getActivityComposeViewModel<HomeViewModel>()
        val optimalViewModel = getActivityComposeViewModel<OptimalViewModel>()
        val state by viewModel.collectAsState()
        val isOneDay = backEntry.arguments?.getBoolean(Screen.isOneDay) ?: false

        val dateList = if (!isOneDay) {
            val startDate = viewModel.tourDate?.start.toString()
            val endDate = viewModel.tourDate?.end?.toString() ?: ""
            startDate.inRangeList(endDate)
        } else {
            emptyList()
        }

        val homeTours = viewModel.homeTours.collectAsStateWithLifecycle()
        if (homeTours.value.size != dateList.size) {
            viewModel.setHomeTourListSize(List(dateList.size) { mutableListOf() })
        }
        Log.e("aaa", "homeTours : ${homeTours}")

//        if (viewModel.homeTourList.size != dateList.size) {
//            viewModel.setHomeTourListSize(List(dateList.size) { mutableListOf() })
//        }

        val selectedTours = viewModel.selectedTours.collectAsStateWithLifecycle()

        if (viewModel.homeOneDayTourList.isEmpty()) {
            viewModel.setHomeOneDayTourListSize(mutableListOf())
        }

        val tourList by remember {
            mutableStateOf(viewModel.homeTourList)
        }

        val oneDayTourList by remember {
            mutableStateOf(viewModel.homeOneDayTourList)
        }

        var optimalTitleVisible by remember {
            mutableStateOf(false)
        }

        viewModel.collectSideEffect {
            when (it) {
                is HomeSideEffect.MoveToTourStep -> {
                    navController.currentBackStackEntry?.arguments?.apply {
                        putParcelable(Screen.tourDate, viewModel.tourDate)
                    }
                    navController.navigate(Screen.Tour.createRoute(it.day, it.isOneDay))
                }

                is HomeSideEffect.GenerateOptimal -> {
                    viewModel.getOptimalRoute(it.uuid)
                }

                is HomeSideEffect.GetOptimalData -> {
                    optimalViewModel.setOptimalTours(it.routes)
                    viewModel.moveToOptimalPage()
                }

                is HomeSideEffect.MoveToOptimalPage -> {
                    navController.navigate(Screen.Optimal.route) {
                        popUpTo(Screen.Home.route) {
                            inclusive = true
                        }
                    }
                }

                is HomeSideEffect.Toast -> showToast(context, it.message)
            }
        }

        Log.e("aaa", "homeTours.value : ${homeTours.value}")
        HomePage(
            context = context,
            focusManager = focusManager,
            optimalTitleVisible = optimalTitleVisible,
            onDismissTitleVisible = {
                optimalTitleVisible = false
            },
            onTopBarButtonClick = {
                if (homeTours.value.any { it.isEmpty() }) {
                    viewModel.showToast("일차마다 관광지가 포함되어야 합니다.")
                } else {
                    optimalTitleVisible = true
                }
            },
            cityInfo = viewModel.cityInfo ?: CityInfo(),
            startDate = viewModel.tourDate?.start.toString(),
            endDate = viewModel.tourDate?.end.toString(),
            isOneDay = isOneDay,
            dateList = if (viewModel.tourDate?.end == null) emptyList() else dateList,
            tourList = homeTours.value,
            selectedTours = selectedTours.value,
            oneDayTourList = oneDayTourList,
            oneDayTourInfo = viewModel.homeOneDayStartTourInfo,
            state = state,
            onClick = { index ->
                viewModel.moveToTourStep(index, isOneDay)
            },
            onOneDayTourClick = {
                viewModel.moveToTourStep(-2, isOneDay)
            },
            onCreateTour = { title ->
                if (isOneDay) {
                    if (viewModel.homeOneDayStartTourInfo == null) {
                        viewModel.showToast("출발지를 선정해주세요.")
                    } else if (viewModel.homeOneDayTourList.isEmpty()) {
                        viewModel.showToast("관광지를 하나 이상 선정해주세요.")
                    } else {
                        Timber.e("aaa date : ${viewModel.tourDate?.start}")
                        Timber.e("aaa start : ${viewModel.homeOneDayStartTourInfo}")
                        Timber.e("aaa oneday list : ${viewModel.homeOneDayTourList}")
                    }
                } else {
                    Log.e("aaa", "click selectedTours : ${selectedTours.value}")
                    Log.e("aaa", "click homeTours : ${homeTours.value}")
                    var flag = false
                    // 총 관광지의 갯수가 총일차 * 4 를 넘어서는 안된다.
                    // 즉, 4일차면 16개의 관광지만 최적의 경로로 뽑을 수 있음
                    if (selectedTours.value.size <= dateList.size * 4) {
                        viewModel.setOptimalRoute(
                            title = title,
                            areaId = viewModel.cityInfo?.areaId ?: 0,
                            optimalRoutes = getOptimalRouteRequestDto(
                                viewModel.tourDate?.onPrintDateRange(),
                                homeTours.value
                            )
                        )
                    } else {
                        viewModel.showToast("총 관광지는 ${dateList.size * 4}를 넘을 수 없다.")
                    }
                }
            },
            onBackPressed = {
                if (System.currentTimeMillis() - originBackPressedTime > 2000) {
                    originBackPressedTime = System.currentTimeMillis()
                    showToast(context, "뒤로가기 한 번 더 누르면, 종료합니다.")
                } else {
                    navController.popBackStack()
                }
            }
        )
    }
}

fun NavGraphBuilder.tourComposable(navController: NavController) {
    composable(
        route = Screen.Tour.route,
        arguments = Screen.Tour.navArgument
    ) { backEntry ->
        val context = LocalContext.current
        val viewModel = getActivityComposeViewModel<HomeViewModel>()
        val state by viewModel.collectAsState()
        val isOneDay = backEntry.arguments?.getBoolean(Screen.isOneDay) ?: false
        val day = backEntry.arguments?.getInt(Screen.day) ?: 0

        val tours = viewModel.tours.collectAsStateWithLifecycle()
        LaunchedEffect(key1 = Unit) {
            if (tours.value.isEmpty()) {
                viewModel.getTour()
            }
        }

        val searchText by viewModel.searchText.collectAsStateWithLifecycle()
        val selectedTours = viewModel.selectedTours.collectAsStateWithLifecycle()
        val homeTours = viewModel.homeTours.collectAsStateWithLifecycle()

        Timber.e("aaa tour homeTours : ${homeTours.value}")

        var selectedId by remember {
            mutableStateOf(-1)
        }
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

        viewModel.collectSideEffect {
            when (it) {
                is HomeSideEffect.Toast -> showToast(context, it.message)
                else -> Unit
            }
        }

        TourAddDialog(
            context = context,
            tourInfo = dialogTourInfo,
            visible = dialogVisible,
            onDismissRequest = { dialogVisible = false },
            onButtonClick = { it ->
                viewModel.onSelectedTours(it)
                dialogVisible = false
                viewModel.addItemHomeTourList(day, it)
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
                viewModel.removeItemHomeTourList(day, it)
            }
        )

        TourPage(
            day = day + 1,
            selectedId = selectedId,
            onSelectedIdChanged = { tourInfo, id ->
//                selectedId = id
//                if (id != -1) {
//                    dialogTourInfo = tourInfo
//                    dialogVisible = true
//                }
            },
            selectedTours = selectedTours.value,
            searchText = searchText,
            homeTours = homeTours.value,
            onSearchTextChanged = viewModel::onSearchTextChange,
            cityInfo = viewModel.cityInfo ?: CityInfo(),
            state = state,
            oneDayStartTourInfo = viewModel.homeOneDayStartTourInfo,
            rememberTours = if (isOneDay) viewModel.homeOneDayTourList else selectedTours.value,
            tours = tours.value,
            onClick = { tourInfo ->
                if (isOneDay) {
                    if (day == -2) {
                        if (viewModel.homeOneDayStartTourInfo?.id == tourInfo.id ||
                            viewModel.homeOneDayTourList.filter { it.id == tourInfo.id }
                                .isNotEmpty()
                        ) {
                            viewModel.showToast("중복되는 관광지는 선택할 수 없습니다.")
                        } else {
                            viewModel.startPositionChecked = true
                            viewModel.homeOneDayStartTourInfo = tourInfo
                            navController.popBackStack()
                        }
                    } else {
                        if (viewModel.homeOneDayTourList.filter { it.id == tourInfo.id }
                                .isNotEmpty() || viewModel.homeOneDayStartTourInfo?.id == tourInfo.id) {
                            viewModel.showToast("중복되는 관광지는 선택할 수 없습니다.")
                        } else {
                            val position = tours.value.indexOfFirst { it.id == tourInfo.id }
                            val updatedTourInfo =
                                tours.value[position].copy(isSelected = !tourInfo.isSelected)
                            viewModel.updateToursSelection(position, updatedTourInfo)
                        }
                    }
                } else {
                    if (selectedTours.value.contains(tourInfo)) {
//                        viewModel.showToast("중복되는 관광지는 삭제해야 합니다.")
                        dialogRemoveVisible = true
                        dialogRemoveTourInfo = tourInfo
                    } else {
                        dialogTourInfo = tourInfo
                        dialogVisible = true
//                        viewModel.showToast("관광지 선택 가능")
                    }
                }

            },
            onClickTour = { selectTours ->
                if (selectTours.isEmpty()) {
                } else {
                }
            }
        )
    }
}