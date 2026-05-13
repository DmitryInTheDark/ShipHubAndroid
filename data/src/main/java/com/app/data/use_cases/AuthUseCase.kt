package com.app.data.use_cases

import com.app.data.UserRepository
import com.app.data.api.AuthApi
import com.app.data.models.AuthRequest
import com.app.data.models.response.AuthResponse
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authApi: AuthApi,
    private val userRepository: UserRepository
) {

    suspend fun login(
        email: String,
        password: String
    ): AuthResponse {
        val response = authApi.auth(
            AuthRequest(email, password)
        )
        userRepository.saveUser(response.user)
        userRepository.updateJWT(response.token)
        return response
    }

}