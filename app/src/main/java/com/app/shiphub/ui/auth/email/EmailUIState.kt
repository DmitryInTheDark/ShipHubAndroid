package com.app.shiphub.ui.auth.email

import com.app.base.BaseState


sealed class EmailUIState: BaseState() {

    class EmailVerified(): EmailUIState()

}