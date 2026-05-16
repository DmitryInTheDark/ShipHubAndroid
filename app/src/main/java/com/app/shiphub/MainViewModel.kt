package com.app.shiphub

import com.app.base.BaseViewModel
import com.app.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    userRepository: UserRepository
): BaseViewModel<MainUIState>(MainUIState.InitScreen()) {

    init {
        if (userRepository.isUserAuthorized()) _state.value = MainUIState.SkipLogin()
        else _state.value = MainUIState.ShowLoginScreen()
    }

}