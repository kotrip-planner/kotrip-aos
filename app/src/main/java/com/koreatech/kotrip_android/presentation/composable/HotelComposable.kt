package com.koreatech.kotrip_android.presentation.composable

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.koreatech.kotrip_android.di.getActivityComposeViewModel
import com.koreatech.kotrip_android.di.getComposeViewModel
import com.koreatech.kotrip_android.presentation.screen.Screen
import com.koreatech.kotrip_android.presentation.views.hotel.HotelPage
import com.koreatech.kotrip_android.presentation.views.hotel.HotelViewModel
import com.koreatech.kotrip_android.presentation.views.optimal.OptimalViewModel
import org.orbitmvi.orbit.compose.collectAsState
import timber.log.Timber

fun NavGraphBuilder.hotelComposable(navController: NavController) {
    composable(
        route = Screen.Hotel.route,
        arguments = Screen.Hotel.navArgument
    ) {
        val context = LocalContext.current
        val viewModel = getActivityComposeViewModel<OptimalViewModel>()
        val hotelViewModel = getComposeViewModel<HotelViewModel>()
        val state by viewModel.collectAsState()

        val hotels = hotelViewModel.hotels.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = Unit) {
            it.arguments?.let { arg ->
                val x = arg.getFloat(Screen.x)
                val y = arg.getFloat(Screen.y)
                hotelViewModel.getHotel(x.toDouble(), y.toDouble())
            }
        }

        val position = it.arguments?.getInt(Screen.position) ?: 0


        Timber.e("aaa position : $position")
        Timber.e("aaa tours : ${viewModel.optimalTours[position].tours}")
        HotelPage(
            context = context,
            hotels = hotels.value,
            position = position,
            firstRoutes = viewModel.optimalTours[position].tours,
            secondRoutes = viewModel.optimalTours[position + 1].tours,
            paths = viewModel.naverPaths
        )
    }
}