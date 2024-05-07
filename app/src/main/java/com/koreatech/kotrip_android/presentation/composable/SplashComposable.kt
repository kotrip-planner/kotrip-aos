package com.koreatech.kotrip_android.presentation.composable

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.koreatech.kotrip_android.di.getComposeViewModel
import com.koreatech.kotrip_android.presentation.screen.Screen
import com.koreatech.kotrip_android.presentation.views.splash.SplashPage
import com.koreatech.kotrip_android.presentation.views.splash.SplashSideEffect
import com.koreatech.kotrip_android.presentation.views.splash.SplashViewModel
import org.orbitmvi.orbit.compose.collectSideEffect

fun NavGraphBuilder.splashComposable(navController: NavController) {
    composable(route = Screen.Splash.route) {
        val viewModel = getComposeViewModel<SplashViewModel>()

        viewModel.collectSideEffect {
            when (it) {
                is SplashSideEffect.MoveToLoginPage -> {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }

                is SplashSideEffect.MoveToEntryPage -> {
                    navController.navigate(Screen.Entry.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            }
        }

        SplashPage()
    }
}