package com.smach.zapmancer.core.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Abstract base class for ViewModels with common state management and effect emission.
 *
 * @param State The UI state type held by the ViewModel
 * @param Event External events that trigger state updates (user actions, network callbacks)
 * @param Effect Side effects to be emitted when certain conditions are met (navigation, analytics)
 */
abstract class BaseViewModel<State, Event, Effect>(
    initialState: State,
) : ViewModel() {

    /**
     * Captured initial state so [resetState] can return to the original value.
     */
    private val initial: State = initialState

    /**
     * Mutable internal state flow for updating UI.
     */
    private val _uiState = MutableStateFlow(initialState)

    /**
     * Read-only state flow exposed to the UI layer.
     */
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    /**
     * Shared flow for emitting side effects (navigation, analytics events).
     */
    private val _effect = MutableSharedFlow<Effect>()

    /**
     * Read-only effect flow exposed to the view layer.
     */
    val effect: SharedFlow<Effect> = _effect.asSharedFlow()

    /**
     * Updates the UI state using a transformation function.
     *
     * @param update A lambda that receives current state and returns updated state
     */
    protected fun updateState(update: State.() -> State) {
        _uiState.update(update)
    }

    /**
     * Sends an effect that will be processed by the view layer.
     *
     * @param effect The side effect to emit
     */
    protected fun sendEffect(effect: Effect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

    /**
     * Handles events from the UI layer. Override in subclasses to implement event handling logic.
     *
     * @param event The external event that triggered this handler
     */
    abstract fun onEvent(event: Event)

    /**
     * Resets the ViewModel state back to the captured [initial] value.
     * Useful for screen navigation or error recovery.
     */
    protected fun resetState() {
        _uiState.value = initial
    }

    /**
     * Disposes resources when the ViewModel is destroyed. Override in subclasses if needed.
     */
    override fun onCleared() {
        super.onCleared()
    }
}
