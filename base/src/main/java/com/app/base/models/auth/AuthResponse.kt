package com.app.base.models.auth

import com.app.base.models.BaseResponse
import com.app.base.models.person.PersonDTO
import com.google.gson.annotations.SerializedName

data class AuthResponse(
    val token: String,
    val user: PersonDTO,
    @SerializedName("message_auth")
    override val message: String? = null,
    @SerializedName("code_auth")
    override val code: Int? = null
) : BaseResponse(message, code)
