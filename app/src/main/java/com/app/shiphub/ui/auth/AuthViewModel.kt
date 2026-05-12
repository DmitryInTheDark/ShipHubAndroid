package com.app.shiphub.ui.auth

import androidx.lifecycle.viewModelScope
import com.app.base.SimpleStates
import com.app.base.BaseViewModel
import com.app.base.models.auth.LoginRequestDTO
import com.app.base.repository.AuthRepository
import com.app.base.storage.TokenStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val tokenStorage: TokenStorage
) : BaseViewModel<AuthUIState>() {

    fun login(email: String, password: String) {
        viewModelScope.launch {
            updateState(SimpleStates.Loading(true))
            try {
                val response = repository.login(LoginRequestDTO(email, password))
                tokenStorage.saveToken(response.token)
            } catch (e: Exception) {
                updateState(SimpleStates.Error(e.message ?: "Unknown error"))
            } finally {
                updateState(SimpleStates.Loading(false))
            }
        }
    }

}
