package com.app.shiphub.ui.auth

import com.app.base.BaseViewModel
import com.app.base.SimpleStates
import com.app.data.use_cases.AuthUseCase
import com.app.base.handleError
import com.app.base.safeCall
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
) : BaseViewModel<AuthUIState>(){

    fun login(email: String, password: String){
        withLoading {
            val result = safeCall {
                authUseCase.login(email, password)
            }
            result.onSuccess {
                Timber.i(it.toString())
            }.onFailure {
                _state.value = SimpleStates.Error(it.handleError())
            }
        }
    }

}
