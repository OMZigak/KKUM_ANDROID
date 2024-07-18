package com.teamkkumul.core.network.api

import com.teamkkumul.core.network.dto.response.BaseResponse
import com.teamkkumul.core.network.dto.response.ResponseMeetUpDetailDto
import com.teamkkumul.core.network.dto.response.ResponseMeetUpParticipantDto
import retrofit2.http.GET
import retrofit2.http.Path

interface MeetUpService {
    @GET("/api/v1/promises/{promiseId}/participants")
    suspend fun getMeetUpParticipantList(
        @Path("promiseId") promiseId: Int,
    ): BaseResponse<ResponseMeetUpParticipantDto>

    @GET("/api/v1/promises/{promiseId}")
    suspend fun getMeetUpDetail(
        @Path("promiseId") promiseId: Int,
    ): BaseResponse<ResponseMeetUpDetailDto>
}
