package com.app.base

sealed class SimpleStates: BaseState() {
    class Init(): SimpleStates()
    class Loading(val isLoading: Boolean): SimpleStates()
    class Error(val message: String): SimpleStates()
}