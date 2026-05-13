package com.app.data.models.domain

import com.app.base.models.BaseResponse
import com.app.data.models.enums.UserType
import com.google.gson.annotations.SerializedName


data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("type") val type: UserType,
    @SerializedName("legalInfo") val legalInfo: LegalInfo?,
    @SerializedName("physicalInfo") val physicalInfo: PhysicalInfo?
): BaseResponse()