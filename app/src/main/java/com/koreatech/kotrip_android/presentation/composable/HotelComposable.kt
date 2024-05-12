package com.koreatech.kotrip_android.presentation.composable

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
        val selectedHotels = viewModel.hotels.collectAsStateWithLifecycle()

        Timber.e("aaa selectedHotels : ${selectedHotels.value}")
        LaunchedEffect(key1 = Unit) {
            it.arguments?.let { arg ->
                val x = arg.getString(Screen.x)
                val y = arg.getString(Screen.y)
                val bx = arg.getString(Screen.bx)
                val by = arg.getString(Screen.by)
                hotelViewModel.getHotel(
                    x?.toDouble() ?: 0.0,
                    y?.toDouble() ?: 0.0,
                    bx?.toDouble() ?: 0.0,
                    by?.toDouble() ?: 0.0
                )
            }
        }

        val position = it.arguments?.getInt(Screen.position) ?: 0


        HotelPage(
            context = context,
            hotels = hotels.value,
            position = position,
            firstRoutes = viewModel.optimalTours[position].tours,
            secondRoutes = viewModel.optimalTours[position + 1].tours,
            paths = viewModel.naverPaths,
            onAddClick = { addHotel, addPosition ->
                viewModel.setHotels(addHotel, addPosition)
                navController.popBackStack()
            }
        )
    }
}