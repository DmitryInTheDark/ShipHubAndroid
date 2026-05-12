package com.app.base.models.auth

data class VerifyCodeRequestDTO(
    val email: String,
    val code: String
)
