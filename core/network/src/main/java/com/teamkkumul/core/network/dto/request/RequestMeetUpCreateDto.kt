package com.teamkkumul.core.network.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestMeetUpCreateDto(
    @SerialName("name")
    val name: String,
    @SerialName("placeName")
    val placeName: String,
    @SerialName("x")
    val x: Double? = null,
    @SerialName("y")
    val y: Double? = null,
    @SerialName("address")
    val address: String?,
    @SerialName("roadAddress")
    val roadAddress: String?,
    @SerialName("time")
    val time: String,
    @SerialName("participants")
    val participants: List<Int>,
    @SerialName("dressUpLevel")
    val dressUpLevel: String,
    @SerialName("penalty")
    val penalty: String,
)
