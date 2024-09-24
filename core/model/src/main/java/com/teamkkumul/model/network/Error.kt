package com.teamkkumul.model.network

sealed class Error(message: String?) : Exception(message) {
    data class ApiError(
        val errorMessage: String?,
    ) : Error(errorMessage)

    data class NetWorkConnectError(
        val errorMessage: String,
    ) : Error(errorMessage)

    data class TimeOutError(
        val errorMessage: String,
    ) : Error(errorMessage)

    data class UnknownError(
        val errorMessage: String,
    ) : Error(errorMessage)
}
