package com.app.data.use_cases

import com.app.base.models.BaseListResponse
import com.app.data.api.ClaimsApi
import com.app.data.models.domain.Claim
import com.app.data.models.domain.Document
import com.app.data.models.enums.ClaimStatus
import okhttp3.MultipartBody
import javax.inject.Inject

class ClaimsUseCase @Inject constructor(
    private val claimsApi: ClaimsApi
) {

    suspend fun getClaims(
        pageNumber: Int,
        pageSize: Int = 20
    ): BaseListResponse<Claim> = claimsApi.getClaims(pageNumber, pageSize)

    suspend fun getActiveClaims(
        pageNumber: Int,
        pageSize: Int = 20
    ): BaseListResponse<Claim> = claimsApi.getActiveClaims(pageNumber, pageSize)

    suspend fun getClaimsByStatus(
        pageNumber: Int,
        pageSize: Int = 20,
        status: ClaimStatus
    ): BaseListResponse<Claim> = claimsApi.getClaimWithStatus(pageNumber, pageSize, status)

    suspend fun getClaimById(
        claimId: Long
    ): Claim = claimsApi.getClaimById(claimId)

    suspend fun getDocumentsByClaimId(
        claimId: Long
    ): BaseListResponse<Document> = claimsApi.getClaimDocuments(claimId)

    suspend fun getAllClaims(): BaseListResponse<Claim> {
        return claimsApi.getAllClaims()
    }

    suspend fun createClaimWithPhotos(
        claimPart: MultipartBody.Part,
        photoParts: List<MultipartBody.Part>,
        documentParts: List<MultipartBody.Part>,
        docTypes: List<String?>
    ): Claim {
        return claimsApi.createClaimWithPhotos(
            claim = claimPart,
            photos = photoParts,
            documents = documentParts,
            documentType1 = docTypes.getOrNull(0),
            documentType2 = docTypes.getOrNull(1),
            documentType3 = docTypes.getOrNull(2)
        )
    }
}