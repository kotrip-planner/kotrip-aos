package com.koreatech.kotrip_android.presentation.composable

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.koreatech.kotrip_android.di.getActivityComposeViewModel
import com.koreatech.kotrip_android.model.home.TourInfo
import com.koreatech.kotrip_android.model.trip.CityInfo
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

        if (viewModel.homeTourList.size != dateList.size) {
            viewModel.setHomeTourListSize(List(dateList.size) { mutableListOf() })
        }

        if (viewModel.homeOneDayTourList.isEmpty()) {
            viewModel.setHomeOneDayTourListSize(mutableListOf())
        }

        val tourList by remember {
            mutableStateOf(viewModel.homeTourList)
        }

        val oneDayTourList by remember {
            mutableStateOf(viewModel.homeOneDayTourList)
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

        HomePage(
            context = context,
            focusManager = focusManager,
            cityInfo = viewModel.cityInfo ?: CityInfo(),
            startDate = viewModel.tourDate?.start.toString(),
            endDate = viewModel.tourDate?.end.toString(),
            isOneDay = isOneDay,
            dateList = if (viewModel.tourDate?.end == null) emptyList() else dateList,
            tourList = tourList,
            oneDayTourList = oneDayTourList,
            oneDayTourInfo = viewModel.homeOneDayStartTourInfo,
            state = state,
            onClick = { index ->
                viewModel.moveToTourStep(index, isOneDay)
            },
            onOneDayTourClick = {
                viewModel.moveToTourStep(-2, isOneDay)
            },
            onCreateTour = {
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
                    var flag = true
                    val position = mutableListOf<Int>()
                    viewModel.tourDate?.total?.let {
                        repeat(it) { index ->
                            if (viewModel.homeTourList[index].isEmpty()) {
                                flag = false
                                position.add(index)
                            }
                        }
                    }
                    if (position.isNotEmpty()) {
                        viewModel.showToast("${position.first() + 1} 일차 관광지를 선정해주세요.")
                    }
                    if (flag) {
                        viewModel.setOptimalRoute(
                            viewModel.cityInfo?.areaId ?: 0,
                            getOptimalRouteRequestDto(
                                viewModel.tourDate?.onPrintDateRange(),
                                viewModel.homeTourList
                            )
                        )
                    }
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
        viewModel.getTour()

        val tours = mutableStateListOf<TourInfo>()
        tours.addAll(state.tours)

        viewModel.collectSideEffect {
            when (it) {
                is HomeSideEffect.Toast -> showToast(context, it.message)
                else -> Unit
            }
        }

        TourPage(
            day = day + 1,
            cityInfo = viewModel.cityInfo ?: CityInfo(),
            state = state,
            oneDayStartTourInfo = viewModel.homeOneDayStartTourInfo,
            rememberTours = if (isOneDay) viewModel.homeOneDayTourList else viewModel.homeTourList[day],
            tours = tours,
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
                            val position = tours.indexOfFirst { it.id == tourInfo.id }
                            val updatedTourInfo =
                                tours[position].copy(isSelected = !tourInfo.isSelected)
                            tours[position] = updatedTourInfo
                        }
                    }
                } else {
                    if (viewModel.homeTourList[day].filter { it.id == tourInfo.id }.isNotEmpty()) {
                        viewModel.showToast("중복되는 관광지는 선택할 수 없습니다.")
                    } else {
                        val position = tours.indexOfFirst { it.id == tourInfo.id }
                        val updatedTourInfo =
                            tours[position].copy(isSelected = !tourInfo.isSelected)
                        tours[position] = updatedTourInfo
                    }
                }

            },
            onClickTour = { selectTours ->
                if (selectTours.isEmpty()) {
                    viewModel.showToast("관광지를 하나라도 선택해야 합니다.")
                } else {
                    if (isOneDay) {
                        if (viewModel.homeOneDayTourList.size > 10 || viewModel.homeOneDayTourList.size + selectTours.size > 10) {
                            viewModel.showToast("관광지는 최대 10까지 담을 수 있습니다.")
                        } else {
                            selectTours.forEach { item ->
                                viewModel.addItemHomeOneDayTourList(item)
                            }
                            navController.popBackStack()
                        }
                    } else {
                        if (viewModel.homeTourList[day].size > 4 || viewModel.homeTourList[day].size + selectTours.size > 4) {
                            viewModel.showToast("관광지는 최대 4까지 담을 수 있습니다.")
                        } else {
                            selectTours.forEach { item ->
                                viewModel.addItemHomeTourList(day, item)
                            }
                            navController.popBackStack()
                        }
                    }
                }
            }
        )
    }
}