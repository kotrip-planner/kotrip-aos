package com.koreatech.kotrip_android.presentation.views.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import com.koreatech.kotrip_android.api.KotripApi
import com.koreatech.kotrip_android.data.DataStoreImpl
import com.koreatech.kotrip_android.data.model.request.RefreshRequestDto
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import retrofit2.Response
import timber.log.Timber

class SplashViewModel(
    private val kotripApi: KotripApi,
    private val dataStoreImpl: DataStoreImpl,
) : ContainerHost<SplashState, SplashSideEffect>, ViewModel() {
    override val container: Container<SplashState, SplashSideEffect> = container(SplashState())

    init {
        intent {
            delay(2000)
            try {
                val refresh = dataStoreImpl.getRefreshToken().firstOrNull()
                if (refresh.isNullOrEmpty()) {
                    postSideEffect(SplashSideEffect.MoveToLoginPage)
                    return@intent
                }
                val response =  kotripApi.refreshToken(RefreshRequestDto(refresh))
                if (response.code == 200) {
                    dataStoreImpl.setAccessToken(response.data.accessToken)
                    dataStoreImpl.setRefreshToken(response.data.refreshToken)
                    postSideEffect(SplashSideEffect.MoveToEntryPage)
                } else {
                    dataStoreImpl.removeAccessToken()
                    dataStoreImpl.removeRefreshToken()
                    postSideEffect(SplashSideEffect.MoveToLoginPage)
                }
            }catch (e: Exception) {
                postSideEffect(SplashSideEffect.MoveToLoginPage)
            }
        }
    }
}