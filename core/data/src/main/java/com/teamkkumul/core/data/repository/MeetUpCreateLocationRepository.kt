package com.teamkkumul.core.data.repository

import com.teamkkumul.model.MeetUpCreateLocationModel

interface MeetUpCreateLocationRepository {
    suspend fun getMeetUpCreateLocation(q: String): Result<List<MeetUpCreateLocationModel.Location>>
}
