package com.teamkkumul.core.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseLatePersonDto(
    @SerialName("penalty")
    val penalty: String,
    @SerialName("isPastDue")
    val isPastDue: String,
    @SerialName("lateComers")
    val lateComers: List<LateComers>,
) {
    @Serializable
    data class LateComers(
        @SerialName("participantId")
        val participantId: Int,
        @SerialName("name")
        val name: String,
        @SerialName("profileImg")
        val profileImg: String,
    )
}
