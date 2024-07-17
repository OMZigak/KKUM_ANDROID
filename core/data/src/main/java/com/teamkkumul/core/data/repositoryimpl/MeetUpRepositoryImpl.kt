package com.teamkkumul.core.data.repositoryimpl

import com.teamkkumul.core.data.mapper.toMeetUpDetailModel
import com.teamkkumul.core.data.mapper.toMeetUpParticipantModel
import com.teamkkumul.core.data.mapper.toMeetUpSealedItem
import com.teamkkumul.core.data.repository.MeetUpRepository
import com.teamkkumul.core.network.api.MeetUpService
import com.teamkkumul.model.MeetUpDetailModel
import com.teamkkumul.model.MeetUpParticipantModel
import com.teamkkumul.model.MeetUpSealedItem
import javax.inject.Inject

class MeetUpRepositoryImpl @Inject constructor(
    private val meetUpService: MeetUpService,
) : MeetUpRepository {
    override suspend fun getMeetUpParticipant(promiseId: Int): Result<MeetUpParticipantModel> =
        runCatching {
            val response = meetUpService.getMeetUpParticipantList(promiseId)
            response.data.toMeetUpParticipantModel()
        }

    override suspend fun getMeetUpDetail(promiseId: Int): Result<MeetUpDetailModel> =
        runCatching {
            val response = meetUpService.getMeetUpDetail(promiseId)
            response.data.toMeetUpDetailModel()
        }

    override suspend fun getMeetUpFriendList(promiseId: Int): Result<List<MeetUpSealedItem>> {
        return runCatching {
            val response = meetUpService.getMeetUpParticipantList(promiseId)
            response.data.toMeetUpSealedItem()
        }
    }
}
