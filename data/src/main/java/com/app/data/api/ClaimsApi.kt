package com.app.data.api

import com.app.base.models.BaseListResponse
import com.app.data.models.domain.Claim
import retrofit2.http.GET
import retrofit2.http.Query

interface ClaimsApi {

    @GET("/claims")
    suspend fun getClaims(
        @Query("page_number") pageNumber: Int = 0,
        @Query("page_size") pageSize: Int = 20
    ): BaseListResponse<Claim>

    @GET("/claims/active")
    suspend fun getActiveClaims(
        @Query("page_number") pageNumber: Int = 0,
        @Query("page_size") pageSize: Int = 20
    ): BaseListResponse<Claim>

}