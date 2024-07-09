package com.teamkkumul.core.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseMyGroupFriendDto(
    @SerialName("member_count")
    val memberCount: Int,
    @SerialName("members")
    val members: List<Member>,
) {
    @Serializable
    data class Member(
        @SerialName("id")
        val id: Int,
        @SerialName("name")
        val name: String,
        @SerialName("profile_img")
        val profileImg: String,
    )
}
