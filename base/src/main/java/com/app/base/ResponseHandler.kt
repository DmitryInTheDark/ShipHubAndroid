package com.app.base

import com.app.base.models.BaseResponse
import com.google.gson.Gson
import okio.IOException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

inline fun <T> safeCall(action: () -> T): Result<T> {
    return try {
        Result.success(action())
    } catch (e: Exception) {
        Result.failure(e)
    }
}

fun Throwable.handleError(): String {
    return when (this) {

        is UnknownHostException,
        is SocketTimeoutException -> {
            "Ошибка сети"
        }

        is IOException -> {
            message ?: "Ошибка ввода/вывода"
        }

        is HttpException -> {
            try {
                val errorBody = response()?.errorBody()?.string()

                if (!errorBody.isNullOrBlank()) {
                    val response =
                        Gson().fromJson(errorBody, BaseResponse::class.java)

                    response.message ?: "Ошибка сервера"
                } else {
                    "Ошибка сервера"
                }

            } catch (_: Exception) {
                "Ошибка обработки ответа"
            }
        }

        else -> {
            "Неизвестная ошибка"
        }
    }
}