package com.teamkkumul.core.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseMeetUpDetailDto(
    @SerialName("address")
    val address: String? = null,
    @SerialName("dressUpLevel")
    val dressUpLevel: String,
    @SerialName("penalty")
    val penalty: String,
    @SerialName("placeName")
    val placeName: String,
    @SerialName("promiseId")
    val promiseId: Int,
    @SerialName("roadAddress")
    val roadAddress: String? = null,
    @SerialName("time")
    val time: String,
)
