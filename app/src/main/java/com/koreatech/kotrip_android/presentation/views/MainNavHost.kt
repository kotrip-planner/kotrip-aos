package com.koreatech.kotrip_android.presentation.views

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.koreatech.kotrip_android.presentation.composable.cityComposable
import com.koreatech.kotrip_android.presentation.composable.entryComposable
import com.koreatech.kotrip_android.presentation.composable.historyComposable
import com.koreatech.kotrip_android.presentation.composable.homeComposable
import com.koreatech.kotrip_android.presentation.composable.hotelComposable
import com.koreatech.kotrip_android.presentation.composable.loginComposable
import com.koreatech.kotrip_android.presentation.composable.optimalComposable
import com.koreatech.kotrip_android.presentation.composable.scheduleComposable
import com.koreatech.kotrip_android.presentation.composable.splashComposable
import com.koreatech.kotrip_android.presentation.composable.tourComposable
import com.koreatech.kotrip_android.presentation.screen.Screen

@Composable
fun MainNavHost(
    navController: NavHostController,
) {
//    NavHost(navController = navController, startDestination = Screen.Login.route) {
//    NavHost(navController = navController, startDestination = Screen.Optimal.route) {
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        splashComposable(navController = navController)
        loginComposable(navController = navController)
        entryComposable(navController = navController)
        historyComposable(navController = navController)
        cityComposable(navController = navController)
        scheduleComposable(navController = navController)
        homeComposable(navController = navController)
        tourComposable(navController = navController)
        optimalComposable(navController = navController)
        hotelComposable(navController = navController)
    }
}