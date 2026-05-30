package com.app.data.api

import com.app.base.models.BaseListResponse
import com.app.data.models.domain.Claim
import com.app.data.models.domain.Document
import com.app.data.models.enums.ClaimStatus
import com.app.data.models.request.UpdateClaimDTO
import com.app.data.models.response.MessageResponse
import okhttp3.MultipartBody
import retrofit2.http.*

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

    @GET("/claims")
    suspend fun getClaimWithStatus(
        @Query("page_number") pageNumber: Int = 0,
        @Query("page_size") pageSize: Int = 20,
        @Query("status") status: ClaimStatus
    ): BaseListResponse<Claim>

    @GET("/claims/{claimId}")
    suspend fun getClaimById(@Path("claimId") claimId: Long): Claim

    @GET("/claims/documents")
    suspend fun getClaimDocuments(
        @Query("claim_id") claimId: Long
    ): BaseListResponse<Document>

    @GET("/claims")
    suspend fun getAllClaims(): BaseListResponse<Claim>

    @GET("/messages")
    suspend fun getMessagesByClaim(
        @Query("claim_id") claimId: Long
    ): BaseListResponse<MessageResponse>

    @Multipart
    @POST("/claims/create_with_photos")
    suspend fun createClaimWithPhotos(
        @Part claim: MultipartBody.Part,
        @Part photo1: MultipartBody.Part? = null,
        @Part photo2: MultipartBody.Part? = null,
        @Part photo3: MultipartBody.Part? = null,
        @Part document1: MultipartBody.Part? = null,
        @Part document2: MultipartBody.Part? = null,
        @Part document3: MultipartBody.Part? = null,
        @Part("document_type1") documentType1: String? = null,
        @Part("document_type2") documentType2: String? = null,
        @Part("document_type3") documentType3: String? = null
    ): Claim

    @Multipart
    @POST("/claims/{claimId}/attach_documents")
    suspend fun attachDocuments(
        @Path("claimId") claimId: Long,
        @Part documents: List<MultipartBody.Part>,
        @Part("document_types") documentTypesJson: List<String>   // JSON-массив строк
    ): Map<String, String>

    @GET("claims/documents/{id}/file")
    suspend fun downloadDocument(@Path("id") id: Long): retrofit2.Response<okhttp3.ResponseBody>

    @PATCH("/claims/{id}/update")
    suspend fun updateClaim(
        @Path("id") id: Long,
        @Body dto: UpdateClaimDTO
    ): Claim
}