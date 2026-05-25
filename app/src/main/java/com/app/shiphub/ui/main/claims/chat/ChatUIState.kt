package com.app.shiphub.ui.main.claims.chat

import com.app.base.BaseHolderModel
import com.app.base.BaseState

data class ChatMessage(
    val content: String,
    val isFromMe: Boolean,
    val timestamp: Long = System.currentTimeMillis()
) : BaseHolderModel

sealed class ChatUIState : BaseState() {
    data object Init : ChatUIState()
    data class Content(
        val messages: List<ChatMessage> = emptyList(),
        val claimId: Long
    ) : ChatUIState()
    data class Error(val message: String) : ChatUIState()
}
