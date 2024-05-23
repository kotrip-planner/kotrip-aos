package com.koreatech.kotrip_android.presentation.composable

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.koreatech.kotrip_android.di.getActivityComposeViewModel
import com.koreatech.kotrip_android.di.getComposeViewModel
import com.koreatech.kotrip_android.model.trip.CityInfo
import com.koreatech.kotrip_android.model.trip.TourDate
import com.koreatech.kotrip_android.presentation.screen.Screen
import com.koreatech.kotrip_android.presentation.utils.showToast
import com.koreatech.kotrip_android.presentation.views.home.HomeViewModel
import com.koreatech.kotrip_android.presentation.views.trip.CityPage
import com.koreatech.kotrip_android.presentation.views.trip.SchedulePage
import com.koreatech.kotrip_android.presentation.views.trip.TripSideEffect
import com.koreatech.kotrip_android.presentation.views.trip.TripViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import timber.log.Timber


fun NavGraphBuilder.cityComposable(
    navController: NavController,
) {
    composable(
        route = Screen.Trip.City.route,
        arguments = Screen.Trip.City.navArgument
    ) { backEntry ->
        val viewModel = getComposeViewModel<TripViewModel>()
        val state by viewModel.collectAsState()
        val isOneDay = backEntry.arguments?.getBoolean(Screen.isOneDay) ?: false

        LaunchedEffect(key1 = Unit) {
            viewModel.getCity()
        }

        viewModel.collectSideEffect {
            when (it) {
                is TripSideEffect.CompletedTrip -> {
                    navController.currentBackStackEntry?.arguments?.putParcelable(Screen.cityInfo, it.cityInfo)
                    navController.navigate(
                        route = Screen.Trip.Schedule.createRoute(it.isOneDay),
                    ) {
                        launchSingleTop = true
                    }
                }

                else -> Unit
            }
        }

        CityPage(
            state = state,
        ) { cityInfo ->
            viewModel.moveToScheduleStep(isOneDay, cityInfo)
        }
    }

}

fun NavGraphBuilder.scheduleComposable(navController: NavController) {
    composable(
        route = Screen.Trip.Schedule.route,
        arguments = Screen.Trip.Schedule.navArgument
    ) { backEntry ->
        val context = LocalContext.current
        val viewModel = getComposeViewModel<TripViewModel>()
        val homeViewModel = getActivityComposeViewModel<HomeViewModel>()
        val state by viewModel.collectAsState()
        val isOneDay = backEntry.arguments?.getBoolean(Screen.isOneDay) ?: false
        val cityInfo = navController.previousBackStackEntry?.arguments?.getParcelable<CityInfo>(Screen.cityInfo)


        viewModel.collectSideEffect {
            when (it) {
                is TripSideEffect.CompletedSchedule -> {
                    homeViewModel.setData(it.cityInfo, it.tourDate)
                    navController.navigate(route = Screen.Home.createRoute(it.isOneDay)) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
                else -> Unit
            }
        }

        SchedulePage(
            context = context,
            tripState = state,
            isOneDay = isOneDay
        ) { start, end ->
            val startDate = start?.date
            val endDate = end?.date

            if (startDate != null && endDate == null && isOneDay) {
                val tourDate = TourDate(start.date, end?.date, 0)
                viewModel.moveToHomeStep(isOneDay, tourDate, cityInfo)
            } else if (startDate != null && endDate != null && !isOneDay) {
                val tourDate = TourDate(start.date, end.date)
                viewModel.moveToHomeStep(isOneDay, tourDate, cityInfo)
            } else {
                if (isOneDay) {
                    showToast(context, "하루 일정을 선택할 수 있습니다.")
                } else {
                    showToast(context, "최대 2~5일 선택할 수 있습니다.")
                }
            }
        }
    }

}