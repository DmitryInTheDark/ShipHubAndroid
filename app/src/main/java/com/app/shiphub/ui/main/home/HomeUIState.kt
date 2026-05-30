package com.app.shiphub.ui.main.home

import com.app.base.BaseState
import com.app.data.models.domain.User
import com.app.data.models.response.NotificationDTO

sealed class HomeUIState: BaseState() {
    class InitUserInfo(val user: User, val notifications: List<NotificationDTO>): HomeUIState()
    class ShowNotifications(val notifications: List<NotificationDTO>): HomeUIState()
    open class Init(): HomeUIState()
}