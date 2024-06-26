package com.koreatech.kotrip_android.kakao

import android.app.Activity
import android.content.Context
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import timber.log.Timber

class KakaoService(
    private val context: Context
) {
    private val kakaoLoginAssignState =
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) KAKAO_TALK
        else KAKAO_ACCOUNT

    fun initKakaoLogin(
        loginSuccess: (String) -> Unit
    ) {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Timber.tag("kakao_error").e(error)
                Log.e("aaa", "kakao error : $error")
            } else if (token != null) {
                Timber.tag("kakao_token").e(token.accessToken)
                loginSuccess(token.accessToken)
            }
        }
        Timber.tag("kakao_state").e("$kakaoLoginAssignState")


        when (kakaoLoginAssignState) {
            KAKAO_TALK -> {
                UserApiClient.instance.loginWithKakaoTalk(
                    context,
                    callback = callback
                )
            }

            KAKAO_ACCOUNT -> {
                UserApiClient.instance.loginWithKakaoAccount(
                    context,
                    callback = callback
                )
            }
        }
    }

    companion object {
        const val KAKAO_TALK = "KAKAO_TALK"
        const val KAKAO_ACCOUNT = "KAKAO_ACCOUNT"
        const val KAKAO = "KAKAO"
    }
}
