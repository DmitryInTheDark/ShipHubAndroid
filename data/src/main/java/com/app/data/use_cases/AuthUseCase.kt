package com.app.data.use_cases

import com.app.data.api.AuthApi
import com.app.data.models.AuthRequest
import com.app.data.models.response.AuthResponse
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authApi: AuthApi
) {

    suspend fun login(
        email: String,
        password: String
    ): AuthResponse {
        return authApi.auth(
            AuthRequest(email, password)
        )
    }

}