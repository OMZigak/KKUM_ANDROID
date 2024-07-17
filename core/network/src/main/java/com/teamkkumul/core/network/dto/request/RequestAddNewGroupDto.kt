package com.teamkkumul.core.network.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestAddNewGroupDto(
    @SerialName("name")
    val name: String,
)
