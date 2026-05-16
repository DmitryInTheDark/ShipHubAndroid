package com.app.data.models.domain

import com.app.data.models.enums.ClaimStatus
import com.app.data.models.enums.TestType

data class Claim(
    val id: Long,
    val dateCreate: String,
    val dateUpdate: String,
    val organizationName: String,
    val clientName: String,
    val contactPhone: String,
    val email: String,
    val testType: TestType,
    val equipment: Equipment,
    val whoCreate: User,
    val isCustomType: Boolean,
    val customTestName: String?,
    val additionalInfo: String?,
    val status: ClaimStatus,
    val lastUpdate: String?,
    val documentsIds: List<Long> = emptyList()
)