package com.app.shiphub.ui.auth

import com.app.base.BaseState

sealed class AuthUIState: BaseState() {

    class State1(): AuthUIState()
    class State2(): AuthUIState()
    class State3(): AuthUIState()
    class State4(): AuthUIState()

}