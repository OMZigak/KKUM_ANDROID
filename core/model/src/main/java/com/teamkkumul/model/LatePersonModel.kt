package com.teamkkumul.model

data class LatePersonModel(
    val penalty: String,
    val isPastDue: Boolean,
    val lateComers: List<LateComers>,
) {
    data class LateComers(
        val participantId: Int,
        val name: String,
        val profileImg: String?,
    )
}
