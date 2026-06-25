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
 * Abstract base class for ViewModels with common state management and error handling.
 * 
 * @param State The UI state type held by the ViewModel
 * @param Event External events that trigger state updates (user actions, network callbacks)
 * @param Effect Side effects to be emitted when certain conditions are met (navigation, analytics)
 */
abstract class BaseViewModel<State, Event, Effect>(
    initialState: State,
) : ViewModel() {

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
        viewModelScope.launch {
            _uiState.update { currentState ->
                try {
                    val newState = currentState.update()
                    
                    // Log state change for debugging in production builds
                    if (BuildConfig.DEBUG) {
                        println("ViewModel state updated to: $newState")
                    }
                    
                    newState
                } catch (e: Exception) {
                    e.printStackTrace()
                    throw e
                }
            }
        }
    }

    /**
     * Sends an effect that will be processed by the view layer.
     * 
     * @param effect The side effect to emit
     */
    protected fun sendEffect(effect: Effect) {
        viewModelScope.launch {
            try {
                _effect.emit(effect)
            } catch (e: Exception) {
                e.printStackTrace()
                // In production, consider logging via analytics service instead of printStackTrace
            }
        }
    }

    /**
     * Handles events from the UI layer. Override in subclasses to implement event handling logic.
     * 
     * @param event The external event that triggered this handler
     */
    abstract fun onEvent(event: Event)

    /**
     * Common error handling utility for network operations and other async tasks.
     * Can be overridden or extended by child ViewModels as needed.
     */
    protected suspend inline fun <T> handleWithRetry(
        operation: () -> T, 
        maxRetries: Int = 3, 
        retryDelayMs: Long = 1000L
    ): Result<T> {
        
        var attempt = 0
        
        while (attempt <= maxRetries) {
            try {
                return Success(operation())
            } catch (e: Exception) {
                if (attempt < maxRetries) {
                    kotlinx.coroutines.delay(retryDelayMs * (2.toLong() ** attempt)) // Exponential backoff
                } else {
                    throw e
                }
            } finally {
                attempt++
            }
        }
        
        return Result.failure(Exception("Operation failed after $maxRetries attempts"))
    }

    /**
     * Helper class for representing operation results.
     */
    sealed interface OperationResult<out T> {
        data class Success<T>(val value: T) : OperationResult<T>
        data class Failure(val error: Exception, val attemptNumber: Int = 0) : OperationResult<Unit>
        
        companion object {
            fun <T> success(value: T): Result<T> = Result.success(OperationResult.Success(value))
            
            fun failure(error: Exception, attemptNumber: Int = 0): Result<Unit> = 
                Result.failure(Exception("Operation failed after $attemptNumber attempts"))
        }
    }

    /**
     * Resets the ViewModel state to initial values. Useful for screen navigation or error recovery.
     */
    protected fun resetState() {
        viewModelScope.launch {
            _uiState.value = initialState.copy(
                isLoading = false, 
                error = null,
                // Add other fields that should be reset here
            )
        }
    }

    /**
     * Disposes resources when the ViewModel is destroyed. Override in subclasses if needed.
     */
    override fun onCleared() {
        super.onCleared()
        // Clean up any subscriptions, timers, or other resources here
        println("ViewModel cleared: ${this::class.simpleName}")
    }

    /**
     * Validates input data before processing operations. Override in subclasses for specific validation rules.
     */
    protected open fun validateInput(data: Any?): Boolean {
        return data != null && !data.toString().isBlank()
    }
}
