package com.app.data.models.domain

import com.app.data.models.enums.UserType

data class BaseUserInfo(
    val username: String,
    val email: String,
    val type: UserType,
    val password: String
)