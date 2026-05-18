package com.app.shiphub.ui.main.claims.claim

import com.app.base.BaseState
import com.app.data.models.domain.Claim

sealed class ClaimUIState : BaseState(){
    class Init(): ClaimUIState()
    class InitScreen(val claim: Claim, val notifications: List<String>): ClaimUIState()
}