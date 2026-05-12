package com.app.shiphub.ui.auth.email

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.app.base.BaseState
import com.app.base.BaseViewModel
import com.app.base.models.auth.RegistrationRequestDTO
import com.app.base.models.auth.VerifyCodeRequestDTO
import com.app.base.repository.AuthRepository
import com.app.base.storage.TokenStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val tokenStorage: TokenStorage
) : BaseViewModel<BaseState>(BaseState.Init()) {

    private val _verifySuccess = MutableSharedFlow<Unit>()
    val verifySuccess = _verifySuccess.asSharedFlow()

    fun verifyCode(email: String, code: String) {
        viewModelScope.launch {
            updateState(BaseState.Loading(true))
            try {
                val response = repository.verifyCode(VerifyCodeRequestDTO(email, code))
                _verifySuccess.emit(Unit)
            } catch (e: Exception) {
                updateState(BaseState.Error(e.message ?: "Unknown error"))
            } finally {
                updateState(BaseState.Loading(false))
            }
        }
    }

    fun resendCode(request: RegistrationRequestDTO) {
        viewModelScope.launch {
            updateState(BaseState.Loading(true))
            try {
                repository.registration(request)
            } catch (e: Exception) {
                updateState(BaseState.Error(e.message ?: "Unknown error"))
            } finally {
                updateState(BaseState.Loading(false))
            }
        }
    }
}
