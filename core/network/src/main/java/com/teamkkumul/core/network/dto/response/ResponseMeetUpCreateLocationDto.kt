package com.teamkkumul.core.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseMeetUpCreateLocationDto(
    @SerialName("locations")
    val locations: List<Location>,
) {
    @Serializable
    data class Location(
        @SerialName("address")
        val address: String?,
        @SerialName("location")
        val location: String,
        @SerialName("roadAddress")
        val roadAddress: String?,
        @SerialName("x")
        val x: Double,
        @SerialName("y")
        val y: Double,
    )
}
