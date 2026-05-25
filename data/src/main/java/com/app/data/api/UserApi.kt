package com.app.data.api

import com.app.data.models.domain.User
import com.app.data.models.request.UpdateUserRequest
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {

    @PUT("/users/{id}")
    suspend fun updateUser(
        @Path("id") id: Long,
        @Body request: UpdateUserRequest
    ): User

}
