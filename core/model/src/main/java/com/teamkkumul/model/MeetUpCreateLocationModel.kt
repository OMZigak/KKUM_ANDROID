package com.teamkkumul.model

data class MeetUpCreateLocationModel(
    val places: List<Place>,
) {
    data class Place(
        val address: String,
        val location: String,
        val roadAddress: String,
    )
}
