package com.app.shiphub.ui.main.profile

import com.app.base.BaseState
import com.app.data.models.domain.User

sealed class ProfileState : BaseState() {
    class Init(): ProfileState()
    data class Content(val user: User) : ProfileState()
    data class SuccessSave(val user: User) : ProfileState()

    class Exit(): ProfileState()
}
