package com.app.data.models.response

import com.app.base.models.BaseResponse
import com.google.gson.annotations.SerializedName

data class RegistrationResponse (
    @SerializedName("response") val response: String?
): BaseResponse()