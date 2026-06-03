package com.app.shiphub

import com.app.base.BaseViewModel
import com.app.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
): BaseViewModel<MainUIState>(MainUIState.InitScreen) {

    init {
        check()
    }

    fun check(){
        withLoading {
            if (userRepository.isUserAuthorized()) {
                Timber.i("isAuthorized")
                val user = userRepository.getUser()
                emitState(MainUIState.SkipLogin(user.type))
            }
            else {
                emitState(MainUIState.ShowLoginScreen)
            }
        }
    }

}
