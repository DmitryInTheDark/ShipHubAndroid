package com.app.data.api

import com.app.data.models.request.AuthRequest
import com.app.data.models.request.RegistrationRequest
import com.app.data.models.request.RestorePasswordDTO
import com.app.data.models.request.RestorePasswordEmailRequestDTO
import com.app.data.models.request.VerifyCodeRequest
import com.app.data.models.request.VerifyRestorePasswordCodeDTO
import com.app.data.models.response.AuthResponse
import com.app.data.models.response.RegistrationResponse
import com.app.data.models.response.TokenResponse
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

interface AuthApi {

    @POST("/auth/login")
    suspend fun auth(
        @Body request: AuthRequest
    ): AuthResponse

    @POST("/auth/registration")
    suspend fun registration(
        @Body request: RegistrationRequest
    ): RegistrationResponse

    @POST("/auth/verify_code")
    suspend fun verifyCode(
        @Body request: VerifyCodeRequest
    ): AuthResponse

    @POST("/auth/restore_password/request")
    suspend fun restorePasswordRequest(
        @Body request: RestorePasswordEmailRequestDTO
    )

    @POST("/auth/restore_password")
    suspend fun verifyRestorePasswordCode(
        @Body request: VerifyRestorePasswordCodeDTO
    ): TokenResponse

    @PATCH("/auth/restore_password")
    suspend fun restorePassword(
        @Body request: RestorePasswordDTO
    ): AuthResponse

}
