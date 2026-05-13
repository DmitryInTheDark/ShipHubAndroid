package com.app.data.api

import com.app.data.models.AuthRequest
import com.app.data.models.response.AuthResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("/auth/login")
    suspend fun auth(
        @Body request: AuthRequest
    ): AuthResponse

}