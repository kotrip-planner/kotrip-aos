package com.koreatech.kotrip_android.presentation.views.splash

sealed class SplashSideEffect {
    data object MoveToLoginPage: SplashSideEffect()
    data object MoveToEntryPage: SplashSideEffect()
}