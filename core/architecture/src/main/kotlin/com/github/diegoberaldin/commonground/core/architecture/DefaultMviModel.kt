package com.github.diegoberaldin.commonground.core.architecture

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class DefaultMviModel<Intent, State, Event>(
    initial: State
) : MviModel<Intent, State, Event>, ViewModel() {
    override val uiState = MutableStateFlow(initial)
    override val events = MutableSharedFlow<Event>()

    override fun accept(intent: Intent) {
        reduce(intent)
    }

    protected abstract fun reduce(intent: Intent)

    protected fun emit(event: Event) {
        viewModelScope.launch {
            events.emit(event)
        }
    }

    protected fun updateState(block: (State) -> State) {
        uiState.update(block)
    }

    protected open fun onCreate() {}
    protected open fun onStart() {}
    protected open fun onResume() {}
    protected open fun onPause() {}
    protected open fun onStop() {}
    protected open fun onDestroy() {}

    @Composable
    override fun BindToLifecycle() {
        val lifecycleOwner = LocalLifecycleOwner.current
        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> onCreate()
                    Lifecycle.Event.ON_START -> onStart()
                    Lifecycle.Event.ON_RESUME -> onResume()
                    Lifecycle.Event.ON_PAUSE -> onPause()
                    Lifecycle.Event.ON_STOP -> onStop()
                    Lifecycle.Event.ON_DESTROY -> onDestroy()
                    else -> Unit
                }
            }

            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    }
}