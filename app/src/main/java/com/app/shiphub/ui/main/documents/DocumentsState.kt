package com.app.shiphub.ui.main.documents

import com.app.base.BaseState

sealed class DocumentsState : BaseState(){
    class InitScreen(): DocumentsState()
}