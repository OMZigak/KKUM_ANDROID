package com.teamkkumul.model

sealed class MeetUpSealedItem {
    data class MyGroupPlus(
        val num: Int,
    ) : MeetUpSealedItem()

    data class Participant(
        val id: Int,
        val name: String,
        val profileImg: String,
    ) : MeetUpSealedItem()
}
