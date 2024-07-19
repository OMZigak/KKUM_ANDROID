package com.teamkkumul.core.network.api

import com.teamkkumul.core.network.api.util.KeyStorage.API
import com.teamkkumul.core.network.api.util.KeyStorage.MEETINGS
import com.teamkkumul.core.network.api.util.KeyStorage.REGISTER
import com.teamkkumul.core.network.api.util.KeyStorage.V1
import com.teamkkumul.core.network.dto.request.RequestAddNewGroupDto
import com.teamkkumul.core.network.dto.request.RequestEnterInvitationCodeDto
import com.teamkkumul.core.network.dto.response.BaseResponse
import com.teamkkumul.core.network.dto.response.ResponseAddNewGroupDto
import com.teamkkumul.core.network.dto.response.ResponseEnterInvitationCodeDto
import retrofit2.http.Body
import retrofit2.http.POST

interface MeetingsService {
    @POST("$API/$V1/$MEETINGS")
    suspend fun addNewGroup(
        @Body request: RequestAddNewGroupDto,
    ): BaseResponse<ResponseAddNewGroupDto>

    @POST("/$API/$V1/$MEETINGS/$REGISTER")
    suspend fun enterInvitationCode(
        @Body request: RequestEnterInvitationCodeDto,
    ): BaseResponse<ResponseEnterInvitationCodeDto>
}
