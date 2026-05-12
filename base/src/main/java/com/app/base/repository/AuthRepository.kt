package com.app.base.repository

import com.app.base.di.UnauthorizedClient
import com.app.base.models.auth.AuthResponse
import com.app.base.models.auth.LoginRequestDTO
import com.app.base.models.auth.RegistrationRequestDTO
import com.app.base.models.auth.VerifyCodeRequestDTO
import com.app.base.models.person.PersonDTO
import com.app.base.network.AuthApiService
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    @UnauthorizedClient private val retrofit: Retrofit
) {
    private val api = retrofit.create(AuthApiService::class.java)

    suspend fun login(request: LoginRequestDTO): AuthResponse {
        return api.login(request)
    }

    suspend fun registration(request: RegistrationRequestDTO): Any {
        return api.registration(request)
    }

    suspend fun verifyCode(request: VerifyCodeRequestDTO): PersonDTO {
        return api.verifyCode(request)
    }
}
