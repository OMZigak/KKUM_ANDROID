package com.teamkkumul.core.network.api

import com.teamkkumul.core.network.dto.request.RequestAddNewGroupDto
import com.teamkkumul.core.network.dto.request.RequestEnterInvitationCodeDto
import com.teamkkumul.core.network.dto.response.BaseResponse
import com.teamkkumul.core.network.dto.response.ResponseAddNewGroupDto
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MeetingsService {
    @POST("$API/$V1/$MEETINGS")
    @Headers("Content-Type: application/json")
    suspend fun addNewGroup(
        @Body request: RequestAddNewGroupDto,
    ): BaseResponse<ResponseAddNewGroupDto>

    @POST("/$API/$V1/$MEETINGS/$REGISTER")
    @Headers("Content-Type: application/json")
    suspend fun enterInvitationCode(
        @Body request: RequestEnterInvitationCodeDto,
    ): BaseResponse<Unit>

    companion object {
        const val API = "api"
        const val V1 = "v1"
        const val MEETINGS = "meetings"
        const val REGISTER = "register"
    }
}
