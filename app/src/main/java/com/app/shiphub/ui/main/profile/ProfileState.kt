package com.app.shiphub.ui.main.profile

import com.app.base.BaseState

sealed class ProfileState : BaseState(){
    class InitScreen(): ProfileState()
}