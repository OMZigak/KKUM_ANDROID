package com.teamkkumul.model

data class MeetUpEditParticipantModel(
    val members: List<Member>,
) {
    data class Member(
        val isParticipant: Boolean,
        val memberId: Int,
        val name: String,
        val profileImg: String?,
    )
}
