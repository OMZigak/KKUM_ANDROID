package com.teamkkumul.core.data.repository

import com.teamkkumul.core.network.dto.response.BaseResponse

interface MeetingsRepository {
    suspend fun addNewGroup(request: String): Result<String>
    suspend fun enterInvitationCode(request: String): Result<BaseResponse<Unit>>
}
