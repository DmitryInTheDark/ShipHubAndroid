package com.app.data.models.domain

import com.app.base.models.BaseResponse
import com.app.data.models.enums.UserType


data class User(
    val id: Long,
    val username: String,
    val email: String,
    val type: UserType,
    val legalInfo: LegalInfo?,
    val physicalInfo: PhysicalInfo?
): BaseResponse()