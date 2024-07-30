package com.teamkkumul.core.data.utils

import com.teamkkumul.core.network.dto.response.BaseResponse
import com.teamkkumul.model.network.ApiError
import com.teamkkumul.model.network.NetWorkConnectError
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import retrofit2.Response
import java.net.UnknownHostException

private const val UNKNOWN_ERROR_MESSAGE = "Unknown error"

private fun HttpException.getErrorMessage(): String {
    val errorBody = response()?.errorBody()?.string() ?: return UNKNOWN_ERROR_MESSAGE
    return parseErrorMessage(errorBody)
}

private fun parseErrorMessage(errorBody: String): String {
    return try {
        val errorResponse = Json.decodeFromString<BaseResponse<BaseResponse.BaseError>>(errorBody)
        errorResponse.error?.message ?: "error message is null"
    } catch (e: Exception) {
        UNKNOWN_ERROR_MESSAGE
    }
}

fun <T> Throwable.toApiResult(): Result<T> {
    return when (this) {
        is HttpException -> Result.failure(ApiError(this.getErrorMessage()))
        is UnknownHostException -> Result.failure(NetWorkConnectError("인터넷에 연결해 주세요"))
        else -> Result.failure(this)
    }
}

fun Response<*>?.getResponseErrorMessage(): String {
    val errorBody = this?.errorBody()?.string() ?: return UNKNOWN_ERROR_MESSAGE
    return parseErrorMessage(errorBody)
}
