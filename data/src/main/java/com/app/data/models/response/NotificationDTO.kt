package com.app.data.models.response

data class NotificationDTO(
    val claimId: Long,
    val lastUpdateAt: String,
    val lastUpdateText: String,
    val lastUpdateById: Long,
    val lastUpdateByName: String
)
