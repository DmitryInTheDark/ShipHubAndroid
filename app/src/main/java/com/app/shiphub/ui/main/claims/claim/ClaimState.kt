package com.app.shiphub.ui.main.claims.claim

import com.app.base.BaseState

sealed class ClaimState : BaseState(){
    class InitScreen(): ClaimState()
}