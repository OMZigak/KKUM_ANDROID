package com.teamkkumul.core.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseUserDto(
    @SerialName("userId")
    val userId: Int,
    @SerialName("level")
    val level: Int,
    @SerialName("name")
    val name: String? = null,
    @SerialName("profileImg")
    val profileImg: String? = null,
    @SerialName("promiseCount")
    val promiseCount: Int,
    @SerialName("tardyCount")
    val tardyCount: Int,
    @SerialName("tardySum")
    val tardySum: Int,
)
