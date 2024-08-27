package com.teamkkumul.core.data.repository

import com.teamkkumul.model.MeetUpCreateLocationModel
import com.teamkkumul.model.MeetUpCreateModel
import com.teamkkumul.model.MeetUpEditParticipantModel

interface MeetUpCreateLocationRepository {
    suspend fun getMeetUpCreateLocation(q: String): Result<List<MeetUpCreateLocationModel.Location>>
    suspend fun postMeetUpCreate(meetingId: Int, meetUpCreateModel: MeetUpCreateModel): Result<Int>
    suspend fun getMeetUpEditParticipant(promiseId: Int): Result<List<MeetUpEditParticipantModel.Member>>
    suspend fun putMeetUpEdit(promiseId: Int, meetUpCreateModel: MeetUpCreateModel): Result<Int>
}
