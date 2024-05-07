package com.koreatech.kotrip_android.presentation.views.history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koreatech.kotrip_android.Constants
import com.koreatech.kotrip_android.api.KotripAuthApi
import com.koreatech.kotrip_android.api.ScheduleRequest
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

class HistoryViewModel(
    private val kotripAuthApi: KotripAuthApi,
    private val dataStoreImpl: DataStoreImpl,
) : ContainerHost<HistoryState, HistorySideEffect>, ViewModel() {
    override val container: Container<HistoryState, HistorySideEffect> = container(HistoryState())

    init {
        intent {
            viewModelScope.launch {
                val histories = kotripAuthApi.getHistory(
                    "${Constants.BEARER_PREFIX} ${
                        dataStoreImpl.getAccessToken().first().toString()
                    }"
                )
                Log.e("aaa", "histories : ${histories}")
                reduce {
                    state.copy(
                        state = UiState.Success,
                        histories = histories.data.history
                    )
                }
            }
        }
    }

    fun moveToOptimalPage() = intent {
        postSideEffect(HistorySideEffect.MoveToOptimalStep)
    }

    fun getHistoryTour(uuid: String) = intent {
        viewModelScope.launch {
            try {
                val routes = kotripAuthApi.getSchedule(
                    "${Constants.BEARER_PREFIX} ${
                        dataStoreImpl.getAccessToken().first().toString()
                    }", ScheduleRequest(uuid)
                )
                postSideEffect(HistorySideEffect.GetHistoryTours(routes = routes.data))
            } catch (e: Exception) {
                postSideEffect(HistorySideEffect.Toast(e.message ?: ""))
            }
        }
    }
}