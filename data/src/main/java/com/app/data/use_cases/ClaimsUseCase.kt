package com.app.data.use_cases

import com.app.base.models.BaseListResponse
import com.app.data.api.ClaimsApi
import com.app.data.models.domain.Claim
import com.app.data.models.domain.Document
import com.app.data.models.enums.ClaimStatus
import com.app.data.models.response.MessageResponse
import okhttp3.MultipartBody
import javax.inject.Inject

class ClaimsUseCase @Inject constructor(
    private val claimsApi: ClaimsApi,
    private val userRepository: com.app.data.UserRepository
) {

    fun getCurrentUserId(): Long = userRepository.getUser().id

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

    suspend fun getMessagesByClaim(claimId: Long): BaseListResponse<MessageResponse> {
        return claimsApi.getMessagesByClaim(claimId)
    }

    suspend fun createClaimWithPhotos(
        claimPart: MultipartBody.Part,
        photoParts: List<MultipartBody.Part>,
        documentParts: List<MultipartBody.Part>,
        docTypes: List<String?>
    ): Claim {
        return claimsApi.createClaimWithPhotos(
            claim = claimPart,
            photo1 =photoParts.getOrNull(0),
            photo2 =photoParts.getOrNull(1),
            photo3 =photoParts.getOrNull(2),
            document1 = documentParts.getOrNull(0),
            document2 = documentParts.getOrNull(1),
            document3 = documentParts.getOrNull(2),
            documentType1 = docTypes.getOrNull(0),
            documentType2 = docTypes.getOrNull(1),
            documentType3 = docTypes.getOrNull(2)
        )
    }

    suspend fun attachDocuments(
        claimId: Long,
        documentParts: List<MultipartBody.Part>,
        documentTypes: List<String>
    ): Map<String, String> {

        if (documentParts.isEmpty()) {
            return emptyMap()
        }

        return claimsApi.attachDocuments(
            claimId = claimId,
            documents = documentParts,
            documentTypesJson = documentTypes
        )
    }
}