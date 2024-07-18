package com.teamkkumul.core.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseMyGroupInfoDto(
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("invitationCode")
    val invitationCode: String,
    @SerialName("meetingId")
    val meetingId: Int,
    @SerialName("metCount")
    val metCount: Int,
    @SerialName("name")
    val name: String,
)
