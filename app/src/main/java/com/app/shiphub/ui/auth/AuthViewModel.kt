package com.app.shiphub.ui.auth

import com.app.base.BaseViewModel
import com.app.base.SimpleStates
import com.app.data.use_cases.AuthUseCase
import com.app.base.handleError
import com.app.base.safeCall
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
) : BaseViewModel<AuthUIState>(AuthUIState.InitScreen()){

    fun login(email: String, password: String){
        withLoading {
            val result = safeCall {
                authUseCase.login(email, password)
            }
            result.onSuccess {
                _state.value = AuthUIState.SuccessLogin()
            }.onFailure {
                emitSimpleState(SimpleStates.Error(it.handleError()))
            }
        }
    }

}
