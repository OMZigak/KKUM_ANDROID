package com.teamkkumul.core.data.repositoryimpl

import com.teamkkumul.core.data.mapper.toAddNewGroupModel
import com.teamkkumul.core.data.repository.MeetingsRepository
import com.teamkkumul.core.data.utils.handleThrowable
import com.teamkkumul.core.network.api.MeetingsService
import com.teamkkumul.core.network.dto.request.RequestAddNewGroupDto
import com.teamkkumul.core.network.dto.request.RequestEnterInvitationCodeDto
import com.teamkkumul.model.AddNewGroupModel
import javax.inject.Inject

internal class MeetingsRepositoryImpl @Inject constructor(
    private val meetingsService: MeetingsService,
) : MeetingsRepository {
    override suspend fun addNewGroup(request: String): Result<AddNewGroupModel> = runCatching {
        meetingsService.addNewGroup(RequestAddNewGroupDto(request)).data?.toAddNewGroupModel()
    }.mapCatching {
        requireNotNull(it)
    }.recoverCatching {
        return it.handleThrowable()
    }

    override suspend fun enterInvitationCode(request: String): Result<Int> = runCatching {
        meetingsService.enterInvitationCode(RequestEnterInvitationCodeDto(request)).data?.meetingId
    }.mapCatching {
        requireNotNull(it)
    }.recoverCatching {
        return it.handleThrowable()
    }
}
