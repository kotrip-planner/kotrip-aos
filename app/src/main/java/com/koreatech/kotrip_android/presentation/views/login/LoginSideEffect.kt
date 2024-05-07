package com.koreatech.kotrip_android.presentation.views.login

sealed class LoginSideEffect {
    object Completed : LoginSideEffect()
    data class Toast(
        val message: String
    ): LoginSideEffect()
}