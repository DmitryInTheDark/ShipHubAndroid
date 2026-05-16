package com.app.data.models.request

data class VerifyCodeRequest(
    val code: String,
    val email: String
)