package com.app.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<S : BaseState>(
    initialState: S
) : ViewModel() {

    protected var currentJob: Job? = null

    protected val _simpleState = MutableStateFlow<SimpleStates>(SimpleStates.Init())
    val simpleState = _simpleState
    protected val _state: MutableStateFlow<S> = MutableStateFlow(initialState)
    val state = _state

    protected suspend fun emitState(state: S){
        _state.emit(state)
    }

    protected suspend fun emitSimpleState(state: SimpleStates){
        _simpleState.emit(state)
    }

    protected fun withLoading(block: suspend () -> Unit) {
        viewModelScope.launch {
            emitSimpleState(SimpleStates.Loading(true))
            try {
                block()
            } finally {
                emitSimpleState(SimpleStates.Loading(false))
            }
        }
    }

    protected fun withLoading(loadingState: suspend (isLoading: Boolean) -> Unit, block: suspend () -> Unit) {
        viewModelScope.launch {
            loadingState.invoke(true)
            try {
                block()
            } finally {
                loadingState.invoke(false)
            }
        }
    }

    protected inline fun <T> Result<T>.handleResponse(action: (T) -> Unit){
        onSuccess { action.invoke(it) }
        onFailure { _simpleState.value = SimpleStates.Error(it.handleError()) }
    }

//    protected inline fun <T> Result<T>.handleResponse(errorAction: () -> Unit, action: (T) -> Unit){
//        onSuccess { action.invoke(it) }
//        onFailure { errorAction.invoke() }
//    }

    fun cancelAction(){
        currentJob?.cancel()
    }

}