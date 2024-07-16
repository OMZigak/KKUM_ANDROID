package com.teamkkumul.core.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseLoginDto(
    @SerialName("name")
    val name: String? = null,
    @SerialName("jwtTokenDto")
    val jwtTokenDto: JwtTokenDto,
) {
    @Serializable
    class JwtTokenDto(
        @SerialName("accessToken")
        val accessToken: String,
        @SerialName("refreshToken")
        val refreshToken: String,
    )
}
