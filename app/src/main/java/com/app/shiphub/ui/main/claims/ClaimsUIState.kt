package com.app.shiphub.ui.main.claims

import com.app.base.BaseState

sealed class ClaimsUIState : BaseState(){
    class InitScreen(): ClaimsUIState()
}