package com.app.shiphub.ui.auth.registration

import androidx.lifecycle.viewModelScope
import com.app.base.SimpleStates
import com.app.base.BaseViewModel
import com.app.base.models.auth.RegistrationRequestDTO
import com.app.base.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val repository: AuthRepository
) : BaseViewModel<SimpleStates>() {

    private val _registrationSuccess = MutableSharedFlow<Unit>()
    val registrationSuccess = _registrationSuccess.asSharedFlow()

    fun register(request: RegistrationRequestDTO) {
        viewModelScope.launch {
            updateState(SimpleStates.Loading(true))
            try {
                repository.registration(request)
                _registrationSuccess.emit(Unit)
            } catch (e: Exception) {
                updateState(SimpleStates.Error(e.message ?: "Unknown error"))
            } finally {
                updateState(SimpleStates.Loading(false))
            }
        }
    }
}
