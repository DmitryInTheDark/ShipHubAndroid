package com.app.data.models.request

import com.app.data.models.domain.LegalInfo
import com.app.data.models.domain.PhysicalInfo
import com.app.data.models.enums.UserType

data class RegistrationRequest(
    val email: String,
    val password: String,
    val username: String,
    val type: UserType,
    val legalInfo: LegalInfo?,
    val physicalInfo: PhysicalInfo?
)