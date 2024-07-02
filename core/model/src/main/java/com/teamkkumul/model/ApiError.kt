package com.teamkkumul.model

data class ApiError(
    val errorMessage: String?
) : Exception(errorMessage)
