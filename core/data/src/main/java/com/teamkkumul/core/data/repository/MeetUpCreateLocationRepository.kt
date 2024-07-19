package com.teamkkumul.core.data.repository

import com.teamkkumul.model.MeetUpCreateLocationModel
import com.teamkkumul.model.MeetUpCreateModel

interface MeetUpCreateLocationRepository {
    suspend fun getMeetUpCreateLocation(q: String): Result<List<MeetUpCreateLocationModel.Location>>
    suspend fun postMeetUpCreate(meetingId: Int, meetUpCreateModel: MeetUpCreateModel): Result<Int>
}
