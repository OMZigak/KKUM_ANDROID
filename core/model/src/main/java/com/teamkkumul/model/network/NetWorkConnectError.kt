package com.teamkkumul.model.network

data class NetWorkConnectError(
    val errorMessage: String
) : Exception(errorMessage)
