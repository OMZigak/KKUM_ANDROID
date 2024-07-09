package com.teamkkumul.core.network.dto.response

data class ResponseMyGroupMeetUpDto(
    val promises: List<Promise>,
) {
    data class Promise(
        val dDay: Int,
        val date: String,
        val id: Int,
        val name: String,
        val placeName: String,
        val time: String,
    )
}
