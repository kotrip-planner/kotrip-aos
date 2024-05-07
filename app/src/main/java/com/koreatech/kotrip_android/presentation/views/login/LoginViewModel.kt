package com.koreatech.kotrip_android.presentation.views.login

import androidx.lifecycle.ViewModel
import com.koreatech.kotrip_android.api.KotripApi
import com.koreatech.kotrip_android.data.DataStoreImpl
import com.koreatech.kotrip_android.data.model.request.LoginRequestDto
import com.koreatech.kotrip_android.data.model.response.AuthResponseDto
import com.koreatech.kotrip_android.kakao.KakaoService
import com.koreatech.kotrip_android.presentation.common.UiState
import kotlinx.coroutines.Job
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

class LoginViewModel(
    private val kakaoService: KakaoService,
    private val dataStoreImpl: DataStoreImpl,
    private val kotripApi: KotripApi,
) : ContainerHost<LoginState, LoginSideEffect>, ViewModel() {
    override val container: Container<LoginState, LoginSideEffect> = container(LoginState())

    fun kakaoLogin() {
        intent {
            reduce { state.copy(status = UiState.Loading) }

            kakaoService.initKakaoLogin { token ->
                intent {
                    if (token.isEmpty()) {
                        reduce { state.copy(status = UiState.Failed) }
                        postSideEffect(sideEffect = LoginSideEffect.Toast("token is not validation"))
                    } else {
                        runCatching {
                            kotripApi.login(LoginRequestDto(token))
                        }.onSuccess {
                            dataStoreImpl.setAccessToken(it.data.accessToken)
                            dataStoreImpl.setRefreshToken(it.data.refreshToken)
                            reduce { state.copy(status = UiState.Success) }
                            postSideEffect(sideEffect = LoginSideEffect.Completed)
                        }.onFailure {
                            reduce { state.copy(status = UiState.Failed) }
                            Timber.e("aaa error :${it.message}")
                            postSideEffect(sideEffect = LoginSideEffect.Toast(it.message ?: ""))
                        }
                    }
                }
            }

        }
    }
}