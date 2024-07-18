package com.teamkkumul.core.network.api

import com.teamkkumul.core.network.dto.response.BaseResponse
import com.teamkkumul.core.network.dto.response.ResponseHomeMembersReadyStatus
import com.teamkkumul.core.network.dto.response.ResponseHomeReadyStatusDto
import com.teamkkumul.core.network.dto.response.ResponseHomeUpComingMeetingDto
import com.teamkkumul.core.network.dto.response.ResponseTodayMeetingDto
import com.teamkkumul.core.network.dto.response.ResponseUserDto
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface HomeService {
    @GET("api/v1/users/me")
    suspend fun getUserInfo(): BaseResponse<ResponseUserDto>

    @GET("api/v1/promises/today/next")
    suspend fun getTodayMeeting(): BaseResponse<ResponseTodayMeetingDto>

    @GET("api/v1/promises/upcoming")
    suspend fun getUpComingMeeting(): BaseResponse<ResponseHomeUpComingMeetingDto>

    @GET("api/v1/promises/{promiseId}/status")
    suspend fun getReadyStatus(
        @Path("promiseId") promiseId: Int,
    ): BaseResponse<ResponseHomeReadyStatusDto?>

    @PATCH("api/v1/promises/{promiseId}/preparation")
    suspend fun patchReady(
        @Path("promiseId") promiseId: Int,
    ): BaseResponse<Unit>

    @PATCH("api/v1/promises/{promiseId}/departure")
    suspend fun patchMoving(
        @Path("promiseId") promiseId: Int,
    ): BaseResponse<Unit>

    @PATCH("api/v1/promises/{promiseId}/arrival")
    suspend fun patchCompleted(
        @Path("promiseId") promiseId: Int,
    ): BaseResponse<Unit>

    @GET("api/v1/promises/{promiseId}/participants")
    suspend fun getMembersReadyStatus(
        @Path("promiseId") promiseId: Int,
    ): BaseResponse<ResponseHomeMembersReadyStatus?>
}
