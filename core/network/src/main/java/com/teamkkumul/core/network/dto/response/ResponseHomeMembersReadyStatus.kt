package com.teamkkumul.core.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseHomeMembersReadyStatus(
    @SerialName("participantCount")
    val participantCount: Int,
    @SerialName("participants")
    val participants: List<Participant>,
) {
    @Serializable
    data class Participant(
        @SerialName("memberId")
        val memberId: Int,
        @SerialName("name")
        val name: String,
        @SerialName("participantId")
        val participantId: Int,
        @SerialName("profileImg")
        val profileImg: String?,
        @SerialName("state")
        val state: String,
    )
}
