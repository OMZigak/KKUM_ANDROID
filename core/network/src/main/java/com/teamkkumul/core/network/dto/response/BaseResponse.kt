package com.teamkkumul.core.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    @SerialName("success")
    val success: Boolean,
    @SerialName("error")
    val error: Error,
    @SerialName("data")
    val data: T? = null,
) {
    @Serializable
    data class Error(
        @SerialName("code")
        val code: String,
        @SerialName("message")
        val message: String,
    )
}
