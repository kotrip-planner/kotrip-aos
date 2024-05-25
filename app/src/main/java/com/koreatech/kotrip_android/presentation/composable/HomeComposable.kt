package com.koreatech.kotrip_android.presentation.composable

import android.app.Activity
import androidx.compose.runtime.Composable
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
import androidx.paging.compose.collectAsLazyPagingItems
import com.koreatech.kotrip_android.di.getActivityComposeViewModel
import com.koreatech.kotrip_android.model.home.TourInfo
import com.koreatech.kotrip_android.model.trip.CityInfo
import com.koreatech.kotrip_android.presentation.screen.Screen
import com.koreatech.kotrip_android.presentation.utils.BackHandler
import com.koreatech.kotrip_android.presentation.utils.getOptimalDayRouteRequestDto
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

        val homeTours = viewModel.homeTours.collectAsStateWithLifecycle()
        if (homeTours.value.size != dateList.size) {
            viewModel.setHomeTourListSize(List(dateList.size) { mutableListOf() })
        }
        val oneDayHomeTours = viewModel.oneDayHomeTours.collectAsStateWithLifecycle()


        val selectedTours = viewModel.selectedTours.collectAsStateWithLifecycle()

        if (viewModel.homeOneDayTourList.isEmpty()) {
            viewModel.setHomeOneDayTourListSize(mutableListOf())
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
                    viewModel.clear()
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
            optimalTitleVisible = optimalTitleVisible,
            onDismissTitleVisible = {
                optimalTitleVisible = false
            },
            onTopBarButtonClick = {
                if (isOneDay) {
                    /**
                     * 당일치기 상단 여행일정 생성하기 버튼 클릭 시
                     */
                    if (viewModel.homeOneDayStartTourInfo == null) {
                        viewModel.showToast("출발지를 선정해주세요.")
                    } else if (oneDayHomeTours.value.isEmpty()) {
                        viewModel.showToast("관광지를 하나 이상 선정해주세요.")
                    } else {
                        if (oneDayHomeTours.value.size >= 9) {
                            viewModel.showToast("관광지가 10개를 초과했습니다.\n줄여주세요.")
                        } else {
                            optimalTitleVisible = true
                        }
                    }
                } else {
                    /**
                     * 당일치기가 아닌 상단 여행일정 생성하기 버튼 클릭 시
                     */
                    if (homeTours.value.any { it.isEmpty() }) {
                        viewModel.showToast("일차마다 관광지가 포함되어야 합니다.")
                    } else {
                        optimalTitleVisible = true
                    }
                }
            },
            cityInfo = viewModel.cityInfo ?: CityInfo(),
            startDate = viewModel.tourDate?.start.toString(),
            endDate = viewModel.tourDate?.end.toString(),
            isOneDay = isOneDay,
            dateList = if (viewModel.tourDate?.end == null) emptyList() else dateList,
            tourList = homeTours.value,
            selectedTours = selectedTours.value,
            oneDayTourList = oneDayHomeTours.value,
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
                    val newList = mutableListOf<TourInfo?>()
                    newList.add(viewModel.homeOneDayStartTourInfo)
                    newList.addAll(oneDayHomeTours.value)
                    viewModel.setOptimalRouteDay(
                        title = title,
                        areaId = viewModel.cityInfo?.areaId ?: 0,
                        optimalRoutes = getOptimalDayRouteRequestDto(
                            viewModel.tourDate?.start,
                            newList
                        )
                    )
                } else {
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

            },
            onHomeClick = {
                optimalViewModel.clear()
                viewModel.clear()
                navController.navigate(Screen.Entry.route) {
                    popUpTo(Screen.Home.route) {
                        inclusive = true
                    }
                }
            }
        )


        BackOnPressed()
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

        val selectedTours = viewModel.selectedTours.collectAsStateWithLifecycle()
        val homeTours = viewModel.homeTours.collectAsStateWithLifecycle()
        val oneDayHomeTours = viewModel.oneDayHomeTours.collectAsStateWithLifecycle()
        val tours = viewModel.getTestTours().collectAsLazyPagingItems()

        viewModel.clearSearchText()
        viewModel.collectSideEffect {
            when (it) {
                is HomeSideEffect.Toast -> showToast(context, it.message)
                else -> Unit
            }
        }

        TourPage(
            viewModel= viewModel,
            context = context,
            day = day + 1,
            tours = tours,
            isOneDay = isOneDay,
            selectedTours = selectedTours.value,
            homeTours = homeTours.value,
            cityInfo = viewModel.cityInfo ?: CityInfo(),
            state = state,
            oneDayStartTourInfo = viewModel.homeOneDayStartTourInfo,
            dayTours = oneDayHomeTours.value,
            onPop = {
                navController.popBackStack()
            }
        )
    }
}

@Composable
fun BackOnPressed() {
    val context = LocalContext.current
    var backPressedState by remember { mutableStateOf(true) }
    var backPressedTime = 0L

    BackHandler(enabled = backPressedState) {
        if (System.currentTimeMillis() - backPressedTime <= 2000L) {
            // 앱 종료
            (context as Activity).finish()
        } else {
            backPressedState = true
            showToast(context, "한 번 더 누르시면 앱이 종료됩니다.")
        }
        backPressedTime = System.currentTimeMillis()
    }
}

