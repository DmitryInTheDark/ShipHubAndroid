package com.app.shiphub.ui.auth.restore

import com.app.base.BaseState

sealed class RestorePasswordUIState : BaseState() {
    object InitScreen : RestorePasswordUIState()
    data class RequestSent(val email: String) : RestorePasswordUIState()
    object PasswordRestored : RestorePasswordUIState()
}
