package com.teamkkumul.model.home

data class HomeReadyStatusModel(
    val arrivalAt: String?,
    val departureAt: String?,
    val preparationStartAt: String?,
    val preparationTime: Int?,
    val promiseTime: String?,
    val travelTime: Int?,
)
