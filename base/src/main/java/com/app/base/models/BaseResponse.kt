package com.app.base.models

import com.google.gson.annotations.SerializedName

open class BaseResponse(
    @SerializedName("message")
    open val message: String? = null,
    @SerializedName("code")
    open val code: Int? = null
)
