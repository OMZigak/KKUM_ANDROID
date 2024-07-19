package com.teamkkumul.core.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseEnterInvitationCodeDto(
    @SerialName("meetingId")
    val meetingId: Int,
)
