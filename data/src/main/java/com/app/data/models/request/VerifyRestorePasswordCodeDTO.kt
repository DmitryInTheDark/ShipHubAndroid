package com.app.data.models.request

data class VerifyRestorePasswordCodeDTO(
    val email: String,
    val code: String
)
