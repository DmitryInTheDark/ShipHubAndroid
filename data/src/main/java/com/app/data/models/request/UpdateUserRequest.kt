package com.app.data.models.request

import com.app.data.models.domain.LegalInfo
import com.app.data.models.domain.PhysicalInfo

data class UpdateUserRequest(
    val username: String? = null,
    val email: String? = null,
    val password: String? = null,
    val isActive: Boolean? = null,
    val verificationCode: String? = null,
    val legalInfo: LegalInfo? = null,
    val physicalInfo: PhysicalInfo? = null
)
