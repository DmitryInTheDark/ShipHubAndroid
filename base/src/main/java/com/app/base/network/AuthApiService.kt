package com.app.base.network

import com.app.base.models.auth.AuthResponse
import com.app.base.models.auth.LoginRequestDTO
import com.app.base.models.auth.RegistrationRequestDTO
import com.app.base.models.auth.VerifyCodeRequestDTO
import com.app.base.models.person.PersonDTO
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequestDTO
    ): AuthResponse

    @POST("auth/registration")
    suspend fun registration(
        @Body request: RegistrationRequestDTO
    ): Any

    @POST("auth/verify_code")
    suspend fun verifyCode(
        @Body request: VerifyCodeRequestDTO
    ): PersonDTO
}
