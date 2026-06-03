package com.app.shiphub.ui.auth.restore

import com.app.base.BaseViewModel
import com.app.base.safeCall
import com.app.data.models.request.RestorePasswordDTO
import com.app.data.use_cases.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RestorePasswordViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : BaseViewModel<RestorePasswordUIState>(RestorePasswordUIState.InitScreen) {

    fun requestRestorePassword(email: String) = withLoading {
        safeCall { authUseCase.restorePasswordRequest(email) }.handleResponse {
            _state.value = RestorePasswordUIState.RequestSent(email)
        }
    }

    fun restorePassword(dto: RestorePasswordDTO) = withLoading {
        safeCall { authUseCase.restorePassword(dto) }.handleResponse {
            _state.value = RestorePasswordUIState.PasswordRestored
        }
    }
}
