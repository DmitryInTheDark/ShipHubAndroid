package com.app.shiphub.ui.auth.email

import com.app.base.BaseViewModel
import com.app.base.safeCall
import com.app.data.use_cases.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EmailViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : BaseViewModel<EmailUIState>(EmailUIState.InitScreen()) {

    fun verifyCode(code: String, email: String) = withLoading {
        safeCall { authUseCase.verifyCode(code, email) }.handleResponse{
            _state.value = EmailUIState.EmailVerified()
        }
    }

    fun verifyRestorePasswordCode(email: String, code: String) = withLoading {
        safeCall { authUseCase.verifyRestorePasswordCode(email, code) }.handleResponse {
            _state.value = EmailUIState.RestoreCodeVerified(email, it.token)
        }
    }

}

