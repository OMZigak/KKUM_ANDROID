package com.teamkkumul.model.network

data class ApiError(
    val errorMessage: String?
) : Exception(errorMessage)
