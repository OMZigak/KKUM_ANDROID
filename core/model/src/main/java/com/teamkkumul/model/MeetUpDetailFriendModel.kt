package com.teamkkumul.model

class MeetUpDetailFriendModel(
    val memberCount: Int,
    val participants: List<Participant>,
) {
    data class Participant(
        val id: Int,
        val name: String,
        val profileImg: String,
        val state: String? = null,
    )
}
