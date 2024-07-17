package com.teamkkumul.model

data class MyGroupMemberModel(
    val memberCount: Int,
    val members: List<Member>,
) {
    data class Member(
        val id: Int,
        val name: String,
        val profileImg: String?,
    )
}
