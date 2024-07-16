package com.teamkkumul.core.data.repositoryimpl

import com.teamkkumul.core.data.repository.MeetingsRepository
import com.teamkkumul.core.network.api.MeetingsService
import com.teamkkumul.core.network.dto.request.RequestAddNewGroupDto
import com.teamkkumul.core.network.dto.request.RequestEnterInvitationCodeDto
import com.teamkkumul.core.network.dto.response.BaseResponse
import javax.inject.Inject

internal class MeetingsRepositoryImpl @Inject constructor(
    private val meetingsService: MeetingsService,
) : MeetingsRepository {
    override suspend fun addNewGroup(request: String): Result<String> =
        runCatching {
            meetingsService.addNewGroup(RequestAddNewGroupDto(request)).data?.invitationCode ?: throw Exception("data is null")
        }

    override suspend fun enterInvitationCode(request: String): Result<BaseResponse<Unit>> =
        runCatching {
            meetingsService.enterInvitationCode(RequestEnterInvitationCodeDto(request))
        }
}
