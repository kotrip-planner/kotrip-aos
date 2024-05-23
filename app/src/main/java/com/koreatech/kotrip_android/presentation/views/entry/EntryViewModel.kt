package com.koreatech.kotrip_android.presentation.views.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koreatech.kotrip_android.Constants
import com.koreatech.kotrip_android.api.KotripAuthApi
import com.koreatech.kotrip_android.data.DataStoreImpl
import com.koreatech.kotrip_android.presentation.common.UiState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class EntryViewModel(
    private val kotripAuthApi: KotripAuthApi,
    private val dataStoreImpl: DataStoreImpl,
) : ContainerHost<EntryState, EntrySideEffect>, ViewModel() {
    override val container: Container<EntryState, EntrySideEffect> = container(EntryState())

    fun moveToHistoryStep() {
        intent {
            reduce { state.copy(state = UiState.Success) }
            postSideEffect(EntrySideEffect.MoveToHistoryPage)
        }
    }

    fun moveToTripStep(isOneDay: Boolean) {
        intent {
            reduce { state.copy(state = UiState.Success) }
            postSideEffect(EntrySideEffect.MoveToTripPage(isOneDay))
        }
    }

    fun logout() {
        viewModelScope.launch {
            dataStoreImpl.removeAccessToken()
            dataStoreImpl.removeRefreshToken()
            intent {
                postSideEffect(EntrySideEffect.Logout)
            }
        }
    }

    fun withdraw() {
        viewModelScope.launch {
            kotripAuthApi.withdraw(
                token = "${Constants.BEARER_PREFIX} ${
                    dataStoreImpl.getAccessToken().first().toString()
                }"
            )
            intent { postSideEffect(EntrySideEffect.Withdraw) }
        }
    }
}