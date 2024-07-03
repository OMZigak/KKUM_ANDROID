package com.teamkkumul.core.data.mapper

import com.teamkkumul.core.network.dto.response.ResponseReqresDto
import com.teamkkumul.model.example.ReqresModel

internal fun ResponseReqresDto.toReqresModel(): List<ReqresModel> = data.map {
    ReqresModel(
        id = it.id,
        email = it.email,
        firstName = it.firstName,
        lastName = it.lastName,
        avatar = it.avatar,
    )
}
