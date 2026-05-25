package com.app.shiphub.ui.main.claims.chat

import kotlinx.serialization.Serializable

@Serializable
data class ChatWebSocketMessage(
    val type: String,
    val claimId: Long? = null,
    val messageId: Long? = null,
    val senderId: Long? = null,
    val senderName: String? = null,
    val text: String? = null,
    val dateCreated: String? = null,
    val error: String? = null,
    val info: String? = null
)
