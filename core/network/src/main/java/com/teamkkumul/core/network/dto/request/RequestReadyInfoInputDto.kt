package com.teamkkumul.core.network.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestReadyInfoInputDto(
    @SerialName("preparationTime")
    val preparationTime: Int,
    @SerialName("travelTime")
    val travelTime: Int,
)
