package com.teamkkumul.core.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseMyGroupMeetUpDto(
    @SerialName("promises")
    val promises: List<Promise>,
) {
    @Serializable
    data class Promise(
        @SerialName("dDay")
        val dDay: Int,
        @SerialName("date")
        val date: String,
        @SerialName("name")
        val name: String,
        @SerialName("placeName")
        val placeName: String,
        @SerialName("promiseId")
        val promiseId: Int,
        @SerialName("time")
        val time: String,
    )
}
