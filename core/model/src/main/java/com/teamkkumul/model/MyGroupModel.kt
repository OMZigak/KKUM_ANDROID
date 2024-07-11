package com.teamkkumul.model

data class MyGroupModel(
    val count: Int,
    val meetings: List<Meeting>,
) {
    data class Meeting(
        val id: Int,
        val memberCount: Int,
        val name: String,
    )
}
