package com.teamkkumul.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddNewGroupModel(
    val meetingId: Int,
    val invitationCode: String,
) : Parcelable
