package com.app.shiphub.ui.auth.registration

import com.app.base.BaseState

sealed class RegistrationUIState : BaseState(){

    class InitScreen(): RegistrationUIState()
    class SuccessRegistration(val email: String): RegistrationUIState()

}