package com.teamkkumul.model

import android.os.Parcelable
import com.teamkkumul.model.type.ScreenType
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddNewGroupModel(
    val meetingId: Int,
    val invitationCode: String,
    val screenType: ScreenType? = null,
) : Parcelable
