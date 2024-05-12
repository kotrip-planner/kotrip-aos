package com.koreatech.kotrip_android.presentation.screen

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val navArgument: List<NamedNavArgument> = emptyList(),
) {
    data object Splash : Screen(route = "splash")
    data object Login : Screen(route = "login")
    data object Entry : Screen(route = "entry")
    data object History : Screen(route = "history")

    object Trip {
        data object City : Screen(
            route = "trip/{$isOneDay}",
            navArgument = listOf(
                navArgument(isOneDay) { type = NavType.BoolType }
            )
        ) {
            fun createRoute(isOneDay: Boolean) = "trip/$isOneDay"
        }

        data object Schedule : Screen(
            route = "schedule/{$isOneDay}",
            navArgument = listOf(
                navArgument(isOneDay) { type = NavType.BoolType },
            )
        ) {
            fun createRoute(isOneDay: Boolean) = "schedule/$isOneDay"
        }
    }

    data object Home : Screen(
        route = "home/{$isOneDay}",
        navArgument = listOf(
            navArgument(isOneDay) { type = NavType.BoolType }
        )
    ) {
        fun createRoute(isOneDay: Boolean) =
            "home/$isOneDay"
    }

    data object Tour : Screen(
        route = "tour/{$day}/{$isOneDay}",
        navArgument = listOf(
            navArgument(day) { type = NavType.IntType },
            navArgument(isOneDay) { type = NavType.BoolType },
        )
    ) {
        fun createRoute(day: Int?, isOneDay: Boolean) =
            "tour/$day/$isOneDay"
    }

    data object Optimal : Screen(
        route = "optimal"
    )

    data object Hotel : Screen(
        route = "hotel/{$x}/{$y}/{$bx}/{$by}/{$position}",
        navArgument = listOf(
            navArgument(x) { type = NavType.StringType },
            navArgument(y) { type = NavType.StringType },
            navArgument(bx) { type = NavType.StringType },
            navArgument(by) { type = NavType.StringType },
            navArgument(position) { type = NavType.IntType },
        )
    ) {
        fun createRoute(x: String, y: String, bx: String, by: String, position: Int) =
            "hotel/$x/$y/$bx/$by/$position"
    }

    companion object {
        const val isOneDay = "isOneDay"
        const val cityInfo = "cityInfo"
        const val tourDate = "tourDate"
        const val day = "day"
        const val x = "x"
        const val y = "y"
        const val bx = "bx"
        const val by = "by"
        const val position = "position"
    }
}