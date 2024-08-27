package com.teamkkumul.core.network.api

import com.teamkkumul.core.network.dto.request.RequestMeetUpCreateDto
import com.teamkkumul.core.network.dto.response.BaseResponse
import com.teamkkumul.core.network.dto.response.ResponseMeetUpCreateLocationDto
import com.teamkkumul.core.network.dto.response.ResponseMeetUpDetailDto
import com.teamkkumul.core.network.dto.response.ResponseMeetUpEditParticipantDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface MeetUpCreateService {
    @GET("/api/v1/locations")
    suspend fun getMeetUpCreateLocation(
        @Query("q") q: String,
    ): BaseResponse<ResponseMeetUpCreateLocationDto>

    @POST("/api/v1/meetings/{meetingId}/promises")
    suspend fun postNewMeetUp(
        @Path("meetingId") meetingId: Int,
        @Body request: RequestMeetUpCreateDto,
    ): BaseResponse<ResponseMeetUpDetailDto>

    @GET("/api/v1/promises/{promiseId}/members")
    suspend fun getMeetUpEditParticipant(
        @Path("promiseId") promiseId: Int,
    ): BaseResponse<ResponseMeetUpEditParticipantDto>

    @PUT("/api/v1/promises/{promiseId}")
    suspend fun putMeetUpEdit(
        @Path("promiseId") promiseId: Int,
        @Body request: RequestMeetUpCreateDto,
    ): BaseResponse<ResponseMeetUpDetailDto>
}
