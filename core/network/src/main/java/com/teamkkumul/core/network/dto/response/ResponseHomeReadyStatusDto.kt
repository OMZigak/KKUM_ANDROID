package com.teamkkumul.core.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseHomeReadyStatusDto(
    @SerialName("arrivalAt")
    val arrivalAt: String?,
    @SerialName("departureAt")
    val departureAt: String?,
    @SerialName("preparationStartAt")
    val preparationStartAt: String?,
    @SerialName("preparationTime")
    val preparationTime: Int?,
    @SerialName("promiseTime")
    val promiseTime: String?,
    @SerialName("travelTime")
    val travelTime: Int?,
)
