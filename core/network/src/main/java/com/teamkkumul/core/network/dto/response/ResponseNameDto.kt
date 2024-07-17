package com.teamkkumul.core.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseNameDto(
    @SerialName("name")
    val name: String? = null,
)
