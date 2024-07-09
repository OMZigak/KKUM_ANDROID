package com.teamkkumul.core.network.dto.response

data class ResponseMyGroupFriendDto(
    val memberCount: Int,
    val members: List<Member>,
) {
    data class Member(
        val id: Int,
        val name: String,
        val profileImg: String,
    )
}
