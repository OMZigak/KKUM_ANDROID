package com.teamkkumul.core.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    @SerialName("success")
    val success: Boolean,
    @SerialName("error")
    val error: BaseError? = null,
    @SerialName("data")
    val data: T? = null,
) {
    @Serializable
    data class BaseError(
        @SerialName("code")
        val code: Int,
        @SerialName("message")
        val message: String,
    )
}
