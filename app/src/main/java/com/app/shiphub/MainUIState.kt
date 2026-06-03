package com.app.shiphub

import com.app.base.BaseState
import com.app.data.models.enums.UserType

sealed class MainUIState: BaseState() {

    object InitScreen : MainUIState()
    class SkipLogin(val userType: UserType): MainUIState()
    object ShowLoginScreen : MainUIState()

}
