package com.teamkkumul.core.network.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestLoginDto(
    @SerialName("provider")
    val provider: String,
    @SerialName("fcmToken")
    val fcmToken: String,
)
