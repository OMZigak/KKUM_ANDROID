package com.teamkkumul.core.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseAddNewGroupDto(
    @SerialName("meetingId")
    val meetingId: Int,
    @SerialName("invitationCode")
    val invitationCode: String,
)
