package com.app.data.models.request

data class RestorePasswordDTO(
    val email: String,
    val token: String,
    val password: String
)
