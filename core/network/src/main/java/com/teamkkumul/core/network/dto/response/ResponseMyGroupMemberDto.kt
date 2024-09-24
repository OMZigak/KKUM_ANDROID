package com.teamkkumul.core.network.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseMyGroupMemberDto(
    @SerialName("memberCount")
    val memberCount: Int,
    @SerialName("members")
    val members: List<Member>,
) {
    @Serializable
    data class Member(
        @SerialName("memberId")
        val memberId: Int,
        @SerialName("name")
        val name: String,
        @SerialName("profileImg")
        val profileImg: String?,
    )
}
