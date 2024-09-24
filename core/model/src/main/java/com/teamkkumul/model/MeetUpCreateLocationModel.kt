package com.teamkkumul.model

data class MeetUpCreateLocationModel(
    val locations: List<Location>,
) {
    data class Location(
        val address: String?,
        val location: String,
        val roadAddress: String?,
        val x: Double,
        val y: Double,
    )
}
