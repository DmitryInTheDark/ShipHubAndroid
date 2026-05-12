package com.app.base.models.auth

import com.app.domain.UserType

import java.io.Serializable

data class RegistrationRequestDTO(
    val name: String,
    val email: String,
    val password: String,
    val type: UserType,
    val physicalInfo: PhysicalInfo? = null,
    val legalInfo: LegalInfo? = null
) : Serializable

data class PhysicalInfo(
    val address: String
) : Serializable

data class LegalInfo(
    val organizationName: String,
    val inn: String,
    val kpp: String,
    val address: String,
    val phone: String
) : Serializable
