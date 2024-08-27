package com.teamkkumul.model

data class MeetUpDetailModel(
    val isParticipant: Boolean?,
    val address: String?,
    val dressUpLevel: String,
    val penalty: String,
    val placeName: String,
    val roadAddress: String?,
    val time: String,
    val promiseName: String,
    val x: Double?,
    val y: Double?,
)
