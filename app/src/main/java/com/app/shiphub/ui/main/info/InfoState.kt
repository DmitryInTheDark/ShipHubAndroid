package com.app.shiphub.ui.main.info

import com.app.base.BaseState

sealed class InfoState : BaseState(){
    class InitScreen(): InfoState()
}