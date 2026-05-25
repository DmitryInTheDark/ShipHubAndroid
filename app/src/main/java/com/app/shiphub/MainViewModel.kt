package com.app.shiphub

import com.app.base.BaseViewModel
import com.app.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
): BaseViewModel<MainUIState>(MainUIState.InitScreen()) {

    init {
        check()
    }

    fun check(){
        withLoading {
            if (userRepository.isUserAuthorized()) emitState(MainUIState.SkipLogin())
            else emitState(MainUIState.ShowLoginScreen())
        }
    }

}