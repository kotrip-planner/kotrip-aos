package com.koreatech.kotrip_android.presentation.views.entry

import androidx.lifecycle.ViewModel
import com.koreatech.kotrip_android.presentation.common.UiState
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class EntryViewModel : ContainerHost<EntryState, EntrySideEffect>, ViewModel() {
    override val container: Container<EntryState, EntrySideEffect>
        = container(EntryState())

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
}