package com.teamkkumul.core.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseMyGroupDto(
    @SerialName("count")
    val count: Int,
    @SerialName("meetings")
    val meetings: List<Meeting>,
) {
    @Serializable
    data class Meeting(
        @SerialName("meetingId")
        val meetingId: Int,
        @SerialName("memberCount")
        val memberCount: Int,
        @SerialName("name")
        val name: String,
    )
}
