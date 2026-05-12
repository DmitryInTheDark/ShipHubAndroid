package com.app.shiphub.ui.auth

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.app.base.BaseState
import com.app.base.BaseViewModel
import com.app.base.models.auth.LoginRequestDTO
import com.app.base.repository.AuthRepository
import com.app.base.storage.TokenStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val tokenStorage: TokenStorage
) : BaseViewModel<BaseState>(BaseState.Init()) {

    private val _loginSuccess = MutableSharedFlow<Unit>()
    val loginSuccess = _loginSuccess.asSharedFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            updateState(BaseState.Loading(true))
            try {
                val response = repository.login(LoginRequestDTO(email, password))
                tokenStorage.saveToken(response.token)
                _loginSuccess.emit(Unit)
            } catch (e: Exception) {
                updateState(BaseState.Error(e.message ?: "Unknown error"))
            } finally {
                updateState(BaseState.Loading(false))
            }
        }
    }
}
