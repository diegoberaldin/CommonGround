package com.github.diegoberaldin.commonground.core.architecture

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface MviModel<Intent, State, Event> {

    val uiState: StateFlow<State>
    val events: SharedFlow<Event>

    fun accept(intent: Intent)

    @Composable
    fun BindToLifecycle()
}