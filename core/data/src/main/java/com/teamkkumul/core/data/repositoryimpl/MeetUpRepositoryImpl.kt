package com.teamkkumul.core.data.repositoryimpl

import com.teamkkumul.core.data.mapper.toLatePersonModel
import com.teamkkumul.core.data.mapper.toMeetUpDetailModel
import com.teamkkumul.core.data.mapper.toMeetUpParticipantModel
import com.teamkkumul.core.data.mapper.toMeetUpSealedItem
import com.teamkkumul.core.data.repository.MeetUpRepository
import com.teamkkumul.core.data.utils.handleThrowable
import com.teamkkumul.core.network.api.MeetUpService
import com.teamkkumul.model.LatePersonModel
import com.teamkkumul.model.MeetUpDetailModel
import com.teamkkumul.model.MeetUpParticipantModel
import com.teamkkumul.model.MeetUpParticipantItem
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

    override suspend fun getMeetUpFriendList(promiseId: Int): Result<List<MeetUpParticipantItem>> {
        return runCatching {
            val response = meetUpService.getMeetUpParticipantList(promiseId)
            response.data?.toMeetUpSealedItem() ?: throw Exception("null")
        }
    }

    override suspend fun getLateComersList(promiseId: Int): Result<LatePersonModel> = runCatching {
        meetUpService.getLateComersList(promiseId).data?.toLatePersonModel()
    }.mapCatching { latePersonModel ->
        requireNotNull(latePersonModel)
    }.recoverCatching {
        return it.handleThrowable()
    }

    override suspend fun patchMeetUpComplete(promiseId: Int): Result<Unit> = runCatching {
        meetUpService.patchMeetUpComplete(promiseId)
        Unit
    }.recoverCatching {
        return it.handleThrowable()
    }

    override suspend fun leaveMeetUp(promiseId: Int): Result<Unit> = runCatching {
        meetUpService.leaveMeetUp(promiseId)
        Unit
    }.recoverCatching {
        return it.handleThrowable()
    }

    override suspend fun deleteMeetUp(promiseId: Int): Result<Unit> = runCatching {
        meetUpService.deleteMeetUp(promiseId)
        Unit
    }.recoverCatching {
        return it.handleThrowable()
    }
}
