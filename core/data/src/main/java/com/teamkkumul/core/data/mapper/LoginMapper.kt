package com.teamkkumul.core.data.mapper

import com.teamkkumul.core.network.dto.response.ResponseLoginDto
import com.teamkkumul.model.login.LoginModel

internal fun ResponseLoginDto.toLoginModel(): LoginModel =
    LoginModel(
        name = name,
        accessToken = jwtTokenDto.accessToken,
        refreshToken = jwtTokenDto.refreshToken,
    )
