package com.teamkkumul.core.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseSetNameDto(
    @SerialName("name")
    val name: String? = null,
)
