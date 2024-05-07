package com.koreatech.kotrip_android.presentation.composable

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.koreatech.kotrip_android.BuildConfig
import com.koreatech.kotrip_android.R
import com.koreatech.kotrip_android.di.getActivityComposeViewModel
import com.koreatech.kotrip_android.presentation.screen.Screen
import com.koreatech.kotrip_android.presentation.views.optimal.NaverLocation
import com.koreatech.kotrip_android.presentation.views.optimal.OptimalPage
import com.koreatech.kotrip_android.presentation.views.optimal.OptimalViewModel
import org.orbitmvi.orbit.compose.collectAsState
import timber.log.Timber

fun NavGraphBuilder.optimalComposable(navController: NavController) {
    composable(route = Screen.Optimal.route) {
        val context = LocalContext.current
        val viewModel = getActivityComposeViewModel<OptimalViewModel>()
        val state by viewModel.collectAsState()

        val routes = viewModel.optimalTours

        var start = ""
        var goal = ""
        var waypoints = ""
        val locations = mutableListOf<NaverLocation>()

        routes.forEach { route ->
            route.tours.forEachIndexed { index, item ->
                if (index == 0) start = "${item.longitude},${item.latitude}"
                else if (index == route.tours.size - 1) goal = "${item.longitude},${item.latitude}"
                else {
                    if (waypoints == "") {
                        waypoints += "${item.longitude},${item.latitude}"
                    } else waypoints += "|${item.longitude},${item.latitude}"
                }
            }
            locations.add(NaverLocation(start, goal, waypoints))
            waypoints = ""
        }

        LaunchedEffect(key1 = Unit) {
            viewModel.getNaverDriving5(
                context.getString(R.string.naver_map_key),
                BuildConfig.client_secret_key, locations
            )
        }

        OptimalPage(
            state = state.status,
            city = viewModel.city,
            routes = routes,
            paths = state.paths,
            onLoadHotel = { x, y, pos ->
                navController.navigate(route = Screen.Hotel.createRoute(x, y, pos)) {
                    launchSingleTop = true
                }
            }
        )
    }
}

