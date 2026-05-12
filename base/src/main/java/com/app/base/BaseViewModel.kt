package com.app.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<S : BaseState> : ViewModel() {

    protected var currentJob: Job? = null

    protected val _state: MutableStateFlow<BaseState> = MutableStateFlow(SimpleStates.Init())
    val state = _state

    protected fun setState(state: SimpleStates){
        viewModelScope.launch {
            _state.emit(state)
        }
    }

    protected fun setState(state: S) {
        viewModelScope.launch {
            _state.emit(state)
        }
    }

    protected fun withLoading(block: suspend () -> Unit) {
        setState(SimpleStates.Loading(true))
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            try {
                block()
            } finally {
                setState(SimpleStates.Loading(false))
            }
        }
    }

    fun cancelAction(){
        currentJob?.cancel()
    }
}