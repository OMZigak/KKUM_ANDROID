package com.teamkkumul.model.login

data class LoginModel(
    val name: String?,
    val accessToken: String,
    val refreshToken: String,
)
