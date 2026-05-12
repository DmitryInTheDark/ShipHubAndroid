package com.app.base.models.person

import com.app.base.models.BaseResponse
import com.app.domain.UserType
import com.google.gson.annotations.SerializedName

data class PersonDTO(
    val id: Long? = null,
    val username: String,
    val email: String,
    val type: UserType,
    val legalInfo: Any? = null,
    val physicalInfo: Any? = null,
    @SerializedName("message_person")
    override val message: String? = null,
    @SerializedName("code_person")
    override val code: Int? = null
) : BaseResponse(message, code)
