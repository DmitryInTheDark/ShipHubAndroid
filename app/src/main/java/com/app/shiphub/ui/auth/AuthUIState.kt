package com.app.shiphub.ui.auth

import com.app.base.BaseState

sealed class AuthUIState: BaseState() {

    class InitScreen(): AuthUIState()
    class SuccessLogin(): AuthUIState()

}