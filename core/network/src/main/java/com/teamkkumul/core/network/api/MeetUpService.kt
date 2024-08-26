package com.teamkkumul.core.network.api

import com.teamkkumul.core.network.dto.response.BaseResponse
import com.teamkkumul.core.network.dto.response.ResponseLatePersonDto
import com.teamkkumul.core.network.dto.response.ResponseMeetUpDetailDto
import com.teamkkumul.core.network.dto.response.ResponseMeetUpParticipantDto
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
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

    @GET("/api/v1/promises/{promiseId}/tardy")
    suspend fun getLateComersList(
        @Path("promiseId") promiseId: Int,
    ): BaseResponse<ResponseLatePersonDto>

    @PATCH("/api/v1/promises/{promiseId}/completion")
    suspend fun patchMeetUpComplete(
        @Path("promiseId") promiseId: Int,
    ): BaseResponse<Unit>

    @DELETE("/api/v1/promises/{promiseId}/leave")
    suspend fun leaveMeetUp(
        @Path("promiseId") promiseId: Int,
    ): BaseResponse<Unit>

    @DELETE("/api/v1/promises/{promiseId}")
    suspend fun deleteMeetUp(
        @Path("promiseId") promiseId: Int,
    ): BaseResponse<Unit>
}
