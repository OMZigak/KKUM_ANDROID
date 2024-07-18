package com.teamkkumul.model.home

data class HomeMembersStatus(
    val participantCount: Int,
    val participants: List<Participant>,
) {
    data class Participant(
        val memberId: Int,
        val name: String,
        val participantId: Int,
        val profileImg: String,
        val state: String,
    )
}
