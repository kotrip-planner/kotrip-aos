package com.koreatech.kotrip_android.presentation.composable

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.koreatech.kotrip_android.di.getComposeViewModel
import com.koreatech.kotrip_android.presentation.screen.Screen
import com.koreatech.kotrip_android.presentation.views.entry.EntryPage
import com.koreatech.kotrip_android.presentation.views.entry.EntrySideEffect
import com.koreatech.kotrip_android.presentation.views.entry.EntryViewModel
import org.orbitmvi.orbit.compose.collectSideEffect

fun NavGraphBuilder.entryComposable(navController: NavController) {
    composable(route = Screen.Entry.route) {
        val viewModel = getComposeViewModel<EntryViewModel>()

        viewModel.collectSideEffect {
            when (it) {
                is EntrySideEffect.MoveToHistoryPage -> navController.navigate(route = Screen.History.route)
                is EntrySideEffect.MoveToTripPage -> navController.navigate(
                    route = Screen.Trip.City.createRoute(it.isOneDay)
                ) {
                    launchSingleTop = true
                }

                EntrySideEffect.Logout -> {
                    navController.popBackStack()
                    navController.navigate(
                        route= Screen.Login.route
                    )
                }

                EntrySideEffect.Withdraw -> {
                    navController.popBackStack()
                    navController.navigate(
                        route= Screen.Login.route
                    )
                }
            }
        }

        EntryPage(
            onClickHistoryStep = { viewModel.moveToHistoryStep() },
            onClickScheduleStep = { viewModel.moveToTripStep(false) },
            onClickOneDayStep = { viewModel.moveToTripStep(true) },
            onWithdraw = { viewModel.withdraw() },
            onLogout = { viewModel.logout() }
        )
    }

}