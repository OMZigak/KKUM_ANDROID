package com.teamkkumul.core.data.repository

import com.teamkkumul.model.MeetUpDetailModel
import com.teamkkumul.model.MeetUpParticipantModel
import com.teamkkumul.model.MeetUpSealedItem

interface MeetUpRepository {
    suspend fun getMeetUpParticipant(promiseId: Int): Result<MeetUpParticipantModel>
    suspend fun getMeetUpDetail(promiseId: Int): Result<MeetUpDetailModel>
    suspend fun getMeetUpFriendList(promiseId: Int): Result<List<MeetUpSealedItem>>
}
