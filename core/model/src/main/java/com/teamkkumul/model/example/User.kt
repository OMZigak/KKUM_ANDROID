package com.teamkkumul.model.example

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String,
    val password: String,
    val nickName: String,
    val phone: String,
) : Parcelable
