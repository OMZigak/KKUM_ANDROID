package com.teamkkumul.model.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeReadyStatusModel(
    val arrivalAt: String?,
    val departureAt: String?,
    val preparationStartAt: String?,
    val preparationTime: Int?,
    val promiseTime: String?,
    val travelTime: Int?,
    val promiseId: Int? = null,
) : Parcelable
