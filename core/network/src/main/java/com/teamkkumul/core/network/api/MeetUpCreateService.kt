package com.teamkkumul.core.network.api

import com.teamkkumul.core.network.dto.response.BaseResponse
import com.teamkkumul.core.network.dto.response.ResponseMeetUpCreateLocationDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MeetUpCreateService {
    @GET("/api/v1/locations")
    suspend fun getMeetUpCreateLocation(
        @Query("q") q: String,
    ): BaseResponse<ResponseMeetUpCreateLocationDto>
}
