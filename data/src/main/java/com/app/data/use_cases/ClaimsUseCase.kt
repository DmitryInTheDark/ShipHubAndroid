package com.app.data.use_cases

import com.app.base.models.BaseListResponse
import com.app.data.api.ClaimsApi
import com.app.data.models.domain.Claim
import javax.inject.Inject

class ClaimsUseCase @Inject constructor(
    private val claimsApi: ClaimsApi
) {

    suspend fun getClaims(
        pageNumber: Int,
        pageSize: Int = 20
    ): BaseListResponse<Claim> = claimsApi.getClaims()

    suspend fun getActiveClaims(
        pageNumber: Int,
        pageSize: Int = 20
    ): BaseListResponse<Claim> = claimsApi.getActiveClaims(pageNumber, pageSize)

}