package com.app.data.models.response

import com.app.base.models.BaseResponse
import com.app.data.models.domain.User

data class AuthResponse(
    val token: String,
    val user: User
): BaseResponse()