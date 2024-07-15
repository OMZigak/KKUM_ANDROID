package com.teamkkumul.core.data.repository

import com.teamkkumul.core.network.dto.request.RequestAddNewGroupDto
import com.teamkkumul.core.network.dto.request.RequestEnterInvitationCodeDto
import com.teamkkumul.core.network.dto.response.BaseResponse
import com.teamkkumul.core.network.dto.response.ResponseAddNewGroupDto

interface MeetingsRepository {
    suspend fun addNewGroup(request: RequestAddNewGroupDto): Result<BaseResponse<ResponseAddNewGroupDto>>
    suspend fun enterInvitationCode(request: RequestEnterInvitationCodeDto): Result<BaseResponse<Unit>>
}
