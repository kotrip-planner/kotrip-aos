package com.koreatech.kotrip_android.presentation.composable

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.koreatech.kotrip_android.BuildConfig
import com.koreatech.kotrip_android.R
import com.koreatech.kotrip_android.data.model.response.HotelResponseDto
import com.koreatech.kotrip_android.di.getActivityComposeViewModel
import com.koreatech.kotrip_android.presentation.screen.Screen
import com.koreatech.kotrip_android.presentation.utils.showToast
import com.koreatech.kotrip_android.presentation.views.optimal.NaverLocation
import com.koreatech.kotrip_android.presentation.views.optimal.OptimalPage
import com.koreatech.kotrip_android.presentation.views.optimal.OptimalViewModel
import org.orbitmvi.orbit.compose.collectAsState
import timber.log.Timber

fun NavGraphBuilder.optimalComposable(navController: NavController) {
    composable(route = Screen.Optimal.route) {
        var originBackPressedTime = System.currentTimeMillis()
        val context = LocalContext.current
        val viewModel = getActivityComposeViewModel<OptimalViewModel>()
        val state by viewModel.collectAsState()

        val routes = viewModel.optimalTours
        val hotels = viewModel.hotels.collectAsStateWithLifecycle()
        val hotelsMarker = remember {
            mutableStateListOf<HotelResponseDto>()
        }

        var start = ""
        var goal = ""
        var waypoints = ""
        val locations = mutableListOf<NaverLocation>()


        if (routes.isNotEmpty()) {
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
        }

        LaunchedEffect(key1 = Unit) {
            if (locations.isNotEmpty() && state.paths.isEmpty()) {
                viewModel.getNaverDriving5(
                    context.getString(R.string.naver_map_key),
                    BuildConfig.client_secret_key, locations
                )
                viewModel.initHotels(locations.size - 1)
            }
        }

        OptimalPage(
            state = state.status,
            city = viewModel.city,
            routes = routes,
            hotels = hotels.value,
            paths = state.paths,
            onBackPressed = {
                    if (System.currentTimeMillis() - originBackPressedTime > 2000) {
                        originBackPressedTime = System.currentTimeMillis()
                        showToast(context, "뒤로가기 한 번 더 누르면, 종료합니다.")
                    } else {
                        navController.popBackStack()
                    }
            },
            onLoadHotel = { x, y, bx, by, pos ->
                navController.navigate(route = Screen.Hotel.createRoute(x.toString(), y.toString(), bx.toString(), by.toString(), pos)) {
                    launchSingleTop = true
                }
            }
        )
    }
}

