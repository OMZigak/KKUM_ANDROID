package com.teamkkumul.model

data class MeetUpCreateModel(
    val name: String,
    val placeName: String,
    val x: Double,
    val y: Double,
    val address: String?,
    val roadAddress: String?,
    val time: String,
    val participants: List<Int>,
    val dressUpLevel: String,
    val penalty: String,
    val promiseId: Int? = null,
    val meetUpType: String? = null,
    val date: String? = null,
    val meetingId: Int? = null
)
