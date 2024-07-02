package com.teamkkumul.core.data.mapper

import com.teamkkumul.core.network.dto.response.BaseResponse
import com.teamkkumul.model.BaseApi

internal fun BaseResponse<Unit>.toBaseModel() = BaseApi(
    code,
    message,
)
