package com.app.base

sealed class BaseStates {

    public class Init(): BaseStates()
    public class Loading(val isLoading: Boolean): BaseStates()
    public class Error(val message: String): BaseStates()
}