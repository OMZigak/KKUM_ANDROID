package com.teamkkumul.core.data.repositoryimpl

import com.teamkkumul.core.data.mapper.toMeetUpCreateLocationModel
import com.teamkkumul.core.data.mapper.toMeetUpEditParticipant
import com.teamkkumul.core.data.mapper.toRequestMeetUpCreateDto
import com.teamkkumul.core.data.repository.MeetUpCreateLocationRepository
import com.teamkkumul.core.data.utils.handleThrowable
import com.teamkkumul.core.network.api.MeetUpCreateService
import com.teamkkumul.model.MeetUpCreateLocationModel
import com.teamkkumul.model.MeetUpCreateModel
import com.teamkkumul.model.MeetUpEditParticipantModel
import javax.inject.Inject

class MeetUpCreateLocationRepositoryImpl @Inject constructor(
    private val meetUpCreateLocationService: MeetUpCreateService,
) : MeetUpCreateLocationRepository {
    override suspend fun getMeetUpCreateLocation(q: String): Result<List<MeetUpCreateLocationModel.Location>> =
        runCatching {
            meetUpCreateLocationService.getMeetUpCreateLocation(q).data?.toMeetUpCreateLocationModel()
        }.mapCatching {
            requireNotNull(it)
        }.recoverCatching {
            return it.handleThrowable()
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
            ).data?.promiseId
        }.mapCatching {
            requireNotNull(it)
        }.recoverCatching {
            return it.handleThrowable()
        }
    }

    override suspend fun getMeetUpEditParticipant(promiseId: Int): Result<List<MeetUpEditParticipantModel.Member>> =
        runCatching {
            val response = meetUpCreateLocationService.getMeetUpEditParticipant(promiseId)
            response.data?.toMeetUpEditParticipant() ?: throw Exception("null")
        }
}
