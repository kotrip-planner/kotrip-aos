package com.koreatech.kotrip_android.presentation.composable

import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.koreatech.kotrip_android.di.getActivityComposeViewModel
import com.koreatech.kotrip_android.di.getComposeViewModel
import com.koreatech.kotrip_android.presentation.screen.Screen
import com.koreatech.kotrip_android.presentation.utils.showToast
import com.koreatech.kotrip_android.presentation.views.history.HistoryPage
import com.koreatech.kotrip_android.presentation.views.history.HistorySideEffect
import com.koreatech.kotrip_android.presentation.views.history.HistoryViewModel
import com.koreatech.kotrip_android.presentation.views.optimal.OptimalViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import timber.log.Timber

fun NavGraphBuilder.historyComposable(navController: NavController) {
    composable(route = Screen.History.route) {
        val context = LocalContext.current
        val viewModel: HistoryViewModel = getComposeViewModel()
        val optimalViewModel = getActivityComposeViewModel<OptimalViewModel>()
        val state by viewModel.collectAsState()

        viewModel.collectSideEffect {
            when (it) {
                is HistorySideEffect.GetHistoryTours -> {
                    optimalViewModel.setOptimalTours(it.routes)
                    viewModel.moveToOptimalPage()
                }

                is HistorySideEffect.MoveToOptimalStep -> {
                    navController.navigate(Screen.Optimal.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }

                is HistorySideEffect.Toast -> showToast(context, it.message)
            }
        }

        HistoryPage(
            uiSate = state.state,
            tourHistories = state.histories,
            onClick = {
                viewModel.getHistoryTour(it)
            }
        )
    }
}