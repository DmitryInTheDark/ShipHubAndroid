package com.app.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<S : BaseState> : ViewModel() {

    protected val _state: MutableStateFlow<BaseState> = MutableStateFlow(SimpleStates.Init())
    val state = _state

    protected fun setState(state: SimpleStates){
        viewModelScope.launch {
            _state.emit(state)
        }
    }

    protected fun updateState(state: SimpleStates) {
        viewModelScope.launch {
            _state.emit(state)
        }
    }

}