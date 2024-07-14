package com.teamkkumul.core.data.repositoryimpl

import com.teamkkumul.core.data.repository.MeetingsRepository
import com.teamkkumul.core.network.api.MeetingsService
import com.teamkkumul.core.network.dto.request.RequestAddNewGroupDto
import com.teamkkumul.core.network.dto.response.BaseResponse
import com.teamkkumul.core.network.dto.response.ResponseAddNewGroupDto
import javax.inject.Inject

internal class MeetingsRepositoryImpl @Inject constructor(
    private val meetingsService: MeetingsService,
) : MeetingsRepository {
    override suspend fun addNewGroup(request: RequestAddNewGroupDto): Result<BaseResponse<ResponseAddNewGroupDto>> =
        runCatching {
            meetingsService.addNewGroup(request)
        }
}
