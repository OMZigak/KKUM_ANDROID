package com.teamkkumul.core.data.repositoryimpl

import com.teamkkumul.core.data.mapper.toMeetUpCreateLocationModel
import com.teamkkumul.core.data.mapper.toRequestMeetUpCreateDto
import com.teamkkumul.core.data.repository.MeetUpCreateLocationRepository
import com.teamkkumul.core.network.api.MeetUpCreateService
import com.teamkkumul.model.MeetUpCreateLocationModel
import com.teamkkumul.model.MeetUpCreateModel
import javax.inject.Inject

class MeetUpCreateLocationRepositoryImpl @Inject constructor(
    private val meetUpCreateLocationService: MeetUpCreateService,
) : MeetUpCreateLocationRepository {
    override suspend fun getMeetUpCreateLocation(q: String): Result<List<MeetUpCreateLocationModel.Location>> =
        runCatching {
            val response = meetUpCreateLocationService.getMeetUpCreateLocation(q)
            response.data?.toMeetUpCreateLocationModel() ?: throw Exception("null")
        }

    override suspend fun postMeetUpCreate(
        meetingId: Int,
        meetUpCreateModel: MeetUpCreateModel,
    ): Result<Int> {
        return runCatching {
            val requestDto = meetUpCreateModel.toRequestMeetUpCreateDto()

            meetUpCreateLocationService.postNewMeetUp(
                meetingId,
                requestDto,
            ).data?.promiseId ?: throw Exception("null")
        }
    }
}
