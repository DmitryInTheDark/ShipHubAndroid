package com.app.data.models.request

import com.app.data.models.enums.ClaimStatus

data class UpdateClaimDTO(
    val status: ClaimStatus,
    val updateInfo: String
)
