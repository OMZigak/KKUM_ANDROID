package com.teamkkumul.core.data.utils

import com.teamkkumul.core.network.dto.response.BaseResponse
import com.teamkkumul.model.network.ApiError
import com.teamkkumul.model.network.NetWorkConnectError
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

fun Throwable.getErrorMessage(): String {
    return when (this) {
        is HttpException -> {
            val errorBody = response()?.errorBody()?.string() ?: return "Unknown error"
            val errorResponse = Json.decodeFromString<BaseResponse<Error>>(errorBody)
            errorResponse.error.message
        }

        else -> "An unknown error occurred."
    }
}

fun <T> Throwable.handleThrowable(): Result<T> {
    return Result.failure(
        when (this) {
            is HttpException -> ApiError(this.getErrorMessage())
            is IOException -> NetWorkConnectError("인터넷에 연결해 주세요")
            else -> this
        },
    )
}

fun Response<*>?.getResponseErrorMessage(): String {
    val errorBody = this?.errorBody()?.string() ?: return "Unknown error"
    val errorResponse = Json.decodeFromString<BaseResponse<Error>>(errorBody)
    return errorResponse.error.message
}
