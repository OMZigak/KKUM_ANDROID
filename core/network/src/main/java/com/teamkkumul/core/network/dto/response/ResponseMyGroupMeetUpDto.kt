package com.teamkkumul.core.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseMyGroupMeetUpDto(
    @SerialName("promise")
    val promises: List<Promise>,
) {
    @Serializable
    data class Promise(
        @SerialName("dDay")
        val dDay: Int,
        @SerialName("date")
        val date: String,
        @SerialName("id")
        val id: Int,
        @SerialName("name")
        val name: String,
        @SerialName("place_name")
        val placeName: String,
        @SerialName("time")
        val time: String,
    )
}
