package com.koreatech.kotrip_android.presentation.composable

import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.koreatech.kotrip_android.di.getComposeViewModel
import com.koreatech.kotrip_android.kakao.KakaoService
import com.koreatech.kotrip_android.presentation.screen.Screen
import com.koreatech.kotrip_android.presentation.utils.showToast
import com.koreatech.kotrip_android.presentation.views.login.LoginPage
import com.koreatech.kotrip_android.presentation.views.login.LoginSideEffect
import com.koreatech.kotrip_android.presentation.views.login.LoginViewModel
import org.orbitmvi.orbit.compose.collectSideEffect

fun NavGraphBuilder.loginComposable(navController: NavController) {
    composable(route = Screen.Login.route) {
        val context = LocalContext.current
        val viewModel = getComposeViewModel<LoginViewModel>()

        viewModel.collectSideEffect {
            when (it) {
                is LoginSideEffect.Completed -> navController.navigate(route = Screen.Entry.route)
                is LoginSideEffect.Toast -> showToast(context, it.message)
            }
        }

        val kakaoService: KakaoService = KakaoService(context)

        LoginPage {
            viewModel.kakaoLogin(kakaoService)
        }
    }
}