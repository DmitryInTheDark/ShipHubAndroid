package com.app.base.models

data class BaseListResponse<T>(
    val count: Int,
    val items: List<T>
): BaseResponse()
