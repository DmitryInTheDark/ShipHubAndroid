package com.app.shiphub

import com.app.base.BaseState
import com.app.data.models.enums.UserType

sealed class MainUIState: BaseState() {

    class InitScreen(): MainUIState()
    class SkipLogin(val userType: UserType): MainUIState()
    class ShowLoginScreen(): MainUIState()

}
