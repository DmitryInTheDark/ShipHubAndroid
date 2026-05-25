package com.app.data.models.requests

import com.app.data.models.enums.TestType
import kotlinx.serialization.Serializable

@Serializable
data class ClaimRequest(
    val organizationName: String,
    val clientName: String,
    val contactPhone: String,
    val email: String,
    val testType: TestType?,
    val equipment: EquipmentRequest,
    val isCustomType: Boolean,
    val customTestName: String?,
    val additionalInfo: String?
)
