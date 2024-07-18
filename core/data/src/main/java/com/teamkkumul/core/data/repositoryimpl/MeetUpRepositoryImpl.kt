package com.teamkkumul.core.data.repositoryimpl

import com.teamkkumul.core.data.mapper.toLatePersonModel
import com.teamkkumul.core.data.mapper.toMeetUpDetailModel
import com.teamkkumul.core.data.mapper.toMeetUpParticipantModel
import com.teamkkumul.core.data.mapper.toMeetUpSealedItem
import com.teamkkumul.core.data.repository.MeetUpRepository
import com.teamkkumul.core.network.api.MeetUpService
import com.teamkkumul.model.LatePersonModel
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
            response.data?.toMeetUpParticipantModel() ?: throw Exception("null")
        }

    override suspend fun getMeetUpDetail(promiseId: Int): Result<MeetUpDetailModel> =
        runCatching {
            val response = meetUpService.getMeetUpDetail(promiseId)
            response.data?.toMeetUpDetailModel() ?: throw Exception("null")
        }

    override suspend fun getMeetUpFriendList(promiseId: Int): Result<List<MeetUpSealedItem>> {
        return runCatching {
            val response = meetUpService.getMeetUpParticipantList(promiseId)
            response.data?.toMeetUpSealedItem() ?: throw Exception("null")
        }
    }

    override suspend fun getLateComersList(promiseId: Int): Result<LatePersonModel> {
        return runCatching {
            val response = meetUpService.getLateComersList(promiseId)
            response.data?.toLatePersonModel() ?: throw Exception("null")
        }
    }

    override suspend fun patchMeetUpComplete(promiseId: Int): Result<Unit> {
        return runCatching {
            val response = meetUpService.patchMeetUpComplete(promiseId)
            response.data ?: throw Exception("null")
        }
    }
}
