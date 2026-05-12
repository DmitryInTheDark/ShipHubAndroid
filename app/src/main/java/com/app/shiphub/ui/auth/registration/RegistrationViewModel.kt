package com.app.shiphub.ui.auth.registration

import com.app.base.BaseViewModel
import com.app.base.SimpleStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
) : BaseViewModel<SimpleStates>() {

    private val _registrationSuccess = MutableSharedFlow<Unit>()
    val registrationSuccess = _registrationSuccess.asSharedFlow()

    fun register() {
    }
}
