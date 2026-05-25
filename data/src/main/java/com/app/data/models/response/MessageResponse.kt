package com.app.data.models.response

import kotlinx.serialization.Serializable

@Serializable
data class MessageResponse(
    val text: String,
    val senderId: Long,
    val dateCreated: String? = null
)
