package com.app.shiphub

import com.app.base.BaseState

sealed class MainUIState: BaseState() {

    class SkipLogin(): MainUIState()
    class ShowLoginScreen(): MainUIState()

}