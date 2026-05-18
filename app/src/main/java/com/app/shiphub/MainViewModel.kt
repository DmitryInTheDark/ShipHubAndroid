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
        withLoading {
            if (userRepository.isUserAuthorized()) emitState(MainUIState.SkipLogin())
            else emitState(MainUIState.ShowLoginScreen())
        }
    }

}