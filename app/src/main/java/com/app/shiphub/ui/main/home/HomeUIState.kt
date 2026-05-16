package com.app.shiphub.ui.main.home

import com.app.base.BaseState
import com.app.data.models.domain.User

sealed class HomeUIState: BaseState() {
    class InitUserInfo(val user: User, val notifications: List<String>): HomeUIState()
    class ShowNotifications(val notifications: List<String>): HomeUIState()
}