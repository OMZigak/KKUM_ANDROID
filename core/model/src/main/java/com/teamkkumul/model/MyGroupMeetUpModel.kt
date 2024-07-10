package com.teamkkumul.model

data class MyGroupMeetUpModel(
    val promises: List<Promise>,
) {
    data class Promise(
        val dDay: Int,
        val date: String,
        val name: String,
        val placeName: String,
        val time: String,
    )
}
