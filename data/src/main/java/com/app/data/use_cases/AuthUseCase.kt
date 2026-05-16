package com.app.data.use_cases

import com.app.data.UserRepository
import com.app.data.api.AuthApi
import com.app.data.models.request.AuthRequest
import com.app.data.models.domain.BaseUserInfo
import com.app.data.models.domain.LegalInfo
import com.app.data.models.domain.PhysicalInfo
import com.app.data.models.request.RegistrationRequest
import com.app.data.models.request.VerifyCodeRequest
import com.app.data.models.response.AuthResponse
import com.app.data.models.response.RegistrationResponse
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

    suspend fun registrationLegal(userInfo: BaseUserInfo, legalInfo: LegalInfo): RegistrationResponse{
        return authApi.registration(
            RegistrationRequest(
                userInfo.email,
                userInfo.password,
                userInfo.username,
                userInfo.type,
                legalInfo,
                null
            )
        )
    }

    suspend fun registrationPhysical(userInfo: BaseUserInfo, physicalInfo: PhysicalInfo): RegistrationResponse {
        return authApi.registration(
            RegistrationRequest(
                userInfo.email,
                userInfo.password,
                userInfo.username,
                userInfo.type,
                null,
                physicalInfo
            )
        )
    }

    suspend fun verifyCode(code: String, email: String): AuthResponse{
        val response = authApi.verifyCode(
            VerifyCodeRequest(code, email)
        )
        userRepository.saveUser(response.user)
        userRepository.updateJWT(response.token)
        return response
    }

}