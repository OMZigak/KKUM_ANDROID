package com.teamkkumul.core.network.api

import com.teamkkumul.core.network.dto.response.BaseResponse
import com.teamkkumul.core.network.dto.response.ResponseTodayMeetingDto
import com.teamkkumul.core.network.dto.response.ResponseUserDto
import retrofit2.http.GET

interface HomeService {
    @GET("api/v1/users/me")
    suspend fun getUserInfo(): BaseResponse<ResponseUserDto>

    @GET("api/v1/promises/today/next")
    suspend fun getTodayMeeting(): BaseResponse<ResponseTodayMeetingDto>
}
