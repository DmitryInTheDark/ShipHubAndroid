package com.app.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<S : BaseState>(initialState: S) : ViewModel() {

    protected val _state = MutableStateFlow(initialState)
    val state = _state

    protected fun setState(state: S){
        viewModelScope.launch {
            _state.emit(state)
        }
    }

    protected fun updateState(state: BaseState) {
        viewModelScope.launch {
            (state as? S)?.let { _state.emit(it) }
        }
    }

}