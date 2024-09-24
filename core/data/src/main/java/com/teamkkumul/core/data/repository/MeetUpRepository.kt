package com.teamkkumul.core.data.repository

import com.teamkkumul.model.LatePersonModel
import com.teamkkumul.model.MeetUpDetailModel
import com.teamkkumul.model.MeetUpParticipantModel
import com.teamkkumul.model.MeetUpParticipantItem

interface MeetUpRepository {
    suspend fun getMeetUpParticipant(promiseId: Int): Result<MeetUpParticipantModel>
    suspend fun getMeetUpDetail(promiseId: Int): Result<MeetUpDetailModel>
    suspend fun getMeetUpFriendList(promiseId: Int): Result<List<MeetUpParticipantItem>>
    suspend fun getLateComersList(promiseId: Int): Result<LatePersonModel>
    suspend fun patchMeetUpComplete(promiseId: Int): Result<Unit>
    suspend fun leaveMeetUp(promiseId: Int): Result<Unit>
    suspend fun deleteMeetUp(promiseId: Int): Result<Unit>
}
